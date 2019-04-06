package com.zhou.speech.widget

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.zhou.speech.R

class TextPopWindow(context: Context) : PopupWindow() {

    private var id: Int = -1

    fun show(view: View, id: Int) {
        this@TextPopWindow.id = id
        showAtLocation(view, Gravity.TOP, 0, view.top + 130)
    }

    var copyListener: ((Int) -> Unit)? = null

    var editListener: ((Int) -> Unit)? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_text_pop, null, false)
        contentView = view
        var w = 0
        var h = 0
        w = View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.UNSPECIFIED)
        h = View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.UNSPECIFIED)
        view.measure(w, h)
        setBackgroundDrawable(null)
        width = view.measuredWidth
        height = view.measuredHeight
        contentView.findViewById<View>(R.id.btn_copy).setOnClickListener {
            copyListener?.invoke(id)
            dismiss()
        }
        contentView.findViewById<View>(R.id.btn_edit).setOnClickListener {
            editListener?.invoke(id)
            dismiss()
        }
    }
}