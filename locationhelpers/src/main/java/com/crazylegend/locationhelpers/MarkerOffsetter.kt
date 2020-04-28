package com.crazylegend.locationhelpers


/**
 * Created by crazy on 4/28/20 to long live and prosper !
 */

val markerLocation : HashMap<String, String> = HashMap()

fun coordinateForMarker(latitude: Float, longitude: Float,  COORDINATE_OFFSET:Float = 0.00002f): Array<String?> {
    val location = arrayOfNulls<String>(2)

    for (i in 0..Int.MAX_VALUE) {

        if (mapAlreadyHasMarkerForLocation((latitude + i * COORDINATE_OFFSET).toString()
                        + "," + (longitude + i * COORDINATE_OFFSET))) {

            // If i = 0 then below if condition is same as upper one. Hence, no need to execute below if condition.
            if (i == 0)
                continue

            if (mapAlreadyHasMarkerForLocation((latitude - i * COORDINATE_OFFSET).toString()
                            + "," + (longitude - i * COORDINATE_OFFSET))) {

                continue

            } else {
                location[0] = (latitude - i * COORDINATE_OFFSET).toString() + ""
                location[1] = (longitude - i * COORDINATE_OFFSET).toString() + ""
                break
            }

        } else {
            location[0] = (latitude + i * COORDINATE_OFFSET).toString() + ""
            location[1] = (longitude + i * COORDINATE_OFFSET).toString() + ""
            break
        }
    }

    return location
}

// Return whether marker with same location is already on map
private fun mapAlreadyHasMarkerForLocation(location: String): Boolean {
    return (markerLocation.containsValue(location))
}