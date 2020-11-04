package com.crazylegend.locationhelpers

import android.content.Intent
import android.net.Uri
import com.google.android.gms.maps.model.LatLng

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */

/**
 * There's limit of 20 stops by the Google intent
 * @param latNLongs List<LatLng>
 * @return Intent
 */
fun multiStopIntent(latNLongs: List<LatLng>): Intent {
    val intentList = latNLongs.mapIndexed { index, it ->
        "${it.latitude}:${it.longitude}" + if (index == latNLongs.lastIndex) "" else "/"
    }.toString().removePrefix("[").removeSuffix("]")
            .replace(" ", "")
            .replace(",", "")
            .replace(":", ",")
    val url = "https://www.google.com/maps/dir/$intentList"

    val intent = Intent(Intent.ACTION_VIEW,
            Uri.parse(url))
    intent.setPackage("com.google.android.apps.maps")
    return intent
}
