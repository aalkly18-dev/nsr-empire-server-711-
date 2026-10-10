package com.uranium.agent.tasks

import org.json.JSONObject

object CameraTask {
    fun execute(): JSONObject {
        return JSONObject().put("status", "Camera task stub")
    }
}
