package com.liyy.mynote.data;

/**
 * Author: 李岳阳
 * Date: 2020/7/8
 * Time: 9:17
 * Description：
 */
public class UpdateEntity {

    /**
     * versionCode : 1
     * url : xxxxx
     * fileName : MyNote.apk
     */

    private int versionCode;
    private String url;
    private String fileName;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
