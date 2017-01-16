package com.vangroan.braaiwatch.main.domain.model

import android.os.Handler
import android.os.Looper

/**
 * Created by Victor on 2016/12/22.
 */
class Timer {

    var counterMilestone = COUNTER_MILESTONE
        get
        set(value) {
            field = value
            // Reset the counter using the new milestone, otherwise the next interval milestone will
            // be far into the future.
            counter = (current / counterMilestone) + 1
        }

    private var startTime: Long = 0
    private var current: Long = 0

    var seconds: Long = 0
        private set

    var minutes: Long = 0
        private set

    var hours: Long = 0
        private set

    // Counter starts at 1 so that notification doesn't fire on time 0
    var counter: Long = 1L
        private set

    private var mode = TimerMode.STOPPED
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runner: Runnable
    private var onTimerListener: OnTimerListener? = null
    private var onCounterListener: OnCounterListener? = null

    init {
        runner = Runnable {
            current = System.currentTimeMillis() - startTime

            seconds = (current / 1000) % 60
            minutes = ((current / 1000) / 60) % 60
            hours = (((current / 1000) / 60) / 60)

            onTimerListener?.onTimer()

            if (current / counterMilestone >= counter) {
                onCounterListener?.onCounter(counter)
                // Fire at next milestone
                counter = (current / counterMilestone) + 1
            }

            if (isRunning())
                handler.postDelayed(runner, INTERVAL)
        }
    }

    fun start() {
        startTime = System.currentTimeMillis()
        current = 0
        seconds = 0
        minutes = 0
        hours = 0
        counter = 1L
        mode = TimerMode.RUNNING

        handler.postDelayed(runner, INTERVAL)
    }

    fun stop() {
        mode = TimerMode.STOPPED
    }

    fun isRunning(): Boolean {
        return mode == TimerMode.RUNNING
    }

    fun setOnTimerListener(listener: OnTimerListener?) {
        onTimerListener = listener
    }

    fun setOnCounterListener(listener: OnCounterListener?) {
        onCounterListener = listener
    }

    interface OnTimerListener {
        fun onTimer()
    }

    interface OnCounterListener {
        fun onCounter(counter: Long)
    }

    companion object {
        @JvmStatic val INTERVAL = 1000L
        @JvmStatic val COUNTER_MILESTONE = 5000L
    }
}