package de.uulm.automotiveuulmapp.messages.messageFragment

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.PictureDrawable
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.caverock.androidsvg.SVG
import de.uulm.automotiveuulmapp.R
import de.uulm.automotiveuulmapp.messages.MessageContentActivity
import de.uulm.automotiveuulmapp.messages.messagedb.MessageDao
import de.uulm.automotiveuulmapp.messages.messagedb.MessageEntity
import de.uulm.automotiveuulmapp.messages.pruneMessageTags
import java.util.*

class MessageAdapter(
    private val searchView: SearchView,
    private val messageDao: MessageDao,
    activity: Activity?
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messagesLiveData: LiveData<List<MessageEntity>>? = null
    private var messages = emptyList<MessageEntity>()
    private var currentMessages = emptyList<MessageEntity>()

    init {
        AsyncTask.execute {
            messagesLiveData = messageDao.getLiveData()
            activity?.runOnUiThread {
                messagesLiveData?.observeForever {
                    messages = it
                    notifyQueryChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val messageCard =
            LayoutInflater.from(parent.context).inflate(R.layout.message_card, parent, false)
        return MessageViewHolder(messageCard)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = currentMessages[position]

        message.logoAttachment?.let {
            if (it.isNotEmpty()) {
                val logoImage = holder.itemView.findViewById<ImageView>(R.id.message_logo_image)
                val logoString = String(it)
                val svgStringStartIndex = logoString.indexOf("<svg")
                if( svgStringStartIndex != -1){
                    val svgDrawable = parseSvg(logoString.substring(svgStringStartIndex))
                    logoImage.setImageDrawable(svgDrawable)
                } else {
                    logoImage.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                }
                holder.itemView.findViewById<CardView>(R.id.message_logo_image_holder).visibility = View.VISIBLE
            }
        }

        val logo = holder.itemView.findViewById<TextView>(R.id.message_logo_text)
        logo.text = message.sender[0].toString().toUpperCase(Locale.ROOT)

        val title = holder.itemView.findViewById<TextView>(R.id.message_title_text)
        title.text = message.title

        val content = holder.itemView.findViewById<TextView>(R.id.message_content_text)
        content.text = message.messageText?.pruneMessageTags()

        val readSymbol = holder.itemView.findViewById<TextView>(R.id.readSymbol)
        readSymbol.visibility = if (message.read) View.GONE else View.VISIBLE

        val checkbox = holder.itemView.findViewById<CheckBox>(R.id.message_favourite_checkbox)
        checkbox.setOnCheckedChangeListener { _, _ -> }
        checkbox.isChecked = message.favourite
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            AsyncTask.execute {
                messageDao.update(
                    message.apply { favourite = isChecked })
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(searchView.context, MessageContentActivity::class.java)
            intent.putExtra(MessageContentActivity.EXTRA_PERSISTED_MESSAGE_ID, message.uid)
            startActivity(searchView.context, intent, null)
        }
    }

    /**
     * parses svg string to a PictureDrawable
     *
     * @param svgString String of svg image
     * @return SVG as PictureDrawable
     */
    private fun parseSvg(svgString: String): PictureDrawable{
        val svg: SVG = SVG.getFromString(svgString)
        return PictureDrawable(svg.renderToPicture())
    }

    fun removeMessage(position: Int){
        val message = messages[position]
        AsyncTask.execute{
            messageDao.delete(message.uid!!)
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