package com.vangroan.braaiwatch.view.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.vangroan.braaiwatch.R
import com.vangroan.braaiwatch.helper.sound.NotificationPlayer
import com.vangroan.braaiwatch.model.Timer
import com.vangroan.braaiwatch.view.main.adapters.IntervalAdapter
import com.vangroan.braaiwatch.view.main.enums.IntervalOption
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var timerText: String
    private val timer: Timer = Timer()
    private lateinit var audioNotifier: NotificationPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerText = getString(R.string.activity_main_timer)
        updateTimer()

        timer.setOnTimerListener(object : Timer.OnTimerListener {
            override fun onTimer() {
                updateTimer()
            }
        })

        activity_main_button.setOnClickListener {
            if (timer.isRunning())
                stopTimer()
            else
                startTimer()
        }

        // Interval Spinner
        val adapter = IntervalAdapter(this, android.R.layout.simple_spinner_item)
        activity_main_spinner.adapter = adapter

        activity_main_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateInterval(adapter.getItem(position).interval)
            }
        }

        updateInterval((activity_main_spinner.selectedItem as IntervalOption?)?.interval ?: IntervalOption.SECONDS_5.interval)
    }

    override fun onStart() {
        super.onStart()
        audioNotifier = NotificationPlayer(this)

        timer.setOnCounterListener(object : Timer.OnCounterListener{
            override fun onCounter(counter: Long) {
                Log.d(TAG, counter.toString())
                audioNotifier.play()
            }
        })
    }

    override fun onStop() {
        super.onStop()

        timer.setOnCounterListener(null)

        // Ensure native resources are released while activity is inactive
        audioNotifier.destroy()
    }

    private fun startTimer() {
        timer.start()
        activity_main_button.text = getText(R.string.activity_main_button_stop)
    }

    private fun stopTimer() {
        timer.stop()
        activity_main_button.text = getText(R.string.activity_main_button_start)
    }

    private fun updateTimer() {
        activity_main_timer.text = String.format(timerText, timer.hours, timer.minutes, timer.seconds)
    }

    private fun updateInterval(interval: Long) {
        timer.counterMilestone = interval
    }

    companion object {
        @JvmStatic val TAG = MainActivity::class.simpleName
    }
}
