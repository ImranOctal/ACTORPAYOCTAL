package com.octal.actorpay.ui.shippingaddress.details

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.hbb20.CountryCodePicker
import com.octal.actorpay.MainActivity
import com.octal.actorpay.R
import com.octal.actorpay.app.MyApplication
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.base.ResponseSealed
import com.octal.actorpay.databinding.FragmentShippingAddressBinding
import com.octal.actorpay.databinding.FragmentShippingAddressDetailsBinding
import com.octal.actorpay.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpay.repositories.retrofitrepository.models.order.PlaceOrderParamas
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressItem
import com.octal.actorpay.repositories.retrofitrepository.models.shipping.ShippingAddressListResponse
import com.octal.actorpay.ui.shippingaddress.ShippingAddressViewModel
import com.octal.actorpay.utils.CommonDialogsUtils
import com.octal.actorpay.utils.LocationUtils
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import java.lang.Exception
import java.util.*


class ShippingAddressDetailsFragment : BaseFragment() {

    private var _binding: FragmentShippingAddressDetailsBinding? = null
    private val binding get() = _binding!!
    private val shippingAddressViewModel: ShippingAddressDetailsViewModel by inject()
    var isSave=true
    var isComingFirst=true
    private var mMap: GoogleMap? = null
    lateinit var mLocationUtils: LocationUtils
    var userlat = 0.0
    var userlong = 0.0

    override fun WorkStation() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentShippingAddressDetailsBinding.inflate(inflater, container, false)
        setTitle("My Address")
        showHideBottomNav(false)
        showHideCartIcon(false)
        showHideFilterIcon(false)
        apiResponse()



