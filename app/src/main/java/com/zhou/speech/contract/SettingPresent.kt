package com.zhou.speech.contract

import android.content.Context
import com.zhou.speech.speak.*

/**
 *  @author Administrator_Zhou
 *  created on 2019/4/5
 */
class SettingPresent(context: Context, val view: SettingContract.View) : SettingContract.Present {

    private val map = BDSpeaker.get().getConfig(context)
    private var changeSpeaker = false

    init {
        var speaker = map[KEY_SPEAKER]?.run { this.toInt() } ?: 1
        val speed = map[KEY_SPEED]?.run { this.toInt() } ?: 7
        val tone = map[KEY_TONE]?.run { this.toInt() } ?: 5
        val volume = map[KEY_VOLUME]?.run { this.toInt() } ?: 5
        if (speaker > 2) {
            speaker -= 1
        }
        view.initView(speaker, speed, tone, volume)
    }

    override fun create() {
    }

    override fun play(text: String) {
        if (changeSpeaker) {
            BDSpeaker.get().play(text, map)
        } else {
            BDSpeaker.get().play(text)
        }

        changeSpeaker = false
    }

    override fun update(key: String, value: String) {
        map.put(key, value)
        changeSpeaker = true
    }

    override fun onDestroy(context: Context) {
        BDSpeaker.get().saveConfig(context, map)
    }
}