package com.zhou.speech.common

import android.content.Context
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

const val EDIT_ACTION = "edit_action"
const val EDIT_TEXT = "edit_text"

fun hideSoftInput(context: Context, token: IBinder) {
    val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(token, 0)
}

fun toast(context: Context, text: String, time: Int = Toast.LENGTH_LONG) =
        Toast.makeText(context, text, time).show()
