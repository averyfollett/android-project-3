package com.example.project3

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.lang.Exception
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var httpClient: OkHttpClient
    private lateinit var chitChatAdapter: ChitChatAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val chitChatURL = "https://www.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chitChatAdapter = ChitChatAdapter(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = chitChatAdapter
        httpClient = OkHttpClient()
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener { refresh() }
        refresh()
    }


    private fun refresh() {
        val rt = RefreshTask()
        rt.execute()
    }


    private inner class RefreshTask : AsyncTask<Void?, Void?, String>() {
        override fun doInBackground(vararg voids: Void?): String {
            val request: Request = Request.Builder()
                .url(chitChatURL)
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
            //Log.d(RESPONSE_TAG, result)
            parseJSON(result)
            chitChatAdapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }
    }



    private fun parseJSON(jsonString: String) {
        try {
            val responseObject = JSONObject(jsonString)
            val items = responseObject.getJSONArray("items")
            for (i in 0 until items.length()) {
                val item = items.getJSONObject(i)
                val media = item.getJSONObject("media")
                val urlString = media.getString("m")
                //mPhotosURLs!!.add(0, urlString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}