package com.zhou.speech.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhou.speech.R
import com.zhou.speech.common.hideSoftInput
import com.zhou.speech.common.toast
import com.zhou.speech.contract.MainContract
import com.zhou.speech.contract.MainPresent
import kotlinx.android.synthetic.main.layout_edit.*

class EditFragment : Fragment(), MainContract.View {

    lateinit var present: MainPresent

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mInflater = inflater ?: LayoutInflater.from(context)
        return mInflater.inflate(R.layout.layout_edit, container, true)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addListener()
//        present.loadData()
    }

    private fun addListener() {

        play.setOnClickListener {
            if (checkText()) {
                present.play(getText())
            } else {
                toast(context, "请输入文本")
            }
        }
        stop.setOnClickListener {
            present.stop()
        }
        download.setOnClickListener {
            if (checkText()) {
                present.download(getText())
            } else {
                toast(context, "请输入文本")
            }
        }
        editPad.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isEmpty() == true) {
                    present.clearCache()
                }
            }
        })
    }

    fun loadData(present: MainPresent) {
        this@EditFragment.present = present
        this@EditFragment.present.loadData()
    }

    override fun setEditFile(id: Int) {
        present.loadData(id)
    }

    override fun setEditText(text: String) {
        editPad.text = Editable.Factory.getInstance().newEditable(text)
    }

    fun clearText() {
        hideSoftInput(context, editPad.windowToken)
        editPad.text.clear()
        editPad.clearFocus()
    }

    fun clearFocus() {
        hideSoftInput(context, editPad.windowToken)
        editPad.clearFocus()
    }

    private fun checkText(): Boolean {
        return !TextUtils.isEmpty(editPad.text)
    }

    private fun getText(): String {
        editPad.clearFocus()
        return editPad.text.toString()
    }

    override fun onPause() {
        super.onPause()
        present.stop()
    }

    override fun startDownload() {
        toast(context, "开始合成音频")
    }

    override fun finishDownload() {
        toast(context, "下载成功")
    }
}