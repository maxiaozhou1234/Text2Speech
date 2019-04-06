package com.zhou.speech.contract

import com.zhou.speech.db.FileDB
import com.zhou.speech.db.SimpleFile
import com.zhou.speech.speak.BDSpeaker
import com.zhou.speech.speak.DownloadCallback

/**
 *  @author Administrator_Zhou
 *  created on 2019/4/5
 */
class MainPresent(val view: MainContract.View) : MainContract.Present {

    private var latestFile: SimpleFile? = null

    override fun loadData(id: Int) {
        latestFile = when (id) {
            -1 -> FileDB.get().queryLatest()
            else -> FileDB.get().query(id)
        }

        latestFile?.also {
            view.setEditText(it.data)
        }
    }

    override fun play(text: String) {
        BDSpeaker.get().play(text)
    }

    override fun play(text: String, map: Map<String, String>) {
        BDSpeaker.get().play(text, map)
    }

    override fun pause() {
        BDSpeaker.get().pause()
    }

    override fun stop() {
        BDSpeaker.get().stop()
    }

    override fun destroy() {
        BDSpeaker.get().release()
        FileDB.get().closeDB()
    }

    override fun clearCache() {
        latestFile = null
    }

    override fun download(text: String) {

        latestFile?.also {
            FileDB.get().update(it.id, text)
        } ?: FileDB.get().insert(text)

        BDSpeaker.get().download(text, object : DownloadCallback {
            override fun onStart() {
                view.startDownload()
            }

            override fun onSuccess() {
                view.finishDownload()
            }
        })
    }
}