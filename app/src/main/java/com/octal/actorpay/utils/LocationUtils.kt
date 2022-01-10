package com.octal.actorpay.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.*
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.octal.actorpay.MainActivity
import java.lang.Exception
import java.util.*


class LocationUtils(private var pActivity: Activity, val isFromService: Boolean,val result:(LatLng)->Unit) :
    LocationListener {

    private lateinit var mLocation: Location
    private var mLocationManager: LocationManager? = null

    private var mLocationSettingsRequest: LocationSettingsRequest?=null

    //  private var mLocationRequest: LocationRequest? = null
    private val sUINTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val sFINTERVAL: Long = 2000 /* 2 sec */
    private lateinit var locationManager: LocationManager
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // Code for make call back using interface from utility to activity       private var pCallback: Callback = MainActivity()
    override fun onLocationChanged(location: Location) {
//Call the function of interface define in Activity i.e MainActivity
        // pCallback.updateUi(location)
    }


    fun initConnection() {


        mLocationManager = pActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(pActivity)
        val mLocationRequest = LocationRequest.create().apply {
            {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = sUINTERVAL
                fastestInterval = sFINTERVAL
            }

        }
        //----

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
        mLocationSettingsRequest = builder.build()
        builder.setAlwaysShow(true);
        //---

        checkLocation()

        if (ActivityCompat.checkSelfPermission(
                pActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                pActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.getMainLooper()
        )

        /*fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            OnSuccessListener<Location> { location ->
                if (location != null) {
                    mLocation = location
//                    Toast.makeText(pActivity, "" + mLocation.longitude, Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {
            Log.d("sdsd", it.message!!)
        }*/
    }

    fun stopLocation(){
        fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
    }

    private fun checkLocation(): Boolean {

        //showAlert(pActivity.getString(R.string.location_title),  pActivity.getString(R.string.location_message))
        if (isFromService)
            return false
        else
            return isLocationEnabled()

    }

    private fun isLocationEnabled(): Boolean {
        val mSettingsClient = LocationServices.getSettingsClient(pActivity);
        locationManager = pActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER).not()) {
            mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest!!)
                .addOnSuccessListener(pActivity as Activity,
                    object : OnSuccessListener<LocationSettingsResponse> {
                        override fun onSuccess(locationSettingsResponse: LocationSettingsResponse) {
                            Log.d("DFSf", "Dfasdf")
                        }
                    })
                .addOnFailureListener(pActivity as Activity, object : OnFailureListener {
                    override fun onFailure(e: Exception) {
                        val statusCode = (e as ApiException).getStatusCode()
                        when (statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    val rae: ResolvableApiException = e as ResolvableApiException
                                    rae.startResolutionForResult(pActivity, 1);
                                } catch (sie: IntentSender.SendIntentException) {
                                    Log.d("DFSf", "Dfasdf")

                                }
                            }
                        }
                    }
                })
        }



        return true

    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {

    }

    internal var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d("dfa", "" + locationResult.lastLocation.latitude)

            result(LatLng(locationResult.lastLocation.latitude,locationResult.lastLocation.longitude))
//            getAddress(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
//              Toast.makeText(pActivity,""+locationResult.lastLocation.longitude,Toast.LENGTH_SHORT).show()


        }

    }

}