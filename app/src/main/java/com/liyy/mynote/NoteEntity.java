package com.liyy.mynote;

import cn.bmob.v3.BmobObject;

/**
 * Author: 李岳阳
 * Date: 2020/7/2
 * Time: 20:08
 * Description：
 */
public class NoteEntity extends BmobObject {

    private String noteTime;
    private Long noteTimestamp;
    private String title;
    private String value;

    public String getNoteTime() {
        return noteTime;
    }

    public void setNoteTime(String noteTime) {
        this.noteTime = noteTime;
    }

    public Long getNoteTimestamp() {
        return noteTimestamp;
    }

    public void setNoteTimestamp(Long noteTimestamp) {
        this.noteTimestamp = noteTimestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "noteTime='" + noteTime + '\'' + ", title='" + title + '\'' + ", value='" + value;
    }
}
