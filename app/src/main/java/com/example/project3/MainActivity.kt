package com.example.project3

import android.os.AsyncTask
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

    private lateinit var recyclerView: RecyclerView
    private lateinit var httpClient: OkHttpClient
    private lateinit var chitChatAdapter: ChitChatAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var newMessageEditText: EditText
    private lateinit var sendButton: Button

    private lateinit var chats: ArrayList<Chat>

    private val chitChatURL = "https://www.stepoutnyc.com/chitchat"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chats = ArrayList()

        chitChatAdapter = ChitChatAdapter(this, chats)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = chitChatAdapter
        httpClient = OkHttpClient()
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener { refresh() }

        newMessageEditText = findViewById(R.id.newMessageEditText)
        sendButton = findViewById(R.id.sendButton)

        sendButton.setOnClickListener {
            if (newMessageEditText.text.toString() != "") {
                post(newMessageEditText.text.toString())
                newMessageEditText.text.clear()
                refresh()
            }
        }

        refresh()
    }

    private fun refresh() {
        val rt = RefreshTask()
        rt.execute()
    }

    private fun post(message: String) {
        val pt = PostMessageTask(message)
        pt.execute()
    }

    private inner class RefreshTask : AsyncTask<Void?, Void?, String>() {
        override fun doInBackground(vararg voids: Void?): String {
            var urlBuilder = chitChatURL.toHttpUrlOrNull()?.newBuilder()
            if (urlBuilder != null) {
                urlBuilder.addQueryParameter("key", "fa070b3c-0685-431d-9fe2-e156bcbcfadb")
                urlBuilder.addQueryParameter("client", "avery.follett@mymail.champlain.edu")
            }
            var getURL = urlBuilder?.build().toString()

            val request: Request = Request.Builder()
                .url(getURL)
                .build()
            try {
                httpClient.newCall(request).execute().use { response -> return response.body!!.string() }
            } catch (e: Exception) {
                e.printStackTrace()
                return "Error: Could not complete request."
            }
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            parseJSON(result)
            chitChatAdapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private inner class PostMessageTask(private var message: String) : AsyncTask<Void?, Void?, String>() {
        override fun doInBackground(vararg voids: Void?): String {

            val requestBody: RequestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("key", "fa070b3c-0685-431d-9fe2-e156bcbcfadb")
                    .addFormDataPart("client", "avery.follett@mymail.champlain.edu")
                    .addFormDataPart("message", message)
                    .build()

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

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

        }
    }

    private fun parseJSON(jsonString: String) {
        chats.clear()
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