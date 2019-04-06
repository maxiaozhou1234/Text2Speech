package com.zhou.speech.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.util.concurrent.locks.ReentrantLock

/**
 *  @author Administrator_Zhou
 *  created on 2019/3/28
 */
class FileDB {

    private lateinit var helper: FileDBHelper
    private var db: SQLiteDatabase? = null
    private val lock = ReentrantLock(false)

    private val limit = 20

    companion object {
        fun get(): FileDB {
            return Holder.instance
        }
    }

    object Holder {
        val instance = FileDB()
    }

    fun init(context: Context) {
        helper = FileDBHelper(context)
    }

    private fun openDB() {
        if (db == null || !(db!!.isOpen)) {
            db = helper.writableDatabase
        }
    }

    fun closeDB() {
        if (db != null && db!!.isOpen) {
            db?.close()
        }
    }

    fun insert(text: String) {
        try {
            lock.lock()
            openDB()
            val tmp = text.replace(Regex("\\s+"), "")
            val summary = tmp.substring(0, if (tmp.length > limit) limit else tmp.length)
            val sql = "insert into ${FileDBHelper.TABLE_NAME} (summary,data) values(\"$summary\",\"$text\")"
            db?.execSQL(sql)
        } finally {
            lock.unlock()
        }
    }

    fun delete(id: Int) {
        try {
            lock.lock()
            openDB()
            val sql = "delete from ${FileDBHelper.TABLE_NAME} where id=$id"
            db?.execSQL(sql)
        } finally {
            lock.unlock()
        }
    }

    fun update(id: Int, text: String) {
        try {
            lock.lock()
            openDB()
            val tmp = text.replace(Regex("\\s+"), "")
            val summary = tmp.substring(0, if (tmp.length > limit) limit else tmp.length)
            val sql = "update ${FileDBHelper.TABLE_NAME} set summary='$summary',data='$text',date=(datetime('now','localtime')) where id=$id"
            db?.execSQL(sql)
        } finally {
            lock.unlock()
        }
    }

    fun query(): ArrayList<SimpleFile> {
        val list = arrayListOf<SimpleFile>()
        try {
            lock.lock()
            openDB()
            val sql = "select * from ${FileDBHelper.TABLE_NAME} order by date desc"
            val cursor = db?.rawQuery(sql, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex("id"))
                    val summary = cursor.getString(cursor.getColumnIndex("summary"))
                    val date = cursor.getString(cursor.getColumnIndex("date"))
                    list.add(SimpleFile(id, summary, date))
                }
            }
            cursor?.close()
        } finally {
            lock.unlock()
        }
        return list
    }

    fun query(id: Int): SimpleFile? {
        var file: SimpleFile? = null
        try {
            lock.lock()
            openDB()
            val sql = "select * from ${FileDBHelper.TABLE_NAME} where id = $id"
            val cursor = db?.rawQuery(sql, null)
            if (cursor != null && cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val summary = cursor.getString(cursor.getColumnIndex("summary"))
                val data = cursor.getString(cursor.getColumnIndex("data"))
                val date = cursor.getString(cursor.getColumnIndex("date"))
                file = SimpleFile(id, summary, date)
                file.data = data
                cursor.close()
            }
        } finally {
            lock.unlock()
        }
        return file
    }

    fun queryLatest(): SimpleFile? {
        var file: SimpleFile? = null
        try {
            lock.lock()
            openDB()
            val sql = "select * from ${FileDBHelper.TABLE_NAME} order by date desc limit 1"
            val cursor = db?.rawQuery(sql, null)
            if (cursor != null && cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val summary = cursor.getString(cursor.getColumnIndex("summary"))
                val data = cursor.getString(cursor.getColumnIndex("data"))
                val date = cursor.getString(cursor.getColumnIndex("date"))
                file = SimpleFile(id, summary, date)
                file.data = data
                cursor.close()
            }
        } finally {
            lock.unlock()
        }
        return file
    }
}