package com.zhou.speech.speak

/**
 *  @author Administrator_Zhou
 *  created on 2019/3/24
 */
class SpeakUtil private constructor() {

    private lateinit var speaker: Speaker
    private var type: String? = null

    companion object {
        fun get(): SpeakUtil {
            return Holder.instance
        }
    }

    object Holder {
        val instance = SpeakUtil()
    }

//    fun load(context: Context, type: String) {
//        this@SpeakUtil.type = type
//        when (type) {
//            BaiduSpeak -> speaker = BDSpeaker(context)
////            XunFeiSpeak -> speaker = null
//        }
//    }



}