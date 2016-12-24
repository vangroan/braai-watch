package com.vangroan.braaiwatch.view.main.enums

import android.content.Context
import com.vangroan.braaiwatch.R

/**
 * Created by Victor on 2016/12/24.
 */
enum class IntervalOption(val interval: Long, val titleRes: Int) {
    SECONDS_5(5000L, R.string.activity_main_interval_option_5_seconds),
    SECONDS_15(15000L, R.string.activity_main_interval_option_15_seconds),
    SECONDS_30(30000L, R.string.activity_main_interval_option_30_seconds),
    SECONDS_60(60000L, R.string.activity_main_interval_option_60_seconds);

    fun getTitleString(context: Context): String {
        return context.getString(titleRes)
    }
}