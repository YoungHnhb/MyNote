package com.liyy.mynote;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Author: 李岳阳
 * Date: 2020/7/2
 * Time: 18:37
 * Description：
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // init Bmob
        Bmob.initialize(this, "fd256321add2a9423c193ef1299bbbbb");
    }
}
