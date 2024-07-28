package com.sample.ktln04

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var stopwatch: Chronometer
    private var running = false
    private var offset: Long = 0

    private val OFFSET_KEY = "offset"
    private val RUNNING_KEY = "running"
    private val BASE_KEY = "base"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        stopwatch = findViewById<Chronometer>(R.id.stopwatch)
        if(savedInstanceState != null){
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)

            if(running){
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            }
            else setBaseTime()
        }

        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnPause = findViewById<Button>(R.id.btnPause)
        val btnReset = findViewById<Button>(R.id.btnReset)
        val tvOffset = findViewById<TextView>(R.id.tvOffset)

        btnStart.setOnClickListener {
            if(!running) {
                setBaseTime()
                stopwatch.start()
                running = true
                tvOffset.text = stopwatch.base.toString()
            }
        }

        btnPause.setOnClickListener {
            if(running) {
                saveOffset()
                stopwatch.stop()
                running = false
                tvOffset.text = stopwatch.base.toString()
            }
        }

        btnReset.setOnClickListener {
            offset = 0
            setBaseTime()
            tvOffset.text = stopwatch.base.toString()
        }
    }

    override fun onRestart() {
        super.onRestart()
        try {
            if (running) {
                setBaseTime()
                stopwatch.start()
                offset = 0
            }
        }
        catch(e: Exception){
            Log.i("tag_log", e.message.toString())
        }
    }

    override fun onStop() {
        super.onStop()
        try{
            if(running){
                saveOffset()
                stopwatch.stop()
            }
        }
        catch(e: Exception){
            Log.i("tag_log", e.message.toString())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            putLong(OFFSET_KEY, offset)
            putBoolean(RUNNING_KEY, running)
            putLong(BASE_KEY, stopwatch.base)
        }

        super.onSaveInstanceState(outState)
    }

    private fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }
}