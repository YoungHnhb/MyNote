package com.liyy.mynote

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mAdapter: NoteAdapter
    private var mPageLimit = 50
    private var mPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setModeNight()
        initView()
//        checkUpdate(this)
    }

    private fun initView() {
        mAdapter = NoteAdapter()
        data_list_rv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mAdapter
        }
        swipe_refresh_layout.setColorSchemeColors(
            ContextCompat.getColor(this, android.R.color.holo_orange_light),
            ContextCompat.getColor(this, android.R.color.holo_red_light),
            ContextCompat.getColor(this, android.R.color.holo_green_light))
        swipe_refresh_layout.setOnRefreshListener {
            refreshData(false)
        }
        swipe_refresh_layout.setOnOverScrollTopListener {
            startActivity(Intent(this@MainActivity, AddNoteActivity::class.java))
        }
        data_list_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !recyclerView.canScrollVertically(1)) {
                    refreshData(true)
                }
            }
        })
    }

    private fun setModeNight() {
        val c: Calendar = Calendar.getInstance()
        c.timeInMillis = System.currentTimeMillis()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        if (20 <= hour || hour <= 5) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    override fun onResume() {
        super.onResume()
        swipe_refresh_layout.isRefreshing = true
        refreshData(false)
    }

    private fun refreshData(isLoadMore: Boolean) {
        var bmobQuery: BmobQuery<NoteEntity> = BmobQuery<NoteEntity>()
        bmobQuery.setLimit(mPageLimit)
        bmobQuery.order("-noteTimestamp")
        if (isLoadMore) {
            bmobQuery.setSkip(mPage * mPageLimit)
        } else {
            bmobQuery.setSkip(0)
        }
        bmobQuery.findObjects(object : FindListener<NoteEntity>() {
            override fun done(result: MutableList<NoteEntity>?, exception: BmobException?) {
                if (exception == null) {
                    if (result == null) {
                        return
                    }
                    if (isLoadMore) {
                        mAdapter.addDataList(result as ArrayList<NoteEntity>)
                        mPage++
                    } else {
                        mAdapter.setDataList(result as ArrayList<NoteEntity>)
                        mPage = 1
                    }
                } else {
                    Toast.makeText(this@MainActivity, exception.message, Toast.LENGTH_SHORT).show()
                }
                swipe_refresh_layout.isRefreshing = false
            }
        })
    }

}
