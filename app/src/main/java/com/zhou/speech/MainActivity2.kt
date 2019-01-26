package com.zhou.speech

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View

class MainActivity2 : AppCompatActivity() {

    lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        drawer = findViewById(R.id.drawer)

        findViewById<View>(R.id.ib).setOnClickListener { drawer.closeDrawer(Gravity.LEFT) }
    }
}