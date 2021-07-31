package tech.intouch.messages

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import tech.intouch.models.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import tech.intouch.R

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = ChatLogActivity::class.java.simpleName
    }

    val adapter = GroupAdapter<ViewHolder>()

    private val toUser: User
        get() = intent.getParcelableExtra(NewMessageActivity.USER_KEY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        swiperefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent))

        recyclerview_chat_log.adapter = adapter

        supportActionBar?.title = toUser.name

        listenForMessages()

        send_button_chat_log.setOnClickListener {
            performSendMessage()
        }
    }

    private fun listenForMessages() {
        swiperefresh.isEnabled = true
        swiperefresh.isRefreshing = true
        //TODO
    }

    private fun performSendMessage() {
        val text = edittext_chat_log.text.toString()
        if (text.isEmpty()) {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        //TODO
    }

}

class ChatFromItem(val text: String, val user: User, val timestamp: Long) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.textview_from_row.text = text
//        viewHolder.itemView.from_msg_time.text = getFormattedTimeChatLog(timestamp)

        val targetImageView = viewHolder.itemView.imageview_chat_from_row

        if (!user.profileImageUrl!!.isEmpty()) {

            val requestOptions = RequestOptions().placeholder(R.drawable.no_image2)


            Glide.with(targetImageView.context)
                    .load(user.profileImageUrl)
                    .thumbnail(0.1f)
                    .apply(requestOptions)
                    .into(targetImageView)

        }
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

}

class ChatToItem(val text: String, val user: User, val timestamp: Long) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = text
//        viewHolder.itemView.to_msg_time.text = getFormattedTimeChatLog(timestamp)

        val targetImageView = viewHolder.itemView.imageview_chat_to_row

        if (!user.profileImageUrl!!.isEmpty()) {

            val requestOptions = RequestOptions().placeholder(R.drawable.no_image2)

            Glide.with(targetImageView.context)
                    .load(user.profileImageUrl)
                    .thumbnail(0.1f)
                    .apply(requestOptions)
                    .into(targetImageView)

        }
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

}


