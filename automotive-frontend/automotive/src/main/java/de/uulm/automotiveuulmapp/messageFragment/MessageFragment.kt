package de.uulm.automotiveuulmapp.messageFragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.uulm.automotiveuulmapp.BaseFragment
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.messages.messagedb.MessageDatabase

class MessageFragment : BaseFragment() {

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = context!!.getSharedPreferences("firstMessage",Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_message, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.message_recycler_view)

        val searchView = view.findViewById<SearchView>(R.id.messageSearch)

        messageAdapter = MessageAdapter(searchView, MessageDatabase.getDaoInstance(recyclerView.context), activity)

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

        // used to enable and handle swipe gestures
        val itemTouchHelper = ItemTouchHelper(
            MessageSimpleCallback(
                {itemPos ->  messageAdapter.removeMessage(itemPos)},
                {messageAdapter.notifyQueryChanged()},  // just resets the slided element to its original position
                parentFragmentManager)
        )
        // binding "swipe handler" to recyclerview
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // check if the hint overlay should be displayed
        if(checkHintRequirement()){
            this.startActivity(Intent(context, MessageDeletionHelper::class.java))
        }
        return view
    }

    /**
     * Checks for a flag in the shared preferences if the hint view should be displayed
     * This is required at the first time a user comes to the message view and at least one message is
     * already saved.
     *
     * @return If hint is required
     */
    private fun checkHintRequirement(): Boolean{
        var hintRequired: Boolean = false
        val showMessageDeletionHint = sharedPreferences!!.getBoolean("showMessageDeletionHint", false)
        if(sharedPreferences != null && showMessageDeletionHint){
            with(sharedPreferences!!.edit()){
                putBoolean("showMessageDeletionHint", false)
                hintRequired = true
                commit()
            }
        }
        return hintRequired
    }
}