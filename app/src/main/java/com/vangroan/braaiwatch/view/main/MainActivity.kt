package com.vangroan.braaiwatch.view.main

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.vangroan.braaiwatch.R
import com.vangroan.braaiwatch.model.Timer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var timerText : String
    private val timer: Timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerText = getString(R.string.activity_main_timer)
        updateTimer()

        timer.setOnTimerListener(object : Timer.OnTimerListener{
            override fun onTimer() {
                updateTimer()
            }
        })

        activity_main_button.setOnClickListener {
            if (timer.isRunning()) {
                timer.stop()
                activity_main_button.text = getText(R.string.activity_main_button_start)
            } else {
                timer.start()
                activity_main_button.text = getText(R.string.activity_main_button_stop)
            }
        }
    }

    private fun updateTimer() {
        activity_main_timer.text = String.format(timerText, timer.hours, timer.minutes, timer.seconds)
    }
}
