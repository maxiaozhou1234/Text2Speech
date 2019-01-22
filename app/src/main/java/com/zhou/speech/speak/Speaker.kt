package com.zhou.speech.speak

import android.content.Context
import android.os.Environment
import android.util.Log
import com.baidu.tts.client.SpeechError
import com.baidu.tts.client.SpeechSynthesizer
import com.baidu.tts.client.SpeechSynthesizerListener
import com.baidu.tts.client.TtsMode
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

class Speaker private constructor() {

    companion object {
        fun get(): Speaker {
            return Handler.instance
        }
    }

    object Handler {
        val instance = Speaker()
    }

    private lateinit var synthesizer: SpeechSynthesizer
    private val TAG = "speaker"
    private val ttsDir = "baiduTTS"
    private val appId = "15323941"
    private val appKey = "XhiVGzmGyE0aoNi6PBGY85y3"
    private val secretKey = "OQr40A7Wa2nBEKzOWGPUBYdaWflH4Hk0  "
    private var ttsMode = TtsMode.MIX

    private val storagePath = Environment.getExternalStorageDirectory().path + "/" + ttsDir + "/"
    private var speechSpeaker = "1"
    private val model = arrayOf(
            "bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat",
            "bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat",
            "bd_etts_text.dat",
            "bd_etts_common_speech_yyjw_mand_eng_high_am-mix_v3.0.0_20170512.dat",
            "bd_etts_common_speech_as_mand_eng_high_am_v3.0.0_20170516.dat")

    lateinit var handler: android.os.Handler

    fun init(context: Context, handler: android.os.Handler) {

        Thread {
            for (f in model) {
                if (!checkFileExists(f)) {
                    copy(context, f)
                }
            }
            synthesizer = SpeechSynthesizer.getInstance()
            synthesizer.apply {
                setContext(context)
                setSpeechSynthesizerListener(speechListener)
                setAppId(appId)
                setApiKey(appKey, secretKey)
                val authInfo = this.auth(ttsMode)
                if (authInfo.isSuccess) {
                    Log.i(TAG, "check auth success.")
                } else {
                    Log.i(TAG, "check auth failed.")
                    return@apply
                }
                val map = hashMapOf(SpeechSynthesizer.PARAM_SPEAKER to "1", SpeechSynthesizer.PARAM_VOLUME to "5",
                        SpeechSynthesizer.PARAM_SPEED to "7",
                        SpeechSynthesizer.PARAM_PITCH to "5",
                        SpeechSynthesizer.PARAM_MIX_MODE to SpeechSynthesizer.MIX_MODE_DEFAULT,
                        SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE to storagePath + "bd_etts_text.dat",
                        SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE to storagePath + model[1])
                for ((k, v) in map) {
                    setParam(k, v)
                }

                val result = this.initTts(ttsMode)
                if (result != 0) {
                    Log.i(TAG, "init tts failed. error = $result")
                } else {
                    Log.i(TAG, "init tts success.")
                }
            }
        }.start()

        this@Speaker.handler = handler
    }

    fun speak(text: String) {
        val r = synthesizer.speak(text)
        if (r != 0) {
            Log.i(TAG, "speak failed")
        }
    }

    fun speak(text: String, map: Map<String, String>) {

        val speaker = map[SpeechSynthesizer.PARAM_SPEAKER]
        speaker?.also {
            if (speechSpeaker !== it) {
                Log.d(TAG, "修改发音人$speaker")
                synthesizer.loadModel(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, storagePath + model[it.toInt()])
                speechSpeaker = it
            }
        }

        for ((k, v) in map) {
            synthesizer.setParam(k, v)
        }
        speak(text)
    }

    fun synthesize(text: String) {
        Log.d(TAG, "开始合成")
        PcmUtil.instance.createFile(text, handler)
        synthesizer.synthesize(text)
    }

    fun synthesize(text: String, map: Map<String, String>) {

        val speaker = map[SpeechSynthesizer.PARAM_SPEAKER]
        speaker?.also {
            if (speechSpeaker !== it) {
                Log.d(TAG, "修改发音人$speaker")
                synthesizer.loadModel(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, storagePath + model[it.toInt()])
                speechSpeaker = it
            }
        }

        for ((k, v) in map) {
            synthesizer.setParam(k, v)
        }
        synthesize(text)
    }

    private fun checkFileExists(fileName: String): Boolean {
        val file = File(Environment.getExternalStorageDirectory().path + "/" + ttsDir + "/" + fileName)
        return file.exists() && file.canRead()
    }

    private fun copy(context: Context, fileName: String) {
        val path = storagePath + fileName
        val file = File(path)
        if (!file.exists()) {
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            file.createNewFile()
        }
        val bis = context.assets.open(fileName)
        val bos = BufferedOutputStream(FileOutputStream(file))
        val buffer = ByteArray(1024)
        while (bis.read(buffer) != -1) {
            bos.write(buffer)
        }
        bis.close()
        bos.close()
    }

    private val speechListener = object : SpeechSynthesizerListener {
        override fun onSynthesizeStart(p0: String?) {

        }

        override fun onSpeechFinish(p0: String?) {
        }

        override fun onSpeechProgressChanged(p0: String?, p1: Int) {
        }

        override fun onSynthesizeFinish(p0: String?) {
            PcmUtil.instance.done()
        }

        override fun onSpeechStart(p0: String?) {
        }

        override fun onSynthesizeDataArrived(p0: String?, p1: ByteArray?, p2: Int) {
            PcmUtil.instance.write(p1)
        }

        override fun onError(p0: String?, p1: SpeechError?) {
        }
    }


}