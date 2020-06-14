package com.crazylegend.locationhelpers

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.annotation.RequiresPermission


/**
 * Created by crazy on 4/28/20 to long live and prosper !
 */


val Context.locationManager
    get() = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

fun LocationManager?.stopLocationCallbacks(locationListener: LocationListener) {
    this?.removeUpdates(locationListener)
}

/**
 * Call first
 */
fun locationCallback(locationChanged: (location: Location) -> Unit): LocationListener = locationListenerDSL {
    it?.apply {
        locationChanged(this)
    }
}


/**
 * Create request and start location updates
 */
@RequiresPermission(allOf = [ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION])
fun Context.createLocationRequest(LOCATION_REQ_TIME: Long,
                                  minimumDistance: Float = 0f,
                                  provider: String = LocationManager.GPS_PROVIDER,
                                  locationListener: LocationListener,
                                  additionalStuff: LocationManager.() -> Unit = {}): LocationManager? {
    val lm = this.locationManager ?: return null
    lm.requestLocationUpdates(
            provider,
            LOCATION_REQ_TIME,
            minimumDistance,
            locationListener
    )
    lm.additionalStuff()
    return lm
}


fun locationListenerDSL(
        statusChanged: (provider: String?, status: Int, extras: Bundle?) -> Unit = { _, _, _ -> },
        providerEnabled: (provider: String?) -> Unit = { _ -> },
        providerDisabled: (provider: String?) -> Unit = { _ -> },
        locationChanged: (location: Location?) -> Unit = { _ -> }
): LocationListener {
    return object : LocationListener {
        override fun onLocationChanged(location: Location) {
            locationChanged(location)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            statusChanged(provider, status, extras)
        }

        override fun onProviderEnabled(provider: String) {
            providerEnabled(provider)
        }

        override fun onProviderDisabled(provider: String) {
            providerDisabled(provider)
        }
    }
}