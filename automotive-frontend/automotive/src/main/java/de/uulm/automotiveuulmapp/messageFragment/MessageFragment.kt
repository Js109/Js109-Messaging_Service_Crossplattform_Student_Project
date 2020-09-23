package de.uulm.automotiveuulmapp.messageFragment

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.uulm.automotiveuulmapp.BaseFragment
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.messages.messagedb.MessageDatabase
import kotlinx.coroutines.launch

class MessageFragment : BaseFragment() {

    private lateinit var messageAdapter: MessageAdapter

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

        // binding "swipe handler" to recyclerview
        itemTouchHelperCallback.attachToRecyclerView(recyclerView)
        return view
    }

    // used to enable and handle swipe gestures
    private val itemTouchHelperCallback = ItemTouchHelper(
        object : SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // not implemented
                return false
            }

            // is triggered when the element has reached its swipe destination
            // calls a dialogue, if the element should be deleted
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val dialog = MessageDeletionDialogFragment (
                    onConfirm = {
                        messageAdapter.removeMessage(viewHolder.adapterPosition)
                    },
                    onDeny = fun (){
                        // just resets the slided element to its original position
                        messageAdapter.notifyQueryChanged()
                    })
                dialog.show(parentFragmentManager, "MessageViewDeletionDialogue")
            }

            // Simply avoids the swiped element to swipe out of bounds
            // adjust dX to change the distance the element swipes to
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX/10,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
    )
}