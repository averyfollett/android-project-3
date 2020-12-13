package com.example.project3

import android.os.AsyncTask //is deprecated now, but still works great
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    //define all vars we will need
    private lateinit var recyclerView: RecyclerView
    private lateinit var httpClient: OkHttpClient
    private lateinit var chitChatAdapter: ChitChatAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var newMessageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var chats: ArrayList<Chat>
    lateinit var likedChatIDs: ArrayList<String>
    lateinit var dislikedChatIDs: ArrayList<String>
    private val chitChatURL = "https://www.stepoutnyc.com/chitchat"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize to empty array lists
        chats = ArrayList()
        likedChatIDs = ArrayList()
        dislikedChatIDs = ArrayList()

        //set up recycler view for chats
        chitChatAdapter = ChitChatAdapter(this, chats)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = chitChatAdapter

        //initialize http client
        httpClient = OkHttpClient()

        //set up swipe to refresh and assign refresh function
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener { refresh() }

        //set up vars for message posting
        newMessageEditText = findViewById(R.id.newMessageEditText)
        sendButton = findViewById(R.id.sendButton)

        //function called when sending a message
        sendButton.setOnClickListener {
            if (newMessageEditText.text.toString() != "") {
                post(newMessageEditText.text.toString())
                newMessageEditText.text.clear()
                refresh()
            }
        }

        //initial refresh so app displays chats on launch
        refresh()
    }

    //function called when refreshing chats
    fun refresh() {
        val rt = RefreshTask()
        rt.execute()
    }

    //function called when posting a new chat
    //takes chat message as a string
    private fun post(message: String) {
        val pt = PostMessageTask(message)
        pt.execute()
    }

    //function called when liking a message
    //takes message ID as a string
    fun like(messageID: String) {
        val lt = LikeTask(messageID)
        lt.execute()
    }

    //function called when disliking a message
    //takes message ID as a string
    fun dislike(messageID: String) {
        val dt = DislikeTask(messageID)
        dt.execute()
    }

    //implement a refresh task to handle GET request from server
    private inner class RefreshTask : AsyncTask<Void?, Void?, String>() {
        override fun doInBackground(vararg voids: Void?): String {
            //build the proper URL using my key and client name
            var urlBuilder = chitChatURL.toHttpUrlOrNull()?.newBuilder()
            if (urlBuilder != null) {
                urlBuilder.addQueryParameter("key", "fa070b3c-0685-431d-9fe2-e156bcbcfadb")
                urlBuilder.addQueryParameter("client", "avery.follett@mymail.champlain.edu")
            }
            var getURL = urlBuilder?.build().toString()

            //build and send the actual request
            val request: Request = Request.Builder()
                .url(getURL)
                .build()
            try {
                //try and return the JSON
                httpClient.newCall(request).execute().use { response -> return response.body!!.string() }
            } catch (e: Exception) {
                e.printStackTrace()
                return "Error: Could not complete request."
            }
        }

        //parse JSON and refresh our recycler view
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            parseJSON(result)
            chitChatAdapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    //implement a post task to handle POST request from server
    private inner class PostMessageTask(private var message: String) : AsyncTask<Void?, Void?, String>() {
        override fun doInBackground(vararg voids: Void?): String {
            //build the proper URL using my key and client name
            val requestBody: RequestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("key", "fa070b3c-0685-431d-9fe2-e156bcbcfadb")
                    .addFormDataPart("client", "avery.follett@mymail.champlain.edu")
                    .addFormDataPart("message", message)
                    .build()

            //build and send the actual request
            val request: Request = Request.Builder()
                    .url(chitChatURL)
                    .post(requestBody)
                    .build()
            return try {
                httpClient.newCall(request).execute()
                "Posted"
            } catch (e: Exception) {
                e.printStackTrace()
                "Error: Could not complete request."
            }
        }
    }

    //implement a like task to handle GET request from server
    private inner class LikeTask(private var messageID: String) : AsyncTask<Void?, Void?, String>() {
        override fun doInBackground(vararg voids: Void?): String {
            //build the proper URL using my key and client name
            val likeURL = "$chitChatURL/like/$messageID"
            var urlBuilder = likeURL.toHttpUrlOrNull()?.newBuilder()
            if (urlBuilder != null) {
                urlBuilder.addQueryParameter("key", "fa070b3c-0685-431d-9fe2-e156bcbcfadb")
                urlBuilder.addQueryParameter("client", "avery.follett@mymail.champlain.edu")
            }
            var getURL = urlBuilder?.build().toString()

            //build and send the actual request
            val request: Request = Request.Builder()
                    .url(getURL)
                    .build()
            try {
                //try and return the JSON
                httpClient.newCall(request).execute().use { response -> return response.body!!.string() }
            } catch (e: Exception) {
                e.printStackTrace()
                return "Error: Could not complete request."
            }
        }
    }

    //implement a dislike task to handle GET request from server
    private inner class DislikeTask(private var messageID: String) : AsyncTask<Void?, Void?, String>() {
        override fun doInBackground(vararg voids: Void?): String {
            //build the proper URL using my key and client name
            val likeURL = "$chitChatURL/dislike/$messageID"
            var urlBuilder = likeURL.toHttpUrlOrNull()?.newBuilder()
            if (urlBuilder != null) {
                urlBuilder.addQueryParameter("key", "fa070b3c-0685-431d-9fe2-e156bcbcfadb")
                urlBuilder.addQueryParameter("client", "avery.follett@mymail.champlain.edu")
            }
            var getURL = urlBuilder?.build().toString()

            //build and send the actual request
            val request: Request = Request.Builder()
                    .url(getURL)
                    .build()
            try {
                //try and return the JSON
                httpClient.newCall(request).execute().use { response -> return response.body!!.string() }
            } catch (e: Exception) {
                e.printStackTrace()
                return "Error: Could not complete request."
            }
        }
    }

    //implement a JSON parser to fill out our Chat objects
    private fun parseJSON(jsonString: String) {
        //clear previous chats before starting
        chats.clear()
        //for each chat, extract the strings from JSON and then create a new chat object with them
        try {
            val responseObject = JSONObject(jsonString)
            val items = responseObject.getJSONArray("messages")
            for (i in 0 until items.length()) {
                val item = items.getJSONObject(i)
                val id = item.getString("_id")
                val clientName = item.getString("client").replaceAfter("@", "").replace("@", "")
                val message = item.getString("message")
                val timestamp = item.getString("date")
                val likes = item.getString("likes").toInt()
                val dislikes = item.getString("dislikes").toInt()
                chats.add(Chat(id, clientName, message, timestamp, likes, dislikes))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}