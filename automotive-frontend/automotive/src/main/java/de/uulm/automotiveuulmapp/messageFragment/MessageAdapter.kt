package de.uulm.automotiveuulmapp.messageFragment

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.SearchView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import de.uulm.automotive.cds.entities.MessageSerializable
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.messages.MessageContentActivity
import de.uulm.automotiveuulmapp.messages.messagedb.MessageDao
import de.uulm.automotiveuulmapp.messages.messagedb.MessageEntity

class MessageAdapter(private val searchView: SearchView, private val messageDao: MessageDao, activity: Activity?) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messages = emptyList<MessageEntity>()
    private var currentMessages = emptyList<MessageEntity>()

    init {
        AsyncTask.execute{
            messages = messageDao.getAll()
            activity?.runOnUiThread {
                notifyQueryChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val messageCard = LayoutInflater.from(parent.context).inflate(R.layout.message_card, parent, false)
        return MessageViewHolder(messageCard)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = currentMessages[position]

        val logo = holder.itemView.findViewById<TextView>(R.id.message_logo_text)
        logo.text = message.sender[0].toString().toUpperCase()

        val title = holder.itemView.findViewById<TextView>(R.id.message_title_text)
        title.text = message.title

        val content = holder.itemView.findViewById<TextView>(R.id.message_content_text)
        content.text = message.messageText

        val checkbox = holder.itemView.findViewById<CheckBox>(R.id.message_favourite_checkbox)
        checkbox.setOnCheckedChangeListener { _, _ ->  }
        checkbox.isChecked = message.favourite
        checkbox.setOnCheckedChangeListener { _, isChecked ->  AsyncTask.execute { messageDao.update(message.apply { favourite = isChecked }) } }

        holder.itemView.setOnClickListener {
            val intent = Intent(searchView.context, MessageContentActivity::class.java)
            intent.putExtra(MessageContentActivity.EXTRA_MESSAGE, MessageSerializable(message.sender, message.title, message.messageText, message.attachment, message.links, null))
            startActivity(searchView.context, intent, null)
        }
    }

    fun notifyQueryChanged() {
        currentMessages = MessageFilter.filter(messages, searchView.query.toString())
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return currentMessages.size
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}