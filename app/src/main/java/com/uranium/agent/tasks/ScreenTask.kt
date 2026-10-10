package com.uranium.agent.tasks

import org.json.JSONObject

object ScreenTask {
    fun execute(): JSONObject {
        return JSONObject().put("status", "Screen task stub")
    }
}
