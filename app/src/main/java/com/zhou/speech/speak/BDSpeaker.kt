package com.zhou.speech.speak

import android.content.Context
import android.os.Environment
import android.util.Log
import com.baidu.tts.client.*
import com.zhou.speech.common.FileUtil
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class BDSpeaker : Speaker() {

    companion object {
        fun get(): BDSpeaker {
            return Holder.instance
        }
    }

    object Holder {
        val instance = BDSpeaker()
    }

    private val defaultRegex = arrayOf("\n", "[.|。]", "[,|，]")

    private var state = false
    private lateinit var synthesizer: SpeechSynthesizer
    private val TAG = "speaker"
    private val ttsDir = "baiduTTS"
    private val appId = "15323941"
    private val appKey = "XhiVGzmGyE0aoNi6PBGY85y3"
    private val secretKey = "OQr40A7Wa2nBEKzOWGPUBYdaWflH4Hk0  "
    private var ttsMode = TtsMode.MIX
    private var downloadCallback: DownloadCallback? = null
    private var downCount = 0
    private var downSize = 0

    private val storagePath = Environment.getExternalStorageDirectory().path + "/" + ttsDir + "/"
    private var speechSpeaker = "1"
    private val model = arrayOf(
            "bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat",
            "bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat",
            "bd_etts_text.dat",
            "bd_etts_common_speech_yyjw_mand_eng_high_am-mix_v3.0.0_20170512.dat",
            "bd_etts_common_speech_as_mand_eng_high_am_v3.0.0_20170516.dat")

    private val format = SimpleDateFormat("_yyyy_MM_dd_HH:mm:ss", Locale.getDefault())

    override fun init(context: Context) {

        if (state) {
            return
        }
        state = false
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
//                val map = hashMapOf(SpeechSynthesizer.PARAM_SPEAKER to "1", SpeechSynthesizer.PARAM_VOLUME to "5",
//                        SpeechSynthesizer.PARAM_SPEED to "7",
//                        SpeechSynthesizer.PARAM_PITCH to "5",
//                        SpeechSynthesizer.PARAM_MIX_MODE to SpeechSynthesizer.MIX_MODE_DEFAULT,
//                        SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE to storagePath + "bd_etts_text.dat",
//                        SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE to storagePath + model[1])
                val map = getConfig(context)
                map.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT)
                map.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, storagePath + "bd_etts_text.dat")
//                map.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, storagePath + model[1])
                for ((k, v) in map) {
                    setParam(k, v)
                }
                map[KEY_SPEAKER]?.also {
                    setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, storagePath + model[it.toInt()])
                }

                val result = this.initTts(ttsMode)
                if (result != 0) {
                    Log.i(TAG, "init tts failed. error = $result")
                } else {
                    Log.i(TAG, "init tts success.")
                }
            }
        }.start()
    }

    override fun play(text: String) {//字数限制每个文本text不超过1024的GBK字节，即512个汉字或英文字母数字
        val r: Int = if (text.length > MAX_SINGLE_LENGTH) synthesizer.batchSpeak(split(text))
        else synthesizer.speak(text)

        if (r != 0) {
            Log.i(TAG, "speak failed")
        }
    }

    override fun play(text: String, map: Map<String, String>) {
        val speaker = map[SpeechSynthesizer.PARAM_SPEAKER]
        speaker?.also {
            if (speechSpeaker !== it) {
                Log.d(TAG, "修改发音人$speaker")
                //更改发音人，在线要mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0")
                //离线需要更改语音模型
                synthesizer.loadModel(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, storagePath + model[it.toInt()])
                speechSpeaker = it
            }
        }

        for ((k, v) in map) {
            synthesizer.setParam(k, v)
        }
        play(text)
    }

    override fun pause() {
        synthesizer.pause()
    }

    override fun stop() {
        synthesizer.stop()
    }

    override fun release() {
        synthesizer.release()
    }

    override fun download(text: String, callback: DownloadCallback?) {
        Log.d(TAG, "开始合成")
        val f = if (text.length > 5) text.subSequence(0, 5) else text
        val fileName = "${FileUtil.downloadPath}$f${format.format(Date())}.pcm"
        PcmUtil.instance.createFile(fileName)

        downloadCallback = callback

        if (text.length > MAX_SINGLE_LENGTH) {
            val list = split(text)
            downSize = list.size
            downCount = 0
            list.listIterator().forEach { bag -> synthesizer.synthesize(bag) }
        } else {
            synthesizer.synthesize(text)
        }
    }

    override fun speakType(): String {
        return BaiduSpeak
    }

    override fun defaultConfig(): HashMap<String, String> {
//        return hashMapOf(SpeechSynthesizer.PARAM_SPEAKER to "1",
//                SpeechSynthesizer.PARAM_VOLUME to "5",
//                SpeechSynthesizer.PARAM_SPEED to "7",
//                SpeechSynthesizer.PARAM_PITCH to "5")
        return hashMapOf(KEY_SPEAKER to "1",
                KEY_VOLUME to "5",
                KEY_SPEED to "7",
                KEY_TONE to "5")
    }

