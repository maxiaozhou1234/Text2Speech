package com.zhou.speech

import android.app.Application
import com.zhou.speech.db.FileDB
import com.zhou.speech.speak.BDSpeaker

/**
 *  @author Administrator_Zhou
 *  created on 2019/4/4
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread { BDSpeaker.get().init(this) }.start()
        FileDB.get().init(this)
    }
}