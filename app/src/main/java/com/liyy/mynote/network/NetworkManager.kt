package com.liyy.mynote.network

import android.util.Log
import com.google.gson.Gson
import com.liyy.mynote.BuildConfig
import com.liyy.mynote.UPDATE_URL
import com.liyy.mynote.data.UpdateEntity
import okhttp3.*
import java.io.IOException

object NetworkManager {

    private val mClient: OkHttpClient = OkHttpClient.Builder().build()

    fun checkUpdate(callback: UpdateCallback) {
        val request: Request = Request.Builder()
            .url(UPDATE_URL)
            .build()
        mClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("NetworkManager", "onFailure: " + e.message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val resultStr = response.body!!.string()
                Log.e("NetworkManager", "success:" + resultStr + "--" + response.code)
                if (response.code == 200) {
                    val updateEntity =
                        Gson().fromJson(resultStr, UpdateEntity::class.java)
                    if (BuildConfig.VERSION_CODE <= updateEntity.versionCode) {
                        callback.needUpdate(updateEntity)
                    }
                }
            }
        })
    }

    interface UpdateCallback {
        fun needUpdate(updateEntity: UpdateEntity)
    }
}