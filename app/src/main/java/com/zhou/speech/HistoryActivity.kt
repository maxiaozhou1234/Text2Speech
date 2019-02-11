package com.zhou.speech

import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import com.zhou.speech.common.BaseActivity
import com.zhou.speech.common.TextAdapter
import com.zhou.speech.widget.TextPopWindow
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : BaseActivity() {

    lateinit var textAdapter: TextAdapter
    var textPop: TextPopWindow? = null

    override fun setContentView() {
        setContentView(R.layout.activity_history)
    }

    override fun init() {
        supportActionBar?.title = "历史文本"

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        textAdapter = TextAdapter(this)
        recyclerView.adapter = textAdapter

    }

    override fun addListener() {
        textAdapter.setClickListener(object : TextAdapter.ClickListener {
            override fun onClick(text: String) {
                hidePop()
            }

            override fun onLongClick(view: View, text: String) {
                showPop(view, text)
            }
        })

        recyclerView.setOnTouchListener { v: View?, event: MotionEvent? ->
            hidePop()
            false
        }
    }

    fun showPop(view: View, text: String) {
        hidePop()
        if (textPop == null) {
            textPop = TextPopWindow(this)
        }
        textPop?.show(view, text)
    }

    fun hidePop() {
        textPop?.apply {
            if (isShowing) {
                dismiss()
            }
        }
    }


}