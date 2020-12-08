package com.example.project3

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChitChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private var messageText: TextView = itemView.findViewById(R.id.messageText)

    fun bind(chat: Chat) {
        messageText.text = chat.message
    }
}