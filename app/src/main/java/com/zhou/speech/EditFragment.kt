package com.zhou.speech

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhou.speech.common.toast
import kotlinx.android.synthetic.main.layout_edit.*

class EditFragment : Fragment() {

    lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        root = inflater!!.inflate(R.layout.layout_edit, container, true)
        return root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addListener()
    }

    private fun addListener() {

        download.setOnClickListener { toast(context, "下载") }
        play.setOnClickListener { toast(context, "播放") }
        stop.setOnClickListener { toast(context, "停止") }
        clean.setOnClickListener {
            editPad.setText("")
            editPad.clearFocus()
        }

    }

    fun setEditText(text: String) {
        editPad.text = Editable.Factory.getInstance().newEditable(text)
    }

}