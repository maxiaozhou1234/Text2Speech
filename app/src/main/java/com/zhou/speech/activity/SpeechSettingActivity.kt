package com.zhou.speech.activity

import android.view.View
import android.widget.*
import com.zhou.speech.BaseActivity
import com.zhou.speech.R
import com.zhou.speech.common.hideSoftInput
import com.zhou.speech.contract.SettingContract
import com.zhou.speech.contract.SettingPresent
import com.zhou.speech.speak.KEY_SPEAKER
import com.zhou.speech.speak.KEY_SPEED
import com.zhou.speech.speak.KEY_TONE
import com.zhou.speech.speak.KEY_VOLUME

/**
 * 语音合成配置
 */
class SpeechSettingActivity : BaseActivity<SettingPresent>(), SettingContract.View {

    lateinit var radioGroup: RadioGroup
    lateinit var tvSpeed: TextView
    lateinit var tvTone: TextView
    lateinit var tvVolume: TextView
    lateinit var sbSpeed: SeekBar
    lateinit var sbTone: SeekBar
    lateinit var sbVolume: SeekBar
    lateinit var editText: EditText
    lateinit var btnPlay: Button

//    private var isParamChange = false

    //    private val map = hashMapOf(SpeechSynthesizer.PARAM_SPEAKER to "1",
//            SpeechSynthesizer.PARAM_VOLUME to "5",
//            SpeechSynthesizer.PARAM_SPEED to "7",
//            SpeechSynthesizer.PARAM_PITCH to "5")
//    private lateinit var map: HashMap<String, String>

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

        sbSpeed.tag = tvSpeed
        sbTone.tag = tvTone
        sbVolume.tag = tvVolume

//        map = BDSpeaker.get().getConfig(this)

//        val speaker = map[KEY_SPEAKER]
//        val speed = map[KEY_SPEED]
//        val tone = map[KEY_TONE]
//        val volume = map[KEY_VOLUME]
//
//        (radioGroup.getChildAt(speaker!!.toInt()) as RadioButton).isChecked = true
//        tvSpeed.text = speed
//        tvTone.text = tone
//        tvVolume.text = volume
//        sbSpeed.progress = speed!!.toInt()
//        sbTone.progress = tone!!.toInt()
//        sbVolume.progress = volume!!.toInt()
        present = SettingPresent(this, this)

    }

    override fun addListener() {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            //            isParamChange = true
            val type = when (checkedId) {
                R.id.rbWoman -> "0"
                R.id.rbMan -> "1"
                R.id.rbDu -> "3"
                R.id.rbYa -> "4"
                else -> "0"
            }
//            map.put(KEY_SPEAKER, type)//SpeechSynthesizer.PARAM_SPEAKER
            present.update(KEY_SPEAKER, type)
        }
        sbSpeed.setOnSeekBarChangeListener(seekListener)
        sbTone.setOnSeekBarChangeListener(seekListener)
        sbVolume.setOnSeekBarChangeListener(seekListener)

        btnPlay.setOnClickListener {

            val text = editText.text.toString()
            if (text.isEmpty()) {
                Toast.makeText(this@SpeechSettingActivity, "请输入文本", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            editText.clearFocus()
            hideSoftInput(this@SpeechSettingActivity, it.windowToken)
//            if (isParamChange) BDSpeaker.get().play(text, map)
//            else BDSpeaker.get().play(text)
//            isParamChange = false

            present.play(text)
        }

        findViewById<View>(R.id.btnClean).setOnClickListener {
            editText.text.clear()
            editText.clearFocus()
        }
    }

    override fun initView(speaker: Int, speed: Int, tone: Int, volume: Int) {
        (radioGroup.getChildAt(speaker) as RadioButton).isChecked = true
        tvSpeed.text = speed.toString()
        tvTone.text = tone.toString()
        tvVolume.text = volume.toString()
        sbSpeed.progress = speed
        sbTone.progress = tone
        sbVolume.progress = volume
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
                //                isParamChange = true
                when (it.id) {
//                    R.id.sbSpeed -> map.put(KEY_SPEED, it.progress.toString())//SpeechSynthesizer.PARAM_SPEED
//                    R.id.sbTone -> map.put(KEY_TONE, it.progress.toString())//SpeechSynthesizer.PARAM_PITCH
//                    R.id.sbVolume -> map.put(KEY_VOLUME, it.progress.toString())//SpeechSynthesizer.PARAM_VOLUME
                    R.id.sbSpeed -> present.update(KEY_SPEED, it.progress.toString())//SpeechSynthesizer.PARAM_SPEED
                    R.id.sbTone -> present.update(KEY_TONE, it.progress.toString())//SpeechSynthesizer.PARAM_PITCH
                    R.id.sbVolume -> present.update(KEY_VOLUME, it.progress.toString())//SpeechSynthesizer.PARAM_VOLUME
                }
            }
        }
    }

    override fun onDestroy() {
//        BDSpeaker.get().saveConfig(this, map)
        present.onDestroy(this)
        super.onDestroy()
    }

}