package com.octal.actorpayuser.ui.shippingaddress.details

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseActivity
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.ActivityShippingAddressDetailsBinding
import com.octal.actorpayuser.di.models.CoroutineContextProvider
import com.octal.actorpayuser.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpayuser.repositories.retrofitrepository.models.shipping.ShippingAddressItem
import com.octal.actorpayuser.utils.GlobalData
import com.octal.actorpayuser.utils.LocationUtils
import com.octal.actorpayuser.utils.WorkaroundMapFragment
import com.octal.actorpayuser.utils.countrypicker.CountryPicker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*

class ShippingAddressDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityShippingAddressDetailsBinding
    private val shippingAddressViewModel: ShippingAddressDetailsViewModel by inject()
    private var isSave = true
    private var isComingFirst = true
    private var mMap: GoogleMap? = null
    private lateinit var mLocationUtils: LocationUtils
    private var userLat = 0.0
    private var userLong = 0.0

    var shippingAddressItem: ShippingAddressItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shipping_address_details)

        if (intent != null) {
            if (intent.hasExtra("shippingItem"))
                shippingAddressItem =
                    intent.getSerializableExtra("shippingItem") as ShippingAddressItem

        }

        checkLocationPermission()
        apiResponse()

        val codeList = mutableListOf<String>()
        val countryList = mutableListOf<String>()

        GlobalData.allCountries.forEach {
            val code = it.countryCode
            codeList.add(code)
        }
        GlobalData.allCountries.forEach {
            val country = it.country
            countryList.add(country)
        }

        if (GlobalData.allCountries.size > 0) {
            binding.codePicker.text = GlobalData.allCountries[0].countryCode
            binding.codePicker2.text = GlobalData.allCountries[0].countryCode
            binding.countryPicker.text = GlobalData.allCountries[0].country
        }
        /*binding.codeLayout1.setOnClickListener {
            CountryPicker(this,shippingAddressViewModel.methodRepo,GlobalData.allCountries){
                binding.codePicker.text=GlobalData.allCountries[it].countryCode
                binding.codePicker2.text=GlobalData.allCountries[it].countryCode
            }.show()
        }
        binding.codeLayout2.setOnClickListener {
            CountryPicker(this,shippingAddressViewModel.methodRepo,GlobalData.allCountries){
                binding.codePicker.text=GlobalData.allCountries[it].countryCode
                binding.codePicker2.text=GlobalData.allCountries[it].countryCode
            }.show()
        }*/
        binding.countryLayout.setOnClickListener {
            CountryPicker(this,this, shippingAddressViewModel.methodRepo, GlobalData.allCountries) {
                binding.countryPicker.text = GlobalData.allCountries[it].country
                binding.codePicker.text = GlobalData.allCountries[it].countryCode
                binding.codePicker2.text = GlobalData.allCountries[it].countryCode

            }.show()
        }
        if (shippingAddressItem != null) {
            isSave = false
            binding.save.text = getString(R.string.update)
//            binding.name.setText(shippingAddressItem!!.name)
            binding.addressTitle.setText(shippingAddressItem!!.title)
            binding.addressArea.setText(shippingAddressItem!!.area)
            binding.addressLine1.setText(shippingAddressItem!!.addressLine1)
            binding.addressLine2.setText(shippingAddressItem!!.addressLine2)
            binding.addressZipcode.setText(shippingAddressItem!!.zipCode)
            binding.addressCity.setText(shippingAddressItem!!.city)
            binding.addressState.setText(shippingAddressItem!!.state)
            if (countryList.contains(shippingAddressItem!!.country)) {
                binding.countryPicker.text = shippingAddressItem!!.country
            }
            if (shippingAddressItem!!.extensionNumber != null && shippingAddressItem!!.extensionNumber.equals(
                    ""
                ).not()
            ) {

                if (codeList.contains(shippingAddressItem!!.extensionNumber)) {

                    binding.codePicker.text = shippingAddressItem!!.extensionNumber
                    binding.codePicker2.text = shippingAddressItem!!.extensionNumber
                }
            }

            binding.addressPrimaryContact.setText(shippingAddressItem!!.primaryContactNumber)
            binding.addressSecondaryContact.setText(shippingAddressItem!!.secondaryContactNumber)
        }

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as WorkaroundMapFragment?
        mapFragment?.getMapAsync(callback)

        mLocationUtils = LocationUtils(this, false)
        {

            mLocationUtils.stopLocation()
            if (isSave) {
                userLat = it.latitude
                userLong = it.longitude
            } else {
                try {
                    userLat = shippingAddressItem!!.latitude.toDouble()
                    userLong = shippingAddressItem!!.longitude.toDouble()
                } catch (e: Exception) {
                    userLat = 0.0
                    userLong = 0.0
                }
            }

            val cameraPosition = CameraPosition.Builder()
                .target(
                    LatLng(
                        userLat,
                        userLong
                    )
                ) // Sets the center of the map to Mountain View
                .zoom(15f) // Sets the zoom // Sets the orientation of the camera to east // Sets the tilt of the camera to 30 degrees
                .bearing(30f)
                .tilt(45f)
                .build() // Creates a CameraPosition from the builder

            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            mMap!!.clear()
            mMap!!.addMarker(MarkerOptions().position(LatLng(userLat,userLong)))


            getAddress(userLat, userLong)
        }
        mLocationUtils.initConnection()

        binding.save.setOnClickListener {
            validate()
        }
        binding.back.setOnClickListener {
            onBackPressed()
        }

    }

    fun validate() {
//        val name=binding.name.text.toString().trim()
        val title = binding.addressTitle.text.toString().trim()
        val area = binding.addressArea.text.toString().trim()
        val addLine1 = binding.addressLine1.text.toString().trim()
        val addLine2 = binding.addressLine2.text.toString().trim()
        val zipcode = binding.addressZipcode.text.toString().trim()
        val city = binding.addressCity.text.toString().trim()
        val state = binding.addressState.text.toString().trim()
        val country = binding.countryPicker.text.toString().trim()
        val pContact = binding.addressPrimaryContact.text.toString().trim()
        val sContact = binding.addressSecondaryContact.text.toString().trim()

        var isValid = true

        if (sContact.length < 5) {
            binding.addressSecondaryContact.error = "Please Enter Valid Contact"
            isValid = false
            binding.addressSecondaryContact.requestFocus()
        }

        if (pContact.length < 5) {
            binding.addressPrimaryContact.error = "Please Enter Valid Contact"
            isValid = false
            binding.addressPrimaryContact.requestFocus()
        }

        if (state == "") {
            binding.addressState.error = "Please Enter State"
            isValid = false
            binding.addressState.requestFocus()
        }

        if (city == "") {
            binding.addressCity.error = "Please Enter City"
            isValid = false
            binding.addressCity.requestFocus()
        }

        if (area.length < 3) {
            binding.addressArea.error = "Please Enter Valid Address Area"
            isValid = false
            binding.addressArea.requestFocus()
        }


        if (zipcode.length < 6) {
            binding.addressZipcode.error = "Please Enter Valid Zipcode"
            isValid = false
            binding.addressZipcode.requestFocus()
        }
        if (addLine1 == "") {
            binding.addressLine1.error = "Please Enter Address Line 1"
            isValid = false
            binding.addressLine1.requestFocus()
        }
        /* if(name.equals("")){
             binding.name.error="Please Enter Name"
             isValid=false
             binding.name.requestFocus()
         }*/
        if (title.length < 3) {
            binding.addressTitle.error = "Please Enter Valid Address Type"
            isValid = false
            binding.addressTitle.requestFocus()

        }
        if (isValid) {
//            Toast.makeText(requireContext(),"done",Toast.LENGTH_SHORT).show()
            lifecycleScope.launchWhenCreated {
                shippingAddressViewModel.methodRepo.dataStore.getUserId().collect { userID ->
                    if (isSave) {
                        shippingAddressViewModel.addAddress(
                            ShippingAddressItem(
                                addLine1,
                                addLine2,
                                zipcode,
                                city,
                                state,
                                country,
                                userLat.toString(),
                                userLong.toString(),
                                "",
                                title,
                                area,
                                pContact,
                                binding.codePicker.text.toString().trim(),
                                sContact,
                                false,
                                null,
                                userID,
                                null
                            )
                        )
                    } else {
                        shippingAddressViewModel.updateAddress(
                            ShippingAddressItem(
                                addLine1,
                                addLine2,
                                zipcode,
                                city,
                                state,
                                country,
                                userLat.toString(),
                                userLong.toString(),
                                "",
                                title,
                                area,
                                pContact,
                                binding.codePicker.text.toString().trim(),
                                sContact,
                                shippingAddressItem!!.primary,
                                null,
                                userID,
                                shippingAddressItem!!.id
                            )
                        )
                    }
                }
            }


        }
    }

    private fun checkLocationPermission(): Boolean {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permReqLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            )

            return false
        } else {

            return true
        }
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { it ->
            var count = 0
            val totalCount = 1
            it.entries.forEach {
                if (it.value) {
                    count++
                }
            }

            if (count == totalCount) {
//                showCustomToast("Permission Granted")
                mLocationUtils.initConnection()
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    Log.d("ShippingDetails : ", "Never Ask again Selected")
                    //Never ask again selected, or device policy prohibits the app from having that permission.
                }
            }
        }

    private val callback = OnMapReadyCallback { googleMap ->

        mMap = googleMap
        (supportFragmentManager.findFragmentById(R.id.map) as WorkaroundMapFragment)
            .setListener(object : WorkaroundMapFragment.OnTouchListener {
                override fun onTouch() {
                    binding.scrollView.requestDisallowInterceptTouchEvent(true)
                }
            })

        mMap?.let {
            it.mapType = GoogleMap.MAP_TYPE_NORMAL
            it.isTrafficEnabled = false
            it.isBuildingsEnabled = false
            it.uiSettings.isZoomControlsEnabled = true


                mMap!!.setOnCameraIdleListener {

                   val tempLatLng= mMap!!.cameraPosition.target

                    if (!isComingFirst) {
                        userLat = tempLatLng.latitude
                        userLong = tempLatLng.longitude
                        getAddress(userLat, userLong)
                    } else if (isSave) {
                        userLat = tempLatLng.latitude
                        userLong = tempLatLng.longitude
                        getAddress(userLat, userLong)
                    } else {
                        isComingFirst = false
                    }
                    mMap!!.clear()
                    mMap!!.addMarker(MarkerOptions().position(LatLng(userLat,userLong)))

               /* mMap!!.projection.visibleRegion.latLngBounds.center.let { latLng ->
                    if (!isComingFirst) {
                        userLat = latLng.latitude
                        userLong = latLng.longitude
                        getAddress(userLat, userLong)
                    } else if (isSave) {
                        userLat = latLng.latitude
                        userLong = latLng.longitude
                        getAddress(userLat, userLong)
                    } else {
                        isComingFirst = false
                    }
                    mMap!!.clear()
                    mMap!!.addMarker(MarkerOptions().position(LatLng(userLat,userLong)))
                    //AppLogger.w("Latitude is : $latitude Longitude is$longitude")

                }*/
            }

        }

        if (!isSave) {

            try {
                userLat = shippingAddressItem!!.latitude.toDouble()
                userLong = shippingAddressItem!!.longitude.toDouble()
            } catch (e: Exception) {
                userLat = 0.0
                userLong = 0.0
            }

            val cameraPosition = CameraPosition.Builder()
                .target(
                    LatLng(
                        userLat,
                        userLong
                    )
                ) // Sets the center of the map to Mountain View
                .zoom(17f) // Sets the zoom // Sets the orientation of the camera to east // Sets the tilt of the camera to 30 degrees
                .bearing(30f)
                .tilt(45f)
                .build() // Creates a CameraPosition from the builder
            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        }

    }


    private fun getAddress(lat: Double, lng: Double) {


        lifecycleScope.launch(CoroutineContextProvider().Main) {
            try {
                val geocoder = Geocoder(this@ShippingAddressDetailsActivity, Locale.getDefault())

                // TODO: 11/1/20 find address
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                if (addresses.size > 0) {
                    val address = addresses[0].getAddressLine(0)
//            val country = addresses.get(0).countryName;
                    val postalCode = addresses[0].postalCode

                    binding.addressLine1.setText(address)
                    binding.addressZipcode.setText(postalCode)
//            binding.addressCountry.setText(country)
                }
            }
            catch (e : java.lang.Exception){
                Log.e("Location_Thing", "getAddress: ${e.message}")
            }
        }




    }

    fun apiResponse() {

        lifecycleScope.launchWhenStarted {

            shippingAddressViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        showLoadingDialog()
                    }
                    is ResponseSealed.Success -> {
                        hideLoadingDialog()
                        when (event.response) {
                            is SuccessResponse -> {
                                onBackPressed()
                                shippingAddressViewModel.responseLive.value=ResponseSealed.Empty
                            }
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        hideLoadingDialog()
                        if (event.message!!.code == 403) {
                            forcelogout(shippingAddressViewModel.methodRepo)
                        } else
                            showCustomToast(event.message.message)
                    }
                    is ResponseSealed.Empty -> {
                        hideLoadingDialog()

                    }
                }
            }
        }


    }

    override fun onBackPressed() {
//        super.onBackPressed()
        setResult(RESULT_OK)
        finish()
    }

}