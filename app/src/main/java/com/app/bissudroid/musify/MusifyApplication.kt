package com.app.bissudroid.musify

import android.app.Application
import timber.log.Timber

class MusifyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())

        }
        else{
            Timber.uproot(Timber.DebugTree())
        }
    }
}