package com.pajasoft.vetcare

import android.app.Application

class VetCareApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
    }
}