package tech.intouch.views

import android.content.Context
import tech.intouch.models.ChatMessage
import tech.intouch.models.User
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_message_row.view.*
import tech.intouch.R
import tech.intouch.db.AppDatabase

class LatestMessageRow(val chatMessage: ChatMessage, val context: Context) : Item<ViewHolder>() {

    var chatPartnerUser: User? = null

    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.latest_message_textview.text = chatMessage.text

        val chatPartnerId: String

        val db = AppDatabase.getAppDatabase(context)

    }

}