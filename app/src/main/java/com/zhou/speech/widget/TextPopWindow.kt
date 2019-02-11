package com.zhou.speech.widget

import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.zhou.speech.MainActivity
import com.zhou.speech.R
import com.zhou.speech.common.EDIT_ACTION
import com.zhou.speech.common.EDIT_TEXT
import com.zhou.speech.common.toast

class TextPopWindow(context: Context) : PopupWindow() {

    private var text: String? = null

    fun show(view: View, text: String) {
        this@TextPopWindow.text = text
        showAtLocation(view, Gravity.TOP, 0, view.top + 120)
    }

    private var copyListener: ((String) -> Unit)? = null

    fun setCopyListener(listener: (text: String) -> Unit) {
        copyListener = listener
    }

    private var editListener: ((String) -> Unit)? = null

    fun setEditListener(listener: (text: String) -> Unit) {
        editListener = listener
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_text_pop, null, false)
        val clipboardManager: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        contentView = view
        var w = 0
        var h = 0
        w = View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.UNSPECIFIED)
        h = View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.UNSPECIFIED)
        view.measure(w, h)
        setBackgroundDrawable(null)
        width = view.measuredWidth
        height = view.measuredHeight
        Log.d("zhou", "width = $width height = $height")
        setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
        contentView.findViewById<View>(R.id.btn_copy).setOnClickListener {
            val clip = ClipData(ClipDescription("text", arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)), ClipData.Item(text))
            clipboardManager.primaryClip = clip
            toast(context, context.resources.getString(R.string.success))
        }
        contentView.findViewById<View>(R.id.btn_edit).setOnClickListener {
            context.startActivity(Intent(context, MainActivity::class.java).apply {
                action = EDIT_ACTION
                putExtra(EDIT_TEXT, text)
            })
        }
    }
}