package com.zhou.speech.common

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class FileAdapter : RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    var data: ArrayList<String>
    var inflater: LayoutInflater

    constructor(context: Context, data: ArrayList<String>) {
        inflater = LayoutInflater.from(context)
        this@FileAdapter.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        return FileViewHolder(inflater.inflate(android.R.layout.simple_list_item_1, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.file.text = data[position]
        holder.file.setOnClickListener {
            //            listener?.onItemClick(data[position], position)
            listener?.invoke(data[position], position)
        }
    }

    class FileViewHolder : RecyclerView.ViewHolder {

        var file: TextView

        constructor(view: View) : super(view) {
            file = view.findViewById(android.R.id.text1)
        }
    }

    private var listener: ((name: String, pos: Int) -> Unit)? = null

    interface OnItemClickListener {
        fun onItemClick(file: String, position: Int)
    }

    fun setOnItemClickListener(listener: (name: String, pos: Int) -> Unit) {
        this@FileAdapter.listener = listener
    }

}