        if(shippingAddressItem!=null){
            isSave=false
            binding.save.text=getString(R.string.update)
            binding.name.setText(shippingAddressItem!!.name)
            binding.addressTitle.setText(shippingAddressItem!!.title)
            binding.addressArea.setText(shippingAddressItem!!.area)
            binding.addressLine1.setText(shippingAddressItem!!.addressLine1)
            binding.addressLine2.setText(shippingAddressItem!!.addressLine2)
            binding.addressZipcode.setText(shippingAddressItem!!.zipCode)
            binding.addressCity.setText(shippingAddressItem!!.city)
            binding.addressState.setText(shippingAddressItem!!.state)
            binding.addressCountry.setText(shippingAddressItem!!.country)
            binding.addressPrimaryContact.setText(shippingAddressItem!!.primaryContactNumber)
            if(shippingAddressItem!!.extensionNumber !=null && shippingAddressItem!!.extensionNumber.equals("").not()) {
                shippingAddressItem!!.extensionNumber =
                    shippingAddressItem!!.extensionNumber!!.replace("+", "")
                binding.ccp.setCountryForPhoneCode(shippingAddressItem!!.extensionNumber!!.toInt())
                binding.ccp2.setCountryForPhoneCode(shippingAddressItem!!.extensionNumber!!.toInt())
            }
            binding.addressSecondaryContact.setText(shippingAddressItem!!.secondaryContactNumber)
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        mLocationUtils = LocationUtils(requireActivity(), false)
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
                catch (e:Exception){
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
                .zoom(17f) // Sets the zoom // Sets the orientation of the camera to east // Sets the tilt of the camera to 30 degrees
                .bearing(30f)
                .tilt(45f)
                .build() // Creates a CameraPosition from the builder

            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            getAddress(userlat,userlong)



        }
        mLocationUtils.initConnection()

        binding.ccp.setOnCountryChangeListener {
            binding.ccp2.setCountryForPhoneCode(binding.ccp.selectedCountryCode.toInt())
        }
        binding.ccp2.setOnCountryChangeListener {
            binding.ccp.setCountryForPhoneCode(binding.ccp2.selectedCountryCode.toInt())
        }

        binding.save.setOnClickListener {
            val name=binding.name.text.toString().trim()
            val title=binding.addressTitle.text.toString().trim()
            val area=binding.addressArea.text.toString().trim()
            val add_line_1=binding.addressLine1.text.toString().trim()
            val add_line_2=binding.addressLine2.text.toString().trim()
            val zipcode=binding.addressZipcode.text.toString().trim()
            val city=binding.addressCity.text.toString().trim()
            val state=binding.addressState.text.toString().trim()
            val country=binding.addressCountry.text.toString().trim()
            val p_contact=binding.addressPrimaryContact.text.toString().trim()
            val s_contact=binding.addressSecondaryContact.text.toString().trim()

            var isValid=true
            if(name.equals("")){
                binding.name.error="Please Enter Name"
                isValid=false
            }
            if(add_line_1.equals("")){
                binding.addressLine1.error="Please Enter Address Line 1"
                isValid=false
            }
            if(zipcode.length<6){
                binding.addressZipcode.error="Please Enter Valid Zipcode"
                isValid=false
            }
            if(city.equals("")){
                binding.addressCity.error="Please Enter City"
                isValid=false
            }
            if(state.equals("")){
                binding.addressState.error="Please Enter State"
                isValid=false
            }
            if(country.equals("")){
                binding.addressCountry.error="Please Enter Country"
                isValid=false
            }
            if(p_contact.length<5){
                binding.addressPrimaryContact.error="Please Enter Valid Contact"
                isValid=false
            }
            if(s_contact.length<5){
                binding.addressSecondaryContact.error="Please Enter Valid Contact"
                isValid=false
            }
            if(title.length<3){
                binding.addressTitle.error="Please Enter Valid Address Type"
                isValid=false
            }
            if(area.length<3){
                binding.addressArea.error="Please Enter Valid Address Area"
                isValid=false
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
                                name,
                                title,
                                area,
                                p_contact,
                                binding.ccp.selectedCountryCode,
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
                                name,
                                title,
                                area,
                                p_contact,
                                binding.ccp.selectedCountryCode,
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



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLocationPermission()

    }
    fun checkLocationPermission(): Boolean {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
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
//            val mapFragment = childFragmentManager.findFragmentById(R.id.map_map) as SupportMapFragment?
//            mapFragment?.getMapAsync(callback)
            /*val mLocationUtils = LocationUtils(requireActivity(), false)
            {
            }
            mLocationUtils.initConnection()*/
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
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    //Never ask again selected, or device policy prohibits the app from having that permission.
                    //So, disable that feature, or fall back to another situation...
//                    CommonDialogsUtils.showPermissionDialog(requireContext())
                }
            }
        }

    private val callback = OnMapReadyCallback { googleMap ->




        /*   googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
          googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
  */
        mMap = googleMap

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

//        getAddress(userlat,userlong)
        }




//                setMarkerMap(userlat, userlong,destLat,destLong)
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
                binding.addressCountry.setText(country)

        }
    }

    fun apiResponse() {

        lifecycleScope.launchWhenStarted {

            shippingAddressViewModel.responseLive.collect { event ->
                when (event) {
                    is ResponseSealed.loading -> {
                        shippingAddressViewModel.methodRepo.showLoadingDialog(requireContext())
                    }
                    is ResponseSealed.Success -> {
                        shippingAddressViewModel.methodRepo.hideLoadingDialog()
                        when (event.response) {
                            is SuccessResponse -> {
                               requireActivity().onBackPressed()
                            }
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        if (event.message!!.code == 403) {
                            forcelogout(shippingAddressViewModel.methodRepo)
                        }
                        shippingAddressViewModel.methodRepo.hideLoadingDialog()
                        showCustomToast(event.message.message)
                    }
                    is ResponseSealed.Empty -> {
                        shippingAddressViewModel.methodRepo.hideLoadingDialog()

                    }
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        shippingAddressItem=null
    }

    companion object {
        var shippingAddressItem:ShippingAddressItem?=null

        @JvmStatic
        fun newInstance() =
            ShippingAddressDetailsFragment().apply {

            }
    }
}