package com.crazylegend.locationhelpers

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer


/**
 * Created by crazy on 4/28/20 to long live and prosper !
 */


abstract class IconVectorClusterRenderer<T : ClusterItem>(
        private val context: Context,
        map: GoogleMap,
        clusterManager: ClusterManager<T>) : DefaultClusterRenderer<T>(context, map, clusterManager) {

    abstract val drawableID: Int

    override fun onBeforeClusterItemRendered(item: T, markerOptions: MarkerOptions) {
        val icon = BitmapDescriptorFactory.fromBitmap(context.getBitmapFromResource(drawableID))
        markerOptions.icon(icon)
    }

}