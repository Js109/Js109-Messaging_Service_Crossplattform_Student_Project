package de.uulm.automotiveuulmapp.messageFragment

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
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