package com.zhou.speech.speak

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.AudioTrack.PLAYSTATE_PAUSED
import android.util.Log
import com.zhou.speech.common.FileUtil.downloadPath
import io.reactivex.Observable
import java.io.*

class PcmUtil private constructor() {

    private var bos: BufferedOutputStream? = null

    companion object {
        val instance = INSTANCE.instance
    }

    object INSTANCE {
        val instance = PcmUtil()
    }


    fun createFile(fileName: String) {

        val file = File(fileName)
        if (!file.exists()) {
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            file.createNewFile()
        }
        bos = BufferedOutputStream(FileOutputStream(file))
    }

    fun write(p1: ByteArray?) {
        bos?.write(p1)
    }

    fun done() {

        bos?.close()
        if (bos != null) {
            bos = null
            Log.d("speaker", "下载完成 bos = $bos")
        }
    }

    private var audioTrack: AudioTrack? = null
    fun play(fileName: String) {

        if (audioTrack == null) {
            val bufferSize = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                val audioFormat = AudioFormat.Builder().setSampleRate(8000).build()
                audioTrack = AudioTrack(AudioAttributes.Builder().build(), audioFormat, bufferSize, AudioTrack.MODE_STREAM, AudioManager.AUDIO_SESSION_ID_GENERATE)
            } else {
                audioTrack = AudioTrack(AudioManager.STREAM_MUSIC, 8000,
                        AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM)
            }
        } else {
            audioTrack?.pause()
            audioTrack?.flush()
        }

        audioTrack?.apply {
            try {

                play()
                Thread {
                    val fis = FileInputStream(File("$downloadPath$fileName"))
                    val buff = ByteArray(1024)
                    while (playState != PLAYSTATE_PAUSED && fis.read(buff) != -1) {
                        write(buff, 0, buff.size)
                    }
                    fis.close()
                }.start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun pause() {
        audioTrack?.pause()
    }

    fun stop() {
        audioTrack?.stop()
        audioTrack?.flush()
    }
}