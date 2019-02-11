package com.zhou.speech

import android.os.Handler
import android.view.View
import android.widget.*
import com.baidu.tts.client.SpeechSynthesizer
import com.zhou.speech.common.BaseActivity
import com.zhou.speech.common.hideSoftInput
import com.zhou.speech.speak.Speaker

/**
 * 语音合成配置
 */
class SpeechSettingActivity : BaseActivity() {

    lateinit var radioGroup: RadioGroup
    lateinit var tvSpeed: TextView
    lateinit var tvTone: TextView
    lateinit var tvVolume: TextView
    lateinit var sbSpeed: SeekBar
    lateinit var sbTone: SeekBar
    lateinit var sbVolume: SeekBar
    lateinit var editText: EditText
    lateinit var btnPlay: Button
//    lateinit var btnDownload: Button
//    lateinit var recyclerView: RecyclerView

//    val data = arrayListOf<String>()
//    lateinit var adapter: FileAdapter

    private var isParamChange = false

    private val map = hashMapOf(SpeechSynthesizer.PARAM_SPEAKER to "1",
            SpeechSynthesizer.PARAM_VOLUME to "5",
            SpeechSynthesizer.PARAM_SPEED to "7",
            SpeechSynthesizer.PARAM_PITCH to "5")

    override fun setContentView() {
        setContentView(R.layout.activity_setting_speech)
    }

    override fun init() {
        supportActionBar?.title = "发音设置"

        radioGroup = findViewById(R.id.radioGroup)
        sbSpeed = findViewById(R.id.sbSpeed)
        sbTone = findViewById(R.id.sbTone)
        sbVolume = findViewById(R.id.sbVolume)

        tvSpeed = findViewById(R.id.tvSpeed)
        tvTone = findViewById(R.id.tvTone)
        tvVolume = findViewById(R.id.tvVolume)

        editText = findViewById(R.id.editText)
        btnPlay = findViewById(R.id.btnPlay)
//        btnDownload = findViewById(R.id.btnDownload)

//        recyclerView = findViewById(R.id.recyclerView)

        sbSpeed.tag = tvSpeed
        sbTone.tag = tvTone
        sbVolume.tag = tvVolume

        Speaker.get().init(this, handler)

//        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
//        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))

//        data.addAll(FileUtil.getDownloadFiles())
//        adapter = FileAdapter(this, data)
//        recyclerView.adapter = adapter

    }

    override fun addListener() {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            var type = "0"
            isParamChange = true
            when (checkedId) {
                R.id.rbMan -> type = "1"
                R.id.rbWoman -> type = "0"
                R.id.rbDu -> type = "3"
                R.id.rbYa -> type = "4"
            }
            map.put(SpeechSynthesizer.PARAM_SPEAKER, type)
        }
        sbSpeed.setOnSeekBarChangeListener(seekListener)
        sbTone.setOnSeekBarChangeListener(seekListener)
        sbVolume.setOnSeekBarChangeListener(seekListener)

        btnPlay.setOnClickListener {

            val text = editText.text.toString()
            if (text.isEmpty()) {
                Toast.makeText(this@SpeechSettingActivity, "无需合成文本", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            editText.clearFocus()
            hideSoftInput(this@SpeechSettingActivity, it.windowToken)
            if (isParamChange) Speaker.get().speak(text, map)
            else Speaker.get().speak(text)
            isParamChange = false
        }

//        btnDownload.setOnClickListener {
//            val text = editText.text.toString()
//            if (text.isEmpty()) {
//                Toast.makeText(this@SpeechSettingActivity, "无需合成文本", Toast.LENGTH_LONG).show()
//                return@setOnClickListener
//            }
//            editText.clearFocus()
//            if (isParamChange) Speaker.get().synthesize(text, map)
//            else Speaker.get().synthesize(text)
//            isParamChange = false
//        }

        findViewById<View>(R.id.btnClean).setOnClickListener {
            editText.setText("")
            editText.clearFocus()
        }

//        adapter.setOnItemClickListener { file, _ ->
//            //            FileUtil.openFile(this@SpeechSettingActivity, file)
//            PcmUtil.instance.play(file)
//        }
    }

    private val seekListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            val tv = seekBar?.tag as TextView
            tv.text = "$progress"
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            seekBar?.also {
                isParamChange = true
                when (it.id) {
                    R.id.sbSpeed -> map.put(SpeechSynthesizer.PARAM_SPEED, it.progress.toString())
                    R.id.sbTone -> map.put(SpeechSynthesizer.PARAM_PITCH, it.progress.toString())
                    R.id.sbVolume -> map.put(SpeechSynthesizer.PARAM_VOLUME, it.progress.toString())
                }
            }
        }
    }

    private val handler = Handler { msg ->
        when (msg.what) {
            -1 -> Toast.makeText(this, "合成失败", Toast.LENGTH_LONG).show()
            0 -> {
                val name = msg.obj as String
                Toast.makeText(this, "合成成功$name", Toast.LENGTH_LONG).show()
//                data.add(0, name)
//                adapter.notifyDataSetChanged()
            }
        }
        true
    }

}