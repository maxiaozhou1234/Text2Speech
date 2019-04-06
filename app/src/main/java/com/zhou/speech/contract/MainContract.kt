package com.zhou.speech.contract

import com.zhou.speech.BasePresent
import com.zhou.speech.BaseView

/**
 *  @author Administrator_Zhou
 *  created on 2019/4/5
 */
class MainContract {
    interface View : BaseView<Present> {

        fun startDownload()
        fun finishDownload()
        fun setEditText(text: String)
        fun setEditFile(id:Int)
    }

    interface Present : BasePresent {
        fun loadData(id: Int = -1)
        fun clearCache()
        fun play(text: String)
        fun play(text: String, map: Map<String, String>)
        fun pause()
        fun stop()
        fun destroy()
        fun download(text:String)
    }
}