package com.zhou.speech.speak

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Handler
import android.util.Log
import com.zhou.speech.common.FileUtil.downloadPath
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class PcmUtil private constructor() {

    private var fileName: String? = null

    companion object {
        val instance = INSTANCE.instance
    }

    object INSTANCE {
        val instance = PcmUtil()
    }

    private val format = SimpleDateFormat("_yyyy_MM_dd_HH:mm:ss", Locale.getDefault())

    private var bos: BufferedOutputStream? = null
    private var handler: Handler? = null

    fun createFile(fileName: String, handler: Handler) {
        this@PcmUtil.handler = handler
        var f = fileName
        if (fileName.length > 5) {
            f = fileName.substring(0, 5)
        }
        val file = File("$downloadPath$f-${format.format(Date())}.pcm")
        if (!file.exists()) {
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            file.createNewFile()
        }
        bos = BufferedOutputStream(FileOutputStream(file))
        this@PcmUtil.fileName = file.name
    }

    fun write(p1: ByteArray?) {
        bos?.write(p1)
    }

    fun done() {
        bos?.close()
        if (bos != null) {
            bos = null
            Log.d("speaker", "下载完成 bos = $bos")
            handler?.apply {
                val msg = obtainMessage()
                msg.what = 0
                msg.obj = fileName
                msg.sendToTarget()
            }
            fileName = null
            handler = null
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
                val fis = FileInputStream(File("$downloadPath$fileName"))
                val buff = ByteArray(1024)
                while (fis.read(buff) != -1) {
                    write(buff, 0, buff.size)
                }
                fis.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}