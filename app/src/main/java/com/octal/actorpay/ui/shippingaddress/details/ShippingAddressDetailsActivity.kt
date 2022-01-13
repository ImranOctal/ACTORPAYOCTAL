package com.octal.actorpay.ui.shippingaddress.details

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.octal.actorpay.R
import com.octal.actorpay.app.MyApplication
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.databinding.ActivityPlaceOrderBinding
import com.octal.actorpay.databinding.ActivityShippingAddressDetailsBinding
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressItem
import com.octal.actorpay.ui.auth.LoginActivity
import com.octal.actorpay.utils.CommonDialogsUtils
import com.octal.actorpay.utils.GlobalData
import com.octal.actorpay.utils.LocationUtils
import com.octal.actorpay.utils.WorkaroundMapFragment
import com.octal.actorpay.utils.countrypicker.CountryPicker
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import java.lang.Exception
import java.util.*





class ShippingAddressDetailsActivity : FragmentActivity() {

    private lateinit var binding: ActivityShippingAddressDetailsBinding
    private val shippingAddressViewModel: ShippingAddressDetailsViewModel by inject()
    var isSave=true
    var isComingFirst=true
    private var mMap: GoogleMap? = null
    lateinit var mLocationUtils: LocationUtils
    var userlat = 0.0
    var userlong = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shipping_address_details)
        checkLocationPermission()
        apiResponse()

        val codeList= mutableListOf<String>()
        val countryList= mutableListOf<String>()

        GlobalData.allCountries.forEach {
            val code=it.countryCode
            codeList.add(code)
        }
        GlobalData.allCountries.forEach {
            val country=it.country
            countryList.add(country)
        }

        if(GlobalData.allCountries.size>0){
            binding.codePicker.text=GlobalData.allCountries[0].countryCode
            binding.codePicker2.text=GlobalData.allCountries[0].countryCode
            binding.countryPicker.text=GlobalData.allCountries[0].country
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
            CountryPicker(this,shippingAddressViewModel.methodRepo,GlobalData.allCountries){
                binding.countryPicker.text=GlobalData.allCountries[it].country
                binding.codePicker.text=GlobalData.allCountries[it].countryCode
                binding.codePicker2.text=GlobalData.allCountries[it].countryCode

            }.show()
        }
        if(shippingAddressItem !=null){
            isSave=false
            binding.save.text=getString(R.string.update)
//            binding.name.setText(shippingAddressItem!!.name)
            binding.addressTitle.setText(shippingAddressItem!!.title)
            binding.addressArea.setText(shippingAddressItem!!.area)
            binding.addressLine1.setText(shippingAddressItem!!.addressLine1)
            binding.addressLine2.setText(shippingAddressItem!!.addressLine2)
            binding.addressZipcode.setText(shippingAddressItem!!.zipCode)
            binding.addressCity.setText(shippingAddressItem!!.city)
            binding.addressState.setText(shippingAddressItem!!.state)
            if(countryList.contains(shippingAddressItem!!.country)){
                binding.countryPicker.text=shippingAddressItem!!.country
            }
            if(shippingAddressItem!!.extensionNumber !=null && shippingAddressItem!!.extensionNumber.equals("").not()) {

                if (codeList.contains(shippingAddressItem!!.extensionNumber)) {

                    binding.codePicker.text=shippingAddressItem!!.extensionNumber
                    binding.codePicker2.text=shippingAddressItem!!.extensionNumber
                }
            }

            binding.addressPrimaryContact.setText(shippingAddressItem!!.primaryContactNumber)
            binding.addressSecondaryContact.setText(shippingAddressItem!!.secondaryContactNumber)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as WorkaroundMapFragment?
        mapFragment?.getMapAsync(callback)

        mLocationUtils = LocationUtils(this, false)
        {
            if(isSave) {
                userlat = it.latitude
                userlong = it.longitude
            }
            else{
                try {
                    userlat= shippingAddressItem!!.latitude.toDouble()
                    userlong= shippingAddressItem!!.longitude.toDouble()
                }
                catch (e: Exception){
                    userlat = 0.0
                    userlong = 0.0
                }
            }

            val cameraPosition = CameraPosition.Builder()
                .target(
                    LatLng(
                        userlat,
                        userlong
                    )
                ) // Sets the center of the map to Mountain View
                .zoom(15f) // Sets the zoom // Sets the orientation of the camera to east // Sets the tilt of the camera to 30 degrees
                .bearing(30f)
                .tilt(45f)
                .build() // Creates a CameraPosition from the builder

            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            getAddress(userlat,userlong)
        }
        mLocationUtils.initConnection()

        binding.save.setOnClickListener {
            validate()
        }
        binding.back.setOnClickListener {
            onBackPressed()
        }

    }

    fun validate(){
        val name=binding.name.text.toString().trim()
        val title=binding.addressTitle.text.toString().trim()
        val area=binding.addressArea.text.toString().trim()
        val add_line_1=binding.addressLine1.text.toString().trim()
        val add_line_2=binding.addressLine2.text.toString().trim()
        val zipcode=binding.addressZipcode.text.toString().trim()
        val city=binding.addressCity.text.toString().trim()
        val state=binding.addressState.text.toString().trim()
        val country=binding.countryPicker.text.toString().trim()
        val p_contact=binding.addressPrimaryContact.text.toString().trim()
        val s_contact=binding.addressSecondaryContact.text.toString().trim()

        var isValid=true

        if(s_contact.length<5){
            binding.addressSecondaryContact.error="Please Enter Valid Contact"
            isValid=false
            binding.addressSecondaryContact.requestFocus()
        }

        if(p_contact.length<5){
            binding.addressPrimaryContact.error="Please Enter Valid Contact"
            isValid=false
            binding.addressPrimaryContact.requestFocus()
        }

        if(state.equals("")){
            binding.addressState.error="Please Enter State"
            isValid=false
            binding.addressState.requestFocus()
        }

        if(city.equals("")){
            binding.addressCity.error="Please Enter City"
            isValid=false
            binding.addressCity.requestFocus()
        }

        if(area.length<3){
            binding.addressArea.error="Please Enter Valid Address Area"
            isValid=false
            binding.addressArea.requestFocus()
        }


        if(zipcode.length<6){
            binding.addressZipcode.error="Please Enter Valid Zipcode"
            isValid=false
            binding.addressZipcode.requestFocus()
        }
        if(add_line_1.equals("")){
            binding.addressLine1.error="Please Enter Address Line 1"
            isValid=false
            binding.addressLine1.requestFocus()
        }
       /* if(name.equals("")){
            binding.name.error="Please Enter Name"
            isValid=false
            binding.name.requestFocus()
        }*/
        if(title.length<3){
            binding.addressTitle.error="Please Enter Valid Address Type"
            isValid=false
            binding.addressTitle.requestFocus()

        }
        if(isValid){
//            Toast.makeText(requireContext(),"done",Toast.LENGTH_SHORT).show()
            lifecycleScope.launchWhenCreated {
                shippingAddressViewModel.methodRepo.dataStore.getUserId().collect {
                        userID->
                    if(isSave){
                        shippingAddressViewModel.addAddress(
                            ShippingAddressItem(
                                add_line_1,
                                add_line_2,
                                zipcode,
                                city,
                                state,
                                country,
                                userlat.toString(),
                                userlong.toString(),
                                "",
                                title,
                                area,
                                p_contact,
                                binding.codePicker.text.toString().trim(),
                                s_contact,
                                false,
                                null,
                                userID,
                                null
                            )
                        )
                    }
                    else {
                        shippingAddressViewModel.updateAddress(
                            ShippingAddressItem(
                                add_line_1,
                                add_line_2,
                                zipcode,
                                city,
                                state,
                                country,
                                userlat.toString(),
                                userlong.toString(),
                                "",
                                title,
                                area,
                                p_contact,
                                binding.codePicker.text.toString().trim(),
                                s_contact,
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

    fun checkLocationPermission(): Boolean {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permReqLuncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            )

            return false
        } else {

            return true
        }
    }

    val permReqLuncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { it ->
            var count = 0
            val totalCount=1
            it.entries.forEach {
                if (it.value) {
                    count++
                }
            }

            if (count == totalCount) {
//                showCustomToast("Permission Granted")
                mLocationUtils.initConnection()
            }
            else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
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
                mMap!!.projection.visibleRegion.latLngBounds.center.let {
                    if(!isComingFirst) {
                        userlat = it.latitude
                        userlong = it.longitude
                        getAddress(userlat, userlong)
                    }
                    else if(isSave){
                        userlat = it.latitude
                        userlong = it.longitude
                        getAddress(userlat, userlong)
                    } else
                    {
                        isComingFirst=false
                    }
                    //AppLogger.w("Latitude is : $latitude Longitude is$longitude")

                }
            }

        }

        if(!isSave) {

            try {
                userlat= shippingAddressItem!!.latitude.toDouble()
                userlong= shippingAddressItem!!.longitude.toDouble()
            }
            catch (e:Exception){
                userlat = 0.0
                userlong = 0.0
            }

            val cameraPosition = CameraPosition.Builder()
                .target(
                    LatLng(
                        userlat,
                        userlong
                    )
                ) // Sets the center of the map to Mountain View
                .zoom(17f) // Sets the zoom // Sets the orientation of the camera to east // Sets the tilt of the camera to 30 degrees
                .bearing(30f)
                .tilt(45f)
                .build() // Creates a CameraPosition from the builder

            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        }

    }


    fun getAddress(lat: Double, lng: Double) {


        val geocoder = Geocoder(MyApplication.application, Locale.getDefault())

        val    // TODO: 11/1/20 find address
                addresses =
            geocoder.getFromLocation(lat, lng, 1)
        if (addresses.size > 0) {
            val address = addresses.get(0).getAddressLine(0);
            val country = addresses.get(0).countryName;
            val postalCode=addresses.get(0).postalCode

            binding.addressLine1.setText(address)
            binding.addressZipcode.setText(postalCode)
//            binding.addressCountry.setText(country)

        }
    }

    fun apiResponse() {

        lifecycleScope.launchWhenStarted {

            shippingAddressViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        shippingAddressViewModel.methodRepo.showLoadingDialog(this@ShippingAddressDetailsActivity)
                    }
                    is ResponseSealed.Success -> {
                        shippingAddressViewModel.methodRepo.hideLoadingDialog()
                        when (event.response) {
                            is SuccessResponse -> {
                                onBackPressed()
                            }
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        if (event.message!!.code == 403) {
                            forcelogout(shippingAddressViewModel.methodRepo)
                        }
                        shippingAddressViewModel.methodRepo.hideLoadingDialog()
                        Toast.makeText(this@ShippingAddressDetailsActivity,event.message.message,Toast.LENGTH_SHORT).show()
                    }
                    is ResponseSealed.Empty -> {
                        shippingAddressViewModel.methodRepo.hideLoadingDialog()

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

    override fun onDestroy() {
        super.onDestroy()
        shippingAddressItem =null
    }

    fun forcelogout(methodRepo: MethodsRepo){
        CommonDialogsUtils.showCommonDialog(this,methodRepo, "Log Out ",
            "Session Expire", false, false, true, false,
            object : CommonDialogsUtils.DialogClick {
                override fun onClick() {
//                    viewModel.shared.Logout()
                    lifecycleScope.launchWhenCreated {
                        methodRepo.dataStore.logOut()
                        methodRepo.dataStore.setIsIntro(true)
                        startActivity(Intent(this@ShippingAddressDetailsActivity, LoginActivity::class.java))
                        finishAffinity()
                    }
                }
                override fun onCancel() {
                }
            })
    }



    companion object {
        var shippingAddressItem: ShippingAddressItem?=null
    }
}