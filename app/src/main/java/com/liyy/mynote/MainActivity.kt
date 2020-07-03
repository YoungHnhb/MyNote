package com.liyy.mynote

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
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
            Handler().postDelayed({
                refreshData()

            }, 3000)
        }

    }

    override fun onResume() {
        super.onResume()
        swipe_refresh_layout.isRefreshing = true
        refreshData()
    }

    private fun refreshData() {
        var bmobQuery: BmobQuery<NoteEntity> = BmobQuery<NoteEntity>()
        bmobQuery.setLimit(500)
        bmobQuery.findObjects(object : FindListener<NoteEntity>() {
            override fun done(result: MutableList<NoteEntity>?, exception: BmobException?) {
                if (exception == null) {
                    if (result == null) {
                        return
                    }
                    mAdapter.setDataList(result as ArrayList<NoteEntity>)
                } else {
                    Toast.makeText(this@MainActivity, exception.message, Toast.LENGTH_SHORT).show()
                }
                swipe_refresh_layout.isRefreshing = false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> startActivity(Intent(this@MainActivity, AddNoteActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}
