package com.liyy.mynote;

import android.util.Log;

import com.google.gson.Gson;
import com.liyy.mynote.data.UpdateEntity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: 李岳阳
 * Date: 2020/7/8
 * Time: 9:09
 * Description：
 */
public class NetworkManager {

    private static NetworkManager instance;
    private final OkHttpClient okHttpClient;

    protected NetworkManager() {
        okHttpClient = new OkHttpClient();
    }

    public static NetworkManager get() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    public void checkUpdate(UpdateCallback callback) {
        Request request = new Request.Builder()
                .url(ConstantsKt.UPDATE_URL)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("NetworkManager", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resultStr = response.body().string();
                Log.e("NetworkManager", "success:" + resultStr + "--" + response.code());
                if (response.code() == 200) {
                    UpdateEntity updateEntity = new Gson().fromJson(resultStr, UpdateEntity.class);
                    if (BuildConfig.VERSION_CODE <= updateEntity.getVersionCode()) {
                        callback.needUpdate(updateEntity);
                    }
                }
            }
        });
    }

    interface UpdateCallback {
        void needUpdate(UpdateEntity updateEntity);
    }
}
