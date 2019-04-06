package com.zhou.speech.activity

import android.content.*
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import com.zhou.speech.BaseActivity
import com.zhou.speech.BasePresent
import com.zhou.speech.R
import com.zhou.speech.common.*
import com.zhou.speech.db.FileDB
import com.zhou.speech.db.SimpleFile
import com.zhou.speech.widget.TextPopWindow
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : BaseActivity<BasePresent>() {

    lateinit var textAdapter: TextAdapter
    var textPop: TextPopWindow? = null
    private val data = FileDB.get().query()

    override fun setContentView() {
        setContentView(R.layout.activity_history)
    }

    override fun init() {
        supportActionBar?.title = "历史文本"

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        textAdapter = TextAdapter(this, data)
        recyclerView.adapter = textAdapter
    }

    override fun addListener() {
        textAdapter.setClickListener(object : TextAdapter.ClickListener {
            override fun onClick(text: String) {
                hidePop()
            }

            override fun onLongClick(view: View, id: Int) {
                showPop(view, id)
            }
        })

        recyclerView.setOnTouchListener { v: View?, event: MotionEvent? ->
            hidePop()
            false
        }
    }

    fun showPop(view: View, id: Int) {
        hidePop()
        if (textPop == null) {
            textPop = TextPopWindow(this)
            textPop?.editListener = { id ->
                if (id != -1) {
                    startActivity(Intent(this@HistoryActivity, EditActivity::class.java).apply {
                        action = EDIT_ACTION
                        putExtra(EDIT_FILE_ID, id)
                    })
                }
            }
            textPop?.copyListener = { id ->
                if (id != -1) {
                    val simpleFile: SimpleFile? = FileDB.get().query(id)
                    simpleFile?.also {
                        val clipboardManager: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData(ClipDescription("text", arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)), ClipData.Item(it.data))
                        clipboardManager.primaryClip = clip
                        toast(this@HistoryActivity, resources.getString(R.string.success))
                    }
                }
            }

        }
        textPop?.show(view, id)
    }

    fun hidePop() {
        textPop?.apply {
            if (isShowing) {
                dismiss()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        data.clear()
        data.addAll(FileDB.get().query())
        textAdapter.notifyDataSetChanged()
        empty.visibility = if (data.isEmpty()) View.VISIBLE else View.GONE
    }


}