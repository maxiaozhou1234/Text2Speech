package com.zhou.speech.common

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zhou.speech.R
import kotlinx.android.synthetic.main.layout_text.view.*

class TextAdapter : RecyclerView.Adapter<TextAdapter.ViewHolder> {

    private val context: Context
    private val data = arrayListOf<String>()

    constructor(context: Context) {
        this.context = context

        var it = "文本显示测试文本显示测试文本显示测试文本显示测试文本显示测试"
        for (i in 0..20) {
            it += "item $i"
            data.add(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_text, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.apply {
            val t = data[position]
            text.text = t
            text.setOnClickListener {
                clickListener?.onClick(t)
            }
            text.setOnLongClickListener {
                clickListener?.onLongClick(it, t)
                true
            }
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text: TextView = itemView.findViewById(R.id.text)

    }

//    private var clickListener: ((String)-> Unit)? = null
//
//    fun setClickListener(listener: (text:String) -> Unit) {
//        clickListener = listener
//    }

    interface ClickListener {
        fun onClick(text: String)

        fun onLongClick(view: View, text: String)
    }

    private var clickListener: ClickListener? = null

    fun setClickListener(listener: ClickListener) {
        this.clickListener = listener
    }
}