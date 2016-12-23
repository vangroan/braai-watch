package com.vangroan.braaiwatch.model

import android.os.Handler
import android.os.Looper

/**
 * Created by Victor on 2016/12/22.
 */
class Timer {

    private var startTime: Long = 0
    private var current: Long = 0

    var seconds: Long = 0
        private set

    var minutes: Long = 0
        private set

    var hours: Long = 0
        private set

    var counter: Long = 0
        private set

    private var mode = TimerMode.STOPPED
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runner: Runnable
    private var onTimerListener : OnTimerListener? = null
    private var onCounterListener : OnCounterListener? = null

    init {
        runner = Runnable {
            current = System.currentTimeMillis() - startTime

            seconds = (current / 1000) % 60
            minutes = ((current / 1000) / 60) % 60
            hours = (((current / 1000) / 60) / 60)

            onTimerListener?.onTimer()

            if (current / COUNTER_MILESTONE >= counter) {
                onCounterListener?.onCounter(counter)
                // Fire at next milestone
                counter = (current / COUNTER_MILESTONE) + 1
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
        counter = 0
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