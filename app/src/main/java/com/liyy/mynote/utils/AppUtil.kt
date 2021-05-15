package com.liyy.mynote.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.liyy.mynote.MyApp
import java.io.File

object AppUtil {

    fun installAPK(path: String, fileName: String, context: Context) {
        val apkFile: File = File(path, fileName);
        if (!apkFile.exists()) {
            return;
        }
        val intent: Intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val uri: Uri = FileProvider.getUriForFile(context, "com.liyy.mynote.fileprovider", apkFile)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
        }
        MyApp.mApp.startActivity(intent);
    }

}