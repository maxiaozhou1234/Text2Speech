package com.zhou.speech.contract

import com.zhou.speech.BasePresent
import com.zhou.speech.BaseView

/**
 *  @author Administrator_Zhou
 *  created on 2019/4/5
 */
class PcmContract {

    interface View:BaseView<Present>{

    }

    interface Present:BasePresent{
        fun play(file:String)
        fun pause()
        fun stop()
    }
}