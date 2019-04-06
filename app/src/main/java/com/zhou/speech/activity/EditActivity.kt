package com.zhou.speech.activity

import android.view.Menu
import android.view.MenuItem
import com.zhou.speech.BaseActivity
import com.zhou.speech.R
import com.zhou.speech.common.EDIT_ACTION
import com.zhou.speech.common.EDIT_FILE_ID
import com.zhou.speech.contract.MainPresent

/**
 *  @author Administrator_Zhou
 *  created on 2019/4/5
 */
class EditActivity : BaseActivity<MainPresent>() {

    private lateinit var editFragment: EditFragment

    override fun setContentView() {
        setContentView(R.layout.activity_edit)
    }

    override fun init() {
        supportActionBar?.title = "编辑"
        editFragment = supportFragmentManager.findFragmentById(R.id.edit) as EditFragment
        if (EDIT_ACTION == intent.action) {
            supportActionBar?.apply {
                this.setDisplayHomeAsUpEnabled(true)
            }
            editFragment.setEditFile(intent.getIntExtra(EDIT_FILE_ID, 0))
        }
        present = MainPresent(editFragment)
        editFragment.present = present
    }

    override fun addListener() {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_clean) {
            editFragment.clearText()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        present.stop()
    }
}