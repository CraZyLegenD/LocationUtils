package com.crazylegend.locationhelpers

import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.collections.MarkerManager


/**
 * Created by crazy on 6/14/20 to long live and prosper !
 */

inline fun <CLUSTER : ClusterItem> clusterSetupWithInfoWindowClickListener(
        operatorsCluster: ClusterManager<CLUSTER>?,
        googleMap: GoogleMap?,
        markerManager: MarkerManager?,
        clusterList: List<CLUSTER>,
        infoWindowAdapter: GoogleMap.InfoWindowAdapter,
        iconVectorClusterRenderer: IconVectorClusterRenderer<CLUSTER>? = null,
        crossinline onClusterInfoWindowClickListener: CLUSTER.() -> Unit = {}) {
    operatorsCluster?.clearItems()

    operatorsCluster?.addItems(clusterList)
    if (googleMap != null && operatorsCluster != null) {
        iconVectorClusterRenderer?.apply {
            operatorsCluster.renderer = this
        }
    }
    operatorsCluster?.setOnClusterItemInfoWindowClickListener { clusterItem ->
        clusterItem.onClusterInfoWindowClickListener()
    }

    googleMap?.setOnInfoWindowClickListener(operatorsCluster)
    googleMap?.setInfoWindowAdapter(operatorsCluster?.markerManager)
    googleMap?.setOnCameraIdleListener(operatorsCluster)
    googleMap?.setOnMarkerClickListener(markerManager)
    operatorsCluster?.markerCollection?.setInfoWindowAdapter(infoWindowAdapter)
    operatorsCluster?.cluster()
}

inline fun <CLUSTER : ClusterItem> clusterSetup(
        operatorsCluster: ClusterManager<CLUSTER>?,
        googleMap: GoogleMap?,
        markerManager: MarkerManager?,
        clusterList: List<CLUSTER>,
        infoWindowAdapter: GoogleMap.InfoWindowAdapter,
        iconVectorClusterRenderer: IconVectorClusterRenderer<CLUSTER>? = null,
        crossinline onCluster: ClusterManager<CLUSTER>?.() -> Unit = {}) {
    operatorsCluster?.clearItems()

    operatorsCluster?.addItems(clusterList)
    if (googleMap != null && operatorsCluster != null) {
        iconVectorClusterRenderer?.apply {
            operatorsCluster.renderer = this
        }
    }
    operatorsCluster?.onCluster()

    googleMap?.setOnInfoWindowClickListener(operatorsCluster)
    googleMap?.setInfoWindowAdapter(operatorsCluster?.markerManager)
    googleMap?.setOnCameraIdleListener(operatorsCluster)
    googleMap?.setOnMarkerClickListener(markerManager)
    operatorsCluster?.markerCollection?.setInfoWindowAdapter(infoWindowAdapter)
    operatorsCluster?.cluster()
}