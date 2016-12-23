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

    private var mode = TimerMode.STOPPED
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runner: Runnable
    private var onTimerListener : OnTimerListener? = null

    init {
        runner = Runnable {
            current = System.currentTimeMillis() - startTime

            seconds = (current / 1000) % 60
            minutes = ((current / 1000) / 60) % 60
            hours = (((current / 1000) / 60) / 60)

            onTimerListener?.onTimer()

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
        mode = TimerMode.RUNNING

        handler.postDelayed(runner, INTERVAL)
    }

    fun stop() {
        mode = TimerMode.STOPPED
    }

    fun isRunning(): Boolean {
        return mode == TimerMode.RUNNING
    }

    fun setOnTimerListener(listener: OnTimerListener) {
        onTimerListener = listener
    }

    interface OnTimerListener {
        fun onTimer()
    }

    companion object {
        @JvmStatic val INTERVAL = 1000L
    }
}