package com.example.project3

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ChitChatHolder(private var context: Context, itemView: View) : RecyclerView.ViewHolder(itemView){

    //create and assign all variables in the chat cell
    private var nameText: TextView = itemView.findViewById(R.id.userNameText)
    private var timestampText: TextView = itemView.findViewById(R.id.timestampText)
    private var messageText: TextView = itemView.findViewById(R.id.messageText)
    private var likesText: TextView = itemView.findViewById(R.id.likesText)
    private var dislikesText: TextView = itemView.findViewById(R.id.dislikesText)
    private var likeButton: Button = itemView.findViewById(R.id.likeButton)
    private var dislikeButton: Button = itemView.findViewById(R.id.dislikeButton)

    //var to reference the main activity which contains important data and functions
    private val mainActivity = context as MainActivity

    fun bind(chat: Chat) {
        //bind the chat data to the corresponding views in the chat cell
        nameText.text = chat.clientName
        timestampText.text = chat.timestamp
        messageText.text = chat.message
        val likes = chat.likes.toString() + " Likes"
        likesText.text = likes
        val dislikes = chat.dislikes.toString() + " Dislikes"
        dislikesText.text = dislikes

        /*
        //tried to add color switching to like and dislike buttons but the colors were repeated on random other messages
        //likely caused by something with the recycler view???
        if (mainActivity.likedChatIDs.contains(chat.id)) {
            likeButton.setBackgroundColor(Color.GREEN)
            dislikeButton.setBackgroundColor(Color.GRAY)
        }
        else if (mainActivity.dislikedChatIDs.contains(chat.id)) {
            likeButton.setBackgroundColor(Color.GRAY)
            dislikeButton.setBackgroundColor(Color.RED)
        }
        */

        //function called when like button pressed
        likeButton.setOnClickListener {
            //if we haven't already liked the message, let us like it and then refresh the data
            if (!mainActivity.likedChatIDs.contains(chat.id)) {
                chat.id?.let { it1 -> mainActivity.like(it1) }
                chat.id?.let { it1 -> mainActivity.likedChatIDs.add(it1) }
                mainActivity.refresh()
            }
            else {
                //else, let us know we've already liked it
                Toast.makeText(context, "Already Liked", Toast.LENGTH_SHORT)
                        .show()
            }
        }

        //function called when dislike button pressed
        dislikeButton.setOnClickListener {
            //if we haven't already disliked the message, let us dislike it and then refresh the data
            if (!mainActivity.dislikedChatIDs.contains(chat.id)) {
                chat.id?.let { it1 -> mainActivity.dislike(it1) }
                chat.id?.let { it1 -> mainActivity.dislikedChatIDs.add(it1) }
                mainActivity.refresh()
            }
            else {
                //else, let us know we've already disliked it
                Toast.makeText(context, "Already Disliked", Toast.LENGTH_SHORT)
                        .show()
            }
        }
    }
}