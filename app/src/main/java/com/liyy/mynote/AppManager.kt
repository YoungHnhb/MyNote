package com.liyy.mynote

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.liyy.mynote.utils.SPUtils
import java.io.File

/**
 * Author: 李岳阳
 * Date: 2020/7/8
 * Time: 10:23
 * Description：
 */

fun checkUpdate(activity: Activity) {
    val oneDayTimeMillis = 24 * 60 * 60 * 1000
    val lastUpdate = SPUtils.getInstance().getLong(SP_UPDATE_TAG, System.currentTimeMillis() - 2 * oneDayTimeMillis)
    val deltaTime = (System.currentTimeMillis() - lastUpdate) / oneDayTimeMillis
    if (deltaTime > 1) {
        checkUpdateNetwork()
    }
    if (needUpdate().not()) {
        return
    }
    checkPermission(activity)
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
    NetworkManager.get().checkUpdate { updateEntity ->
        SPUtils.getInstance().put(SP_UPDATE_TAG, System.currentTimeMillis())
        SPUtils.getInstance().put(SP_LAST_UPDATE_CODE, updateEntity.versionCode)
        SPUtils.getInstance().put(SP_LAST_UPDATE_URL, updateEntity.url)
    }
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
    DownloadUtil.get().download(url, "download", object : DownloadUtil.OnDownloadListener{
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
            installAPK(path, fileName)
        }
    });
}

fun installAPK(path: String, fileName: String) {
    val apkFile: File = File(path, fileName);
    if (!apkFile.exists()) {
        return;
    }
    val intent: Intent = Intent(Intent.ACTION_VIEW)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val uri: Uri = FileProvider.getUriForFile(MyApp.mApp, "com.liyy.mynote.fileprovider", apkFile)
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
    } else {
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
    }
    MyApp.mApp.startActivity(intent);
}

private fun checkPermission(activity: Activity) {
    if (ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                111)
        }
    }
}