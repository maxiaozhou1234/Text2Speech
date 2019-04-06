package com.zhou.speech.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zhou.speech.R
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.concurrent.TimeUnit

/**
 *  @author Administrator_Zhou
 *  created on 2019/4/6
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        Observable.interval(1, TimeUnit.SECONDS)
                .take(4)
                .subscribe { a ->
                    if (a == 3L) {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }
                    val t = "${(3 - a)}s"
                    text.text = t
                }
    }
}