//    fun synthesize(text: String) {
//        Log.d(TAG, "开始合成")
//        PcmUtil.instance.createFile(text, handler)
//        synthesizer.synthesize(text)
//    }
//
//    fun synthesize(text: String, map: Map<String, String>) {
//
//        val speaker = map[SpeechSynthesizer.PARAM_SPEAKER]
//        speaker?.also {
//            if (speechSpeaker !== it) {
//                Log.d(TAG, "修改发音人$speaker")
//                synthesizer.loadModel(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, storagePath + model[it.toInt()])
//                speechSpeaker = it
//            }
//        }
//
//        for ((k, v) in map) {
//            synthesizer.setParam(k, v)
//        }
//        synthesize(text)
//    }

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
            if (downCount == 0) {
                downloadCallback?.onStart()
            }
        }

        override fun onSpeechFinish(p0: String?) {
        }

        override fun onSpeechProgressChanged(p0: String?, p1: Int) {
        }

        override fun onSynthesizeFinish(p0: String?) {
            if (downSize > 0 && downCount < downSize - 1) {
                downCount++
            } else {
                PcmUtil.instance.done()
                downloadCallback?.onSuccess()
                downloadCallback = null
                downSize = 0
                downCount = 0
            }
        }

        override fun onSpeechStart(p0: String?) {
        }

        override fun onSynthesizeDataArrived(p0: String?, p1: ByteArray?, p2: Int) {
            PcmUtil.instance.write(p1)
        }

        override fun onError(p0: String?, p1: SpeechError?) {
        }
    }

    private fun split(text: String, count: Int = 0): List<SpeechSynthesizeBag> {
        if (count >= defaultRegex.size) {
            return splitByLength(text)
        }
        val data = arrayListOf<SpeechSynthesizeBag>()
        val regex = defaultRegex[count]
        val m = count + 1
        val array = text.split(regex)
        for (item in array) {
            if (item.isEmpty())
                continue
            if (item.length > MAX_SINGLE_LENGTH) {
                data.addAll(split(item, m))
            } else {
                data.add(createSynthesizeBag(item))
            }
        }
        Log.d(TAG, "length = " + text.length + ",after split size = " + data.size)
        return data
    }

    private fun splitByLength(text: String): List<SpeechSynthesizeBag> {
        val data = arrayListOf<SpeechSynthesizeBag>()
        val count = text.length / MAX_SINGLE_LENGTH
        val last = text.length % MAX_SINGLE_LENGTH

        var i = 0
        while (i < count) {
            data.add(createSynthesizeBag(text.substring(i * MAX_SINGLE_LENGTH, (1 + i) * MAX_SINGLE_LENGTH)))
            i++
        }
        if (last != 0) {
            data.add(createSynthesizeBag(text.substring(i * MAX_SINGLE_LENGTH)))
        }

        return data
    }

    private fun createSynthesizeBag(text: String): SpeechSynthesizeBag {
        val bag = SpeechSynthesizeBag()
        bag.text = text
        return bag
    }
}