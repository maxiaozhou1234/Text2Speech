package com.zhou.speech

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.DrawerLayout.STATE_SETTLING
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhou.speech.common.EDIT_ACTION
import com.zhou.speech.common.EDIT_TEXT
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_setting_speech.*
import kotlinx.android.synthetic.main.layout_edit.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG = "Speech"

    lateinit var editFragment: EditFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"))

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
                if (STATE_SETTLING == newState) {
                    toolbar.title = if (drawer_layout.isDrawerOpen(Gravity.START))
                        resources.getString(R.string.app_name) else "更多"
                }
            }

            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
            }

            override fun onDrawerClosed(drawerView: View?) {
            }

            override fun onDrawerOpened(drawerView: View?) {
            }
        })
        toggle.syncState()

        editFragment = supportFragmentManager.findFragmentById(R.id.editFragment) as EditFragment
        nav_view.setNavigationItemSelectedListener(this)

        val rxPermissions = RxPermissions(this)
        rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { permission ->
                    if (permission.granted) {
                        //nothing
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        rxPermissions.request(permission.name).subscribe()
                    } else {
                        val ad = AlertDialog.Builder(this@MainActivity)
                                .setTitle("提醒")
                                .setMessage("本应用需要存储权限放置语音模型，请允许改权限")
                                .setNegativeButton("拒绝", DialogInterface.OnClickListener { dialog, _ ->
                                    dialog.dismiss()
                                    Toast.makeText(this@MainActivity, "权限已被拒绝，请在设置中开启", Toast.LENGTH_SHORT).show()
                                }).setPositiveButton("确定", DialogInterface.OnClickListener { dialog, _ ->
                                    dialog.dismiss()
                                    rxPermissions.request(permission.name).subscribe()
                                })
                                .create()
                        ad.show()
                    }
                }

        if (EDIT_ACTION == intent.action) {
            supportActionBar?.apply {
                setDisplayShowHomeEnabled(true)
            }
            editPad.isEnabled = true
            editPad.text = Editable.Factory.getInstance().newEditable(
                    intent.getStringExtra(EDIT_TEXT) ?: "")
            editFragment.setEditText(intent.getStringExtra(EDIT_TEXT) ?: "")
        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.action_edit) {
//            editPad.isEnabled = !editPad.isEnabled
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        drawer_layout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.nav_bd -> {
                Toast.makeText(this@MainActivity, "百度设置", Toast.LENGTH_LONG).show()
            }
            R.id.nav_xf -> {
                Toast.makeText(this@MainActivity, "讯飞设置", Toast.LENGTH_LONG).show()
            }
            R.id.nav_setting -> {
                Observable.timer(500, TimeUnit.MILLISECONDS)
                        .subscribe {
                            startActivity(Intent(this@MainActivity, SpeechSettingActivity::class.java))
                        }
            }
            R.id.nav_download -> {

                Observable.timer(150, TimeUnit.MILLISECONDS)
                        .subscribe {
                            startActivity(Intent(this@MainActivity, SpeechFileActivity::class.java))
                        }
            }
            R.id.nav_save -> {
                Observable.timer(150, TimeUnit.MILLISECONDS)
                        .subscribe {
                            startActivity(Intent(this@MainActivity, HistoryActivity::class.java))
                        }
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        return true
    }
}
