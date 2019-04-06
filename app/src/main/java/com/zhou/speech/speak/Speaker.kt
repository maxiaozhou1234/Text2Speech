package com.zhou.speech.speak

import android.content.Context
import android.os.Handler
import org.json.JSONObject

/**
 *  @author Administrator_Zhou
 *  created on 2019/3/24
 */
abstract class Speaker {

    abstract fun init(context: Context)
    abstract fun play(text: String)
    abstract fun play(text: String, map: Map<String, String>)
    abstract fun pause()
    abstract fun stop()
    abstract fun release()
    abstract fun download(text: String, callback: DownloadCallback? = null)
    abstract fun speakType(): String

    abstract fun defaultConfig(): HashMap<String, String>

    fun getConfig(context: Context): HashMap<String, String> {
        val config = context.getSharedPreferences(CONFIG_SPEAKER, 0).getString(speakType(), null)
        config?.apply {
            val json = JSONObject(config)
            val name = json.names()
            if (name != null && name.length() > 0) {
                val map = hashMapOf<String, String>()
                val len = name.length() - 1
                for (i in 0..len) {
                    val k = name.optString(i)
                    val v = json.optString(k)
                    map.put(k, v)
                }
                return map
            }
        }
        return defaultConfig()
    }

    fun saveConfig(context: Context,map: Map<String, String>) {
        val set = map.entries
        val json = JSONObject()
        set.forEach {
            json.put(it.key, it.value)
        }
        context.getSharedPreferences(CONFIG_SPEAKER, 0).edit().putString(speakType(), json.toString()).apply()
    }

}