package com.zhou.speech.activity

import com.zhou.speech.BaseActivity
import com.zhou.speech.BasePresent
import com.zhou.speech.R

/**
 *  @author Administrator_Zhou
 *  created on 2019/4/6
 */
class ShareActivity : BaseActivity<BasePresent>() {

    override fun setContentView() {
        setContentView(R.layout.activity_share)
    }

    override fun init() {
        supportActionBar?.title = "分享"
    }

    override fun addListener() {
    }

}