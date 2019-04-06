package com.zhou.speech.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.content.FileProvider
import com.zhou.speech.BuildConfig
import java.io.File
import java.net.URI

object FileUtil {

    val downloadPath = Environment.getExternalStorageDirectory().path + "/speech/"

    fun getDownloadFiles(): ArrayList<String> {
        val data = arrayListOf<String>()
        val file = File(downloadPath)
        if (file.isDirectory) {
            file.listFiles().sortedWith(Comparator { o1, o2 ->
                if (o1.lastModified() > o2.lastModified()) -1
                else 1
            }).map { f -> data.add(f.name) }
        }
        return data
    }

    fun openFile(context: Context, fileName: String) {
        val path = "$downloadPath$fileName"
        val uri: Uri?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", File(path))
        else
            uri = Uri.fromFile(File(path))
        val intent = Intent().run {
            action = Intent.ACTION_VIEW
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            setDataAndType(uri, "*/*")
        }
        context.startActivity(intent)
    }
}
