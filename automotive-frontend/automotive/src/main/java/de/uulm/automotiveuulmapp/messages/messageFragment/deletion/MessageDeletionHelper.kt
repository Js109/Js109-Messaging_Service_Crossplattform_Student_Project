package de.uulm.automotiveuulmapp.messages.messageFragment.deletion

import android.app.Activity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import de.uulm.automotiveuulmapp.R

class MessageDeletionHelper : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_deletion_helper)
        findViewById<ConstraintLayout>(R.id.message_delete_helper_layout).setOnClickListener{
            this.finish()
        }
    }
}