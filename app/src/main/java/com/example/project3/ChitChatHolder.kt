package com.example.project3

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChitChatHolder(private var context: Context, itemView: View) : RecyclerView.ViewHolder(itemView){

    private var nameText: TextView = itemView.findViewById(R.id.userNameText)
    private var timestampText: TextView = itemView.findViewById(R.id.timestampText)
    private var messageText: TextView = itemView.findViewById(R.id.messageText)
    private var likesText: TextView = itemView.findViewById(R.id.likesText)
    private var dislikesText: TextView = itemView.findViewById(R.id.dislikesText)
    private var likeButton: Button = itemView.findViewById(R.id.likeButton)
    private var dislikeButton: Button = itemView.findViewById(R.id.dislikeButton)

    private val mainActivity = context as MainActivity

    fun bind(chat: Chat) {
        nameText.text = chat.clientName
        timestampText.text = chat.timestamp
        messageText.text = chat.message
        likesText.text = chat.likes.toString() + " Likes"
        dislikesText.text = chat.dislikes.toString() + " Dislikes"

        likeButton.setOnClickListener {
            mainActivity.like(chat.id.toString())
            mainActivity.refresh()
        }

        dislikeButton.setOnClickListener {
            mainActivity.dislike(chat.id.toString())
            mainActivity.refresh()
        }
    }
}