package com.liyy.mynote

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import kotlinx.android.synthetic.main.activity_add_note.*
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Author: 李岳阳
 * Date: 2020/7/2
 * Time: 18:26
 * Description：
 */
class AddNoteActivity : AppCompatActivity() {

    private var mDateTimeStr = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_note)
        initView()
        initTimePicker()
        initActionPicker()
        initConfirmBtn()
    }

    private fun initView() {
        supportActionBar?.let {tb ->
            tb.title = ""
            tb.setHomeButtonEnabled(true)
            tb.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun initConfirmBtn() {
        confirm_bt.setOnClickListener {
            var noteEntity = NoteEntity()
            noteEntity.noteTime = "${date_tv.text} ${time_tv.text}"
            noteEntity.noteTimestamp = transToTimeStamp("${date_tv.text} ${time_tv.text}")
            noteEntity.title = title_list_popup.text.toString()
            noteEntity.value = note_value_et.text.toString()

            noteEntity.save(object : SaveListener<String>() {
                override fun done(result: String?, e: BmobException?) {
                    if (e == null) {
                        Toast.makeText(this@AddNoteActivity, "添加成功 ^_^", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddNoteActivity, "添加失败,请稍后再试 >_<", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
        }
    }

    private fun initTimePicker() {
        mDateTimeStr = transToString(System.currentTimeMillis())
        date_tv.text = mDateTimeStr.split(" ")[0]
        time_tv.text = mDateTimeStr.split(" ")[1]

        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        date_tv.setOnClickListener {
            var datePickerDialog = DatePickerDialog(
                this@AddNoteActivity,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val monthStr = String.format("%02d", month + 1)
                    val dayOfMonthStr = String.format("%02d", dayOfMonth)
                    date_tv.text = "${year}-${monthStr}-${dayOfMonthStr}"
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        time_tv.setOnClickListener {
            var timePickerDialog = TimePickerDialog(
                this@AddNoteActivity,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    val hourOfDayStr = String.format("%02d", hourOfDay)
                    val minuteStr = String.format("%02d", minute)
                    time_tv.text = "${hourOfDayStr}:${minuteStr}"
                },
                hour,
                minute,
                DateFormat.is24HourFormat(this@AddNoteActivity)
            )
            timePickerDialog.show()
        }

    }

    fun transToString(time: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm").format(time)
    }

    fun transToTimeStamp(date: String): Long {
        return SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date, ParsePosition(0)).time / 1000
    }

    private fun initActionPicker() {
        var titleListPopupWindow = ListPopupWindow(this)
        var titleDataList = ArrayList<String>()
        titleDataList.add("睡觉")
        titleDataList.add("睡醒")
        titleDataList.add("奶粉")
        titleDataList.add("吸奶")
        titleDataList.add("大便")

        title_list_popup.setOnClickListener {
            with(titleListPopupWindow) {
                setAdapter(
                    ArrayAdapter(
                        this@AddNoteActivity,
                        android.R.layout.simple_list_item_1,
                        titleDataList
                    )
                )
                anchorView = title_list_popup
                setOnItemClickListener { parent, view, position, id ->
                    title_list_popup.text = titleDataList[position]
                    this.dismiss()
                }
                show()
            }
        }
    }
}