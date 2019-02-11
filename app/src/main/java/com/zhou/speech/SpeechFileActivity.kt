package com.zhou.speech

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.zhou.speech.common.BaseActivity
import com.zhou.speech.common.FileAdapter
import com.zhou.speech.common.FileUtil
import com.zhou.speech.speak.PcmUtil
import kotlinx.android.synthetic.main.activity_file_speech.*

/**
 * 已下载的音频文件
 */
class SpeechFileActivity : BaseActivity() {

    lateinit var adapter: FileAdapter
    val data = arrayListOf<String>()

    override fun setContentView() {
        setContentView(R.layout.activity_file_speech)
    }

    override fun init() {
        supportActionBar?.title = "已下载音频"
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        data.addAll(FileUtil.getDownloadFiles())
        adapter = FileAdapter(this, data)
        recyclerView.adapter = adapter
    }

    override fun addListener() {
        adapter.setOnItemClickListener { file, _ ->
            PcmUtil.instance.play(file)
        }
    }
}