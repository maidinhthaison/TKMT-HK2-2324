package com.mdts.eieapp.presentation.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.mdts.eieapp.R
import com.mdts.eieapp.config.Role
import com.mdts.eieapp.presentation.chat.Message

internal class MessageAdapter (private val messages: List<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    var onClicked: ((ListMessageUIEvent) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageTextView.text = message.content
        holder.messageTextView.setOnClickListener {
            onClicked?.invoke(ListMessageUIEvent.OnItemClicked(message = message))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (messages[position].role) {
            Role.USER.role -> R.layout.user_message
            Role.ASSISTANT.role -> R.layout.assistant_message
            else -> throw IllegalArgumentException("Invalid role")
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: MaterialTextView = itemView.findViewById(R.id.messageTextView)
    }

}
sealed class ListMessageUIEvent() {
    data class OnItemClicked(val message: Message) : ListMessageUIEvent()
}


