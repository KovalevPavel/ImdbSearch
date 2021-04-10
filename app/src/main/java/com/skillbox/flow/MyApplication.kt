package com.skillbox.flow

import android.app.Application
import com.skillbox.flow.database.Database

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Database.init(this)
    }
}
