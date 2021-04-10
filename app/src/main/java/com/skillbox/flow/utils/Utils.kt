package com.skillbox.flow.utils

import android.util.Log
import com.skillbox.flow.BuildConfig

fun loggingDebug(string: String) {
    if (BuildConfig.DEBUG)
        Log.d("loggingDebug", string)
}
