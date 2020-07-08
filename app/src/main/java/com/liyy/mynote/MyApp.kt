package com.liyy.mynote

import android.app.Application
import android.content.Context
import cn.bmob.v3.Bmob

/**
 * Author: 李岳阳
 * Date: 2020/7/2
 * Time: 17:48
 * Description：
 */
class MyApp : Application() {



    override fun onCreate() {
        super.onCreate()

        // init Bmob
        Bmob.initialize(this, "fd256321add2a9423c193ef1299bbbbb")
        mApp = applicationContext
    }

    companion object {
        lateinit var mApp: Context
    }

}