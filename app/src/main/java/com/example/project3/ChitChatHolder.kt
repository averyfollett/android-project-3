package com.example.project3

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChitChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private lateinit var messageText: TextView

    fun bind(urlString: String?) {
    }

    init {
        messageText = itemView.findViewById(R.id.messageText)
    }
}