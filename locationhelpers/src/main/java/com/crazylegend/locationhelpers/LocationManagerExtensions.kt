package com.crazylegend.locationhelpers

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.core.location.LocationManagerCompat


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

/**
 * Checks if high accuracy is enabled
 * @receiver Context
 * @return Int -1 if isn't enabled, 3 if is enabled, other values aren't high accuracy
 */
fun Context.isHighAccuracyEnabled(): Int {
    var locationMode: Int = -1
    try {
        val locationManager = locationManager ?: return -1
        LocationManagerCompat.isLocationEnabled(locationManager)
        locationMode = Settings.Secure.getInt(contentResolver, Settings.Secure.LOCATION_MODE)
    } catch (e: Settings.SettingNotFoundException) {
        e.printStackTrace()
    }
    locationMode != Settings.Secure.LOCATION_MODE_OFF && locationMode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY //check location mode
    return locationMode
}

fun locationModeChangedIntent() = IntentFilter(LocationManager.MODE_CHANGED_ACTION)

inline fun listenForLocationModeChanges(crossinline onNewLocationStatus: (Intent) -> Unit) = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (LocationManager.MODE_CHANGED_ACTION == intent?.action) {
            onNewLocationStatus(intent)
        }
    }
}

inline fun Context.registerLocationModeReceiver(crossinline onNewLocationStatus: (Intent) -> Unit): BroadcastReceiver {
    val receiver = listenForLocationModeChanges(onNewLocationStatus)
    registerReceiver(receiver, locationModeChangedIntent())
    return receiver
}