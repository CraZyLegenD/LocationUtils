package com.crazylegend.locationutils

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.crazylegend.locationhelpers.*
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import kotlinx.android.synthetic.main.activity_main.*

class SampleActivity : AppCompatActivity() {

    private var request: LocationRequest? = null
    private var fusedCallback: LocationCallback? = null
    private val fusedLocation get() = fusedLocationProvider
    private val REQUEST_CHECK_SETTINGS = 300
    private val PERMISSION_ID = 344
    private val ENABLE_GPS = 388


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    @SuppressLint("SetTextI18n")
    private fun setupLocation() {
        request = initFusedLocationRequest {
            interval = 5000
            fastestInterval = 3000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        request?.setup(this,
                onError = {
                    it.printStackTrace()
                },
                resolvableApiException = {
                    it.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                },
                onSuccess = {

                })
        fusedCallback = fusedLocationCallback { _, lastLocation ->
            text.text = "${lastLocation.latitude}\n${lastLocation.longitude}"
            Log.d("LAST LOCATION", "${lastLocation.latitude} ${lastLocation.longitude}")
        }
        fusedLocation.startLocationUpdates(request!!, fusedCallback!!)
    }

    override fun onStop() {
        super.onStop()
        fusedCallback?.let { fusedLocation.stopLocationUpdates(it) }
    }

    override fun onResume() {
        super.onResume()

        //this code is for demonstration purposes, make it suit your own please
        checkProcedure()
    }

    private fun checkProcedure() {
        if (checkPermissions()) {
            if (isLocationEnabled)
                setupLocation()
            else
                enableGPS(ENABLE_GPS)
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                ==
                PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_ID -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setupLocation()
                }
            }
            ENABLE_GPS -> {
                if (isLocationEnabled) {
                    checkProcedure()
                }
            }
        }
    }
}
