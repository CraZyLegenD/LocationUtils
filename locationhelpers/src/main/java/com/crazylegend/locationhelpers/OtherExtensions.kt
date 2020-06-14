package com.crazylegend.locationhelpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.data.geojson.GeoJsonFeature
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonLineString
import org.json.JSONObject


/**
 * Created by crazy on 4/28/20 to long live and prosper !
 */


fun Context.getBitmapFromResource(drawableRes: Int): Bitmap? {
    var bitmap: Bitmap? = null
    val drawable = getCompatDrawable(drawableRes)
    val canvas = Canvas()
    drawable?.apply {
        bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        draw(canvas)
    }

    return bitmap
}

fun Context.getCompatDrawable(@DrawableRes drawableRes: Int): Drawable? =
        ContextCompat.getDrawable(this, drawableRes)


fun buildGeoJSONLayer(map: GoogleMap, geoJson: String) = GeoJsonLayer(map, JSONObject(geoJson))

fun buildLayerFromGeoJSONAndAddToMap(map: GoogleMap, geoJson: String) {
    val layer = buildGeoJSONLayer(map, geoJson)
    layer.addLayerToMap()
}

fun buildLayerFromGeoJSONAndAddToMapWithBoundingBox(map: GoogleMap, geoJson: String, onLatLngBounds: LatLngBounds.() -> Unit = {}) {
    val layer = buildGeoJSONLayer(map, geoJson)
    layer.addLayerToMap()
    buildBoundingBox(layer.features, onLatLngBounds)
}

inline fun List<LatLng>.toPolyLines(polyLineBuilder: PolylineOptions.() -> Unit = {}) = PolylineOptions()
        .addAll(this).polyLineBuilder()

inline fun buildBoundingBox(features: Iterable<GeoJsonFeature>, onLatLngBounds: LatLngBounds.() -> Unit = {}) {
    // Get the bounding box builder.
    val builder = LatLngBounds.builder()

    // Get the Coordinates
    for (feature in features) {
        if (feature.hasGeometry()) {
            val geometry = feature.geometry

            val lists = (geometry as GeoJsonLineString).coordinates
            // Feed the Coordinates to the builder.
            for (latLng in lists) {
                builder.include(latLng)
            }
        }
    }

    // Assign the builder's return value to a bounding box.
    val bb = builder.build()

    bb?.apply {
        this.onLatLngBounds()
    }


}