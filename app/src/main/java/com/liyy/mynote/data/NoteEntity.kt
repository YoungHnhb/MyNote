package com.liyy.mynote.data

import cn.bmob.v3.BmobObject

data class NoteEntity(
    var noteTime: String = "",
    var noteTimestamp: Long = 0,
    var title: String = "",
    var value: String = ""
) : BmobObject() {
}