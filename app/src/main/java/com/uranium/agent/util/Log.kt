package com.uranium.agent.util

import android.util.Log

object Logger {
    private const val TAG = "UraniumAgent"
    fun d(msg: String) = Log.d(TAG, msg)
    fun e(msg: String) = Log.e(TAG, msg)
}
