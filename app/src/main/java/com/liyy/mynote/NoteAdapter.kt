package com.liyy.mynote

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

/**
 * Author: 李岳阳
 * Date: 2020/7/2
 * Time: 14:59
 * Description：
 */
class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteHolder>(), View.OnClickListener {

    private var dataList = ArrayList<NoteEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_note_adapter, parent, false
        )
        return NoteHolder(view)
    }

    fun setDataList(data: ArrayList<NoteEntity>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val noteEntity = dataList[position]
        holder.timeTv.text = noteEntity.noteTime
        holder.titleTv.text = noteEntity.title
        holder.valueTv.text = noteEntity.value
        holder.parentLayout.setOnClickListener(this)
        holder.parentLayout.setTag(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var parentLayout: ConstraintLayout = itemView.findViewById(R.id.note_adapter_layout_cl)
        var timeTv: TextView = itemView.findViewById(R.id.time_tv)
        var titleTv: TextView = itemView.findViewById(R.id.title_tv)
        var valueTv: TextView = itemView.findViewById(R.id.value_tv)
    }

    override fun onClick(v: View?) {
        val noteEntity = dataList[v?.getTag() as Int]
        val intent = Intent(v?.context, AddNoteActivity::class.java)
        intent.putExtra("NOTE_ID", noteEntity.objectId)
        v?.context.startActivity(intent)
    }
}