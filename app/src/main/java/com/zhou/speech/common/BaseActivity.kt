package com.zhou.speech.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
        init()
        addListener()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (android.R.id.home == item?.itemId) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    abstract fun setContentView()
    abstract fun init()
    abstract fun addListener()

    fun back() {
        finish()
    }
}