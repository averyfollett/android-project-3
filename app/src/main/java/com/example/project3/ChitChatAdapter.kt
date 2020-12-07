package com.example.project3

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ChitChatAdapter(private var context: Context) : RecyclerView.Adapter<ChitChatHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChitChatHolder {
        val li = LayoutInflater.from(context)
        return ChitChatHolder(li.inflate(R.layout.chat_cell, parent, false))
    }

    override fun onBindViewHolder(holder: ChitChatHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}