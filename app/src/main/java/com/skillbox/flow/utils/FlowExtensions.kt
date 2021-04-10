package com.skillbox.flow.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.RadioGroup
import com.skillbox.flow.R
import com.skillbox.flow.data.MovieType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
fun EditText.textChangedFlow(): Flow<String> {
    return callbackFlow {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sendBlocking(s?.toString().orEmpty())
            }

            override fun afterTextChanged(s: Editable?) = Unit
        }
        this@textChangedFlow.addTextChangedListener(textWatcher)
        awaitClose {
            this@textChangedFlow.removeTextChangedListener(textWatcher)
            loggingDebug("removing text listener")
        }
    }
}

@ExperimentalCoroutinesApi
fun RadioGroup.buttonChangeFlow(): Flow<MovieType> {
    return callbackFlow {
        this@buttonChangeFlow.setOnCheckedChangeListener { _, checkedId ->
            val movieType = when (checkedId) {
                R.id.radioMovie -> MovieType.MOVIE
                R.id.radioSeries -> MovieType.SERIES
                R.id.radioEpisode -> MovieType.EPISODE
                else -> error("Unknown movie type")
            }
            sendBlocking(movieType)
        }
        awaitClose {
            this@buttonChangeFlow.setOnCheckedChangeListener(null)
        }
    }
}
