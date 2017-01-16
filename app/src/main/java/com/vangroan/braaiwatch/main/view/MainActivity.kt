package com.vangroan.braaiwatch.main.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import com.vangroan.braaiwatch.R
import com.vangroan.braaiwatch.main.helper.sound.NotificationPlayer
import com.vangroan.braaiwatch.main.domain.model.Timer
import com.vangroan.braaiwatch.main.view.adapters.IntervalAdapter
import com.vangroan.braaiwatch.main.view.enums.IntervalOption
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var timerText: String
    private val timer: Timer = Timer()
    private lateinit var audioNotifier: NotificationPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

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

        timer.setOnCounterListener(object : Timer.OnCounterListener {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.activity_main_menu_share -> {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, "http://www.google.com ")
                startActivity(Intent.createChooser(intent, getText(R.string.activity_main_chooser_share_title)))
            }
        }
        return super.onOptionsItemSelected(item)
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
