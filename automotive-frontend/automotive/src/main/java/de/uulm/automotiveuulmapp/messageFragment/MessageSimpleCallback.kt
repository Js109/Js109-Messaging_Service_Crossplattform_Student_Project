package de.uulm.automotiveuulmapp.messageFragment

import android.graphics.Canvas
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class MessageSimpleCallback(
    private val onDeleteConfirm: (itemPos: Int) -> Unit,
    private val onDeleteCancel: () -> Unit,
    private val fragmentManager: FragmentManager
): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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
            onConfirm = { onDeleteConfirm(viewHolder.adapterPosition) },
            onDeny = onDeleteCancel)
        dialog.show(fragmentManager, "MessageViewDeletionDialogue")
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