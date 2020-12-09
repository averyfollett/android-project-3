package com.example.project3

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChitChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private var nameText: TextView = itemView.findViewById(R.id.userNameText)
    private var timestampText: TextView = itemView.findViewById(R.id.timestampText)
    private var messageText: TextView = itemView.findViewById(R.id.messageText)
    private var likesText: TextView = itemView.findViewById(R.id.likesText)
    private var dislikesText: TextView = itemView.findViewById(R.id.dislikesText)

    fun bind(chat: Chat) {
        nameText.text = chat.clientName
        timestampText.text = chat.timestamp
        messageText.text = chat.message
        likesText.text = chat.likes.toString() + " Likes"
        dislikesText.text = chat.dislikes.toString() + " Dislikes"
    }
}