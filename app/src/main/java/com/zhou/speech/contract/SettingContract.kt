package com.zhou.speech.contract

import android.content.Context
import com.zhou.speech.BasePresent
import com.zhou.speech.BaseView

/**
 *  @author Administrator_Zhou
 *  created on 2019/4/5
 */
class SettingContract {

    interface View : BaseView<Present> {
        fun initView(speaker: Int, speed: Int, tone: Int, volume: Int)
    }

    interface Present : BasePresent {
        fun create()
        fun play(text: String)
        fun update(key: String, value: String)
        fun onDestroy(context: Context)
    }
}