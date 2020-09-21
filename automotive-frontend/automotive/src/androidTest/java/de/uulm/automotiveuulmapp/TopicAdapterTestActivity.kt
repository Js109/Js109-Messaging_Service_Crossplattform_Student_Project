package de.uulm.automotiveuulmapp

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Activity for testing the TopicAdapter.
 * Contains only a SearchView and a RecyclerView and provides access to them via public fields.
 */
class TopicAdapterTestActivity() : AppCompatActivity() {
    var recyclerView : RecyclerView? = null
    var searchView : SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_adapter_test)

        recyclerView = findViewById<RecyclerView>(R.id.topicAdapterTestRecyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(applicationContext)
        searchView = findViewById<SearchView>(R.id.topicAdapterTestSearchView)
    }
}