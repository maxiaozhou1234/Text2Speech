package com.zhou.speech.common

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zhou.speech.R
import com.zhou.speech.db.SimpleFile

class TextAdapter(val context: Context, data: ArrayList<SimpleFile>) : RecyclerView.Adapter<TextAdapter.ViewHolder>() {

    private var data = arrayListOf<SimpleFile>()

    init {
        this@TextAdapter.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_text, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.apply {
            val file = data[position]
            val t = "${file.date}\n${file.summary}"
            text.text = t
            text.setOnClickListener {
                clickListener?.onClick(file.summary)
            }
            text.setOnLongClickListener {
                clickListener?.onLongClick(it, file.id)
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

        fun onLongClick(view: View, id: Int)
    }

    private var clickListener: ClickListener? = null

    fun setClickListener(listener: ClickListener) {
        this.clickListener = listener
    }
}