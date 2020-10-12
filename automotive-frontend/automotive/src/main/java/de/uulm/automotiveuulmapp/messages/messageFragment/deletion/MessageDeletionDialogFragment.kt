package de.uulm.automotiveuulmapp.messages.messageFragment.deletion

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import de.uulm.automotiveuulmapp.R

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
            builder.setMessage(R.string.message_deletion_dialog_content)
                .setPositiveButton(R.string.dialog_confirmation_yes
                ) { _, _ ->
                    onConfirm()
                }
                .setNegativeButton(R.string.dialog_confirmation_no
                ) { _, _ ->
                    onDeny()
                }
            builder.create()
        }
    }
}