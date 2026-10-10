package com.uranium.agent.tasks

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import org.json.JSONObject

object GpsTask {
    fun execute(context: Context): JSONObject {
        val result = JSONObject()
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (loc != null) {
                result.put("latitude", loc.latitude)
                result.put("longitude", loc.longitude)
                result.put("altitude", loc.altitude)
            } else {
                result.put("error", "Location unavailable")
            }
        } else {
            result.put("error", "Permission denied")
        }
        return result
    }
}
