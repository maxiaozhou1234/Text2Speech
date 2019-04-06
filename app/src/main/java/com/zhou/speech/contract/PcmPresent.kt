package com.zhou.speech.contract

import com.zhou.speech.speak.PcmUtil

/**
 *  @author Administrator_Zhou
 *  created on 2019/4/5
 */
class PcmPresent : PcmContract.Present {
    override fun play(file: String) {
        PcmUtil.instance.play(file)
    }

    override fun pause() {
        PcmUtil.instance.pause()
    }

    override fun stop() {
        PcmUtil.instance.stop()
    }
}