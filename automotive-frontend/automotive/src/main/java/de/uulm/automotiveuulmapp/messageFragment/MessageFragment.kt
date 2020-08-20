package de.uulm.automotiveuulmapp.messageFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.uulm.automotiveuulmapp.BaseFragment
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.messages.messagedb.MessageDao
import de.uulm.automotiveuulmapp.messages.messagedb.MessageDatabase

class MessageFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_message, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.message_recycler_view)

        val searchView = view.findViewById<SearchView>(R.id.messageSearch)

        val messageAdapter = MessageAdapter(searchView, MessageDatabase.getDatabaseInstance(recyclerView.context).messageDao(), activity)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context).apply { reverseLayout = true}
            adapter = messageAdapter
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                messageAdapter.notifyQueryChanged()
                return true
            }
        })

        return view
    }

}