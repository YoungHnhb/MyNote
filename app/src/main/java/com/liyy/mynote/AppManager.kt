package com.liyy.mynote

import android.app.Activity
import android.app.ProgressDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.liyy.mynote.data.UpdateEntity
import com.liyy.mynote.network.NetworkManager
import com.liyy.mynote.permission.PermissionUtil
import com.liyy.mynote.utils.AppUtil
import com.liyy.mynote.utils.SPUtils

/**
 * Author: 李岳阳
 * Date: 2020/7/8
 * Time: 10:23
 * Description：
 */

fun checkUpdate(activity: Activity) {
    val oneDayTimeMillis = 24 * 60 * 60 * 1000
    val lastUpdate = SPUtils.getInstance()
        .getLong(SP_UPDATE_TAG, System.currentTimeMillis() - 2 * oneDayTimeMillis)
    val deltaTime = (System.currentTimeMillis() - lastUpdate) / oneDayTimeMillis
    if (deltaTime > 1) {
        checkUpdateNetwork()
    }
    if (needUpdate().not()) {
        return
    }
    PermissionUtil.checkPermission(activity)
    MaterialAlertDialogBuilder(activity)
        .setTitle("检测到新版本是否更新?")
        .setPositiveButton("立即下载") { _, _ -> showUpdateProgressDialog(activity) }
        .setNegativeButton("暂不更新", null)
        .show()

    /*
    上面lambda补全代码
    .setPositiveButton("立即下载", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                showUpdateProgressDialog(activity)
            }

        })
     */
}

fun checkUpdateNetwork() {
    NetworkManager.checkUpdate(object : NetworkManager.UpdateCallback{
        override fun needUpdate(updateEntity: UpdateEntity) {
            SPUtils.getInstance().put(SP_UPDATE_TAG, System.currentTimeMillis())
            SPUtils.getInstance().put(SP_LAST_UPDATE_CODE, updateEntity.versionCode)
            SPUtils.getInstance().put(SP_LAST_UPDATE_URL, updateEntity.url)
        }
    })
}

fun needUpdate(): Boolean {
    return BuildConfig.VERSION_CODE < SPUtils.getInstance().getInt(SP_LAST_UPDATE_CODE)
}

private fun showUpdateProgressDialog(activity: Activity) {
    val simpleDialog = ProgressDialog(activity)
    simpleDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
    simpleDialog.setCancelable(false)
    simpleDialog.max = 100

    val url = SPUtils.getInstance().getString(SP_LAST_UPDATE_URL, "")
    DownloadUtil.get().download(url, "download", object : DownloadUtil.OnDownloadListener {
        override fun onDownloading(progress: Int) {
            activity.runOnUiThread {
                if (simpleDialog.isShowing.not()) {
                    simpleDialog.show()
                }
                simpleDialog.progress = progress
            }
        }

        override fun onDownloadFailed() {
            simpleDialog.dismiss()
        }

        override fun onDownloadSuccess(path: String, fileName: String) {
            simpleDialog.dismiss()
            AppUtil.installAPK(path, fileName, MyApp.mApp)
        }
    });
}
