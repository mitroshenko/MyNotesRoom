package com.example.android.mynotenew

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.mynotenew.NoteListAdapter.NoteViewHolder
import ivan.mitroshenko.roomnotessample.R

class NoteListAdapter (private val onClick: (NoteEntity) -> Unit):
    ListAdapter<NoteEntity, NoteViewHolder>(NOTES_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.create(parent, onClick)
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }


    class NoteViewHolder(itemView: View, val onClick: (NoteEntity) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val noteItemView: TextView = itemView.findViewById(R.id.tvTitle)

        fun bind(noteEntity: NoteEntity) {
            noteItemView.setOnClickListener { onClick(noteEntity) }
            noteItemView.text = noteEntity.title
        }

        companion object {
            fun create(parent: ViewGroup, onClick: (NoteEntity) -> Unit): NoteViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return NoteViewHolder(view, onClick)
            }
        }
    }

    companion object {
        private val NOTES_COMPARATOR = object : DiffUtil.ItemCallback<NoteEntity>() {
            override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }


}
