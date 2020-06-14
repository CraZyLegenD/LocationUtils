package com.crazylegend.locationhelpers

import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng


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
