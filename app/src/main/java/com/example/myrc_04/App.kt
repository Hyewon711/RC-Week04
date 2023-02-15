package com.example.myrc_04

import android.app.Application

class App : Application() {
    // context를 전역으로 받아오기 위한 클래스
    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}