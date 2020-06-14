package com.crazylegend.locationhelpers

import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil


/**
 * Created by crazy on 4/28/20 to long live and prosper !
 */

val Location.toLatNLong get() = LatLng(latitude, longitude)

val Pair<Double, Double>.toLocation
    get() = Location("").apply {
        latitude = first
        longitude = second
    }

val Pair<Double, Double>.toLatNLong
    get() = LatLng(first, second)


fun GoogleMap.changeOffsetCenter(latLng: LatLng, yOffset: Int = 120) {
    val mapPoint = projection.toScreenLocation(latLng)
    mapPoint[mapPoint.x] = mapPoint.y - yOffset
    animateCamera(CameraUpdateFactory.newLatLng(projection.fromScreenLocation(mapPoint)))
}

fun LatLng.changeMarkerOffsetCenter(googleMap: GoogleMap, yOffset: Int = 120) {
    val mapPoint = googleMap.projection.toScreenLocation(this)
    mapPoint[mapPoint.x] = mapPoint.y - yOffset
    googleMap.animateCamera(CameraUpdateFactory.newLatLng(googleMap.projection.fromScreenLocation(mapPoint)))
}


fun metersToKMH(speed: Double) = speed * 3600 / 1000
fun metersToKMH(speed: Float) = speed * 3600 / 1000


fun metersToKM(meters: Double) = meters / 1000
fun kilometersToMeters(km: Double) = km * 1000

fun metersToKM(meters: Float) = meters / 1000
fun kilometersToMeters(km: Float) = km * 1000


/**
 * Calculates the speed in m/s
 * @param oldLocation Location
 * @param newLocation Location
 * @param tolerance tolerance in meters
 * @return Double
 */
fun getSpeed(oldLocation: Location, newLocation: Location, tolerance: Int = 80): Double {
    val realDistance = getRealDistance(oldLocation, newLocation, tolerance)

    return if (oldLocation.hasSpeed() && newLocation.hasSpeed()) {
        val locationSpeed = listOf(oldLocation.speed, newLocation.speed)
        locationSpeed.max()?.toDouble() ?: 0.toDouble()
    } else {
        return if (realDistance != 0.toDouble()) {

            val timeOld = oldLocation.time / 1000
            val timeNew = newLocation.time / 1000

            realDistance / (timeNew - timeOld).toDouble()

        } else {
            0.toDouble()
        }
    }

}

/**
 * Calculates distance to another location in km
 * @receiver Location
 * @param anotherLocation Location
 * @return Float
 */
fun Location.distanceToInKM(anotherLocation: Location) = metersToKM(this.distanceTo(anotherLocation))

/**
 * returns the distance in meters
 * @param location1 Location
 * @param location2 Location
 * @param tolerance tolerance in meters
 * @return Double
 */
fun getRealDistance(location1: Location, location2: Location, tolerance: Int = 80): Double {
    val latLng1 = LatLng(location1.latitude, location1.longitude)
    val latLng2 = LatLng(location2.latitude, location2.longitude)

    return with(SphericalUtil.computeDistanceBetween(latLng1, latLng2)) {
        if (this >= tolerance) {
            0.toDouble()
        } else {
            this
        }
    }
}