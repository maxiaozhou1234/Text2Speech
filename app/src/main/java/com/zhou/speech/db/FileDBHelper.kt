package com.zhou.speech.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 *  @author Administrator_Zhou
 *  created on 2019/3/28
 */
class FileDBHelper(context: Context) : SQLiteOpenHelper(context, NAME, null, VERSION) {

    companion object {
        const val NAME = "files.db"
        const val TABLE_NAME = "files"
        const val VERSION = 1

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "create TABLE if not exists $TABLE_NAME (id integer primary key autoincrement,summary text,data text,date datetime default (datetime('now','localtime')))"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}