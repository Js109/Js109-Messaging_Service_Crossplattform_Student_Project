package de.uulm.automotiveuulmapp.messageFragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

/**
 * Created and opened
 *
 * @property onConfirm
 * @property onDeny
 */
class MessageDeletionDialogFragment(
    private val onConfirm: () -> Unit,
    private val onDeny: () -> Unit
): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let{
            val builder = AlertDialog.Builder(activity)
            builder.setMessage("Do you really want to delete this message?")
                .setPositiveButton("Yes"
                ) { _, _ ->
                    onConfirm()
                }
                .setNegativeButton("No"
                ) { _, _ ->
                    onDeny()
                }
            builder.create()
        }
    }
}