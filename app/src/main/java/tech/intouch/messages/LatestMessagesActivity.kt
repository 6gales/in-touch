package tech.intouch.messages

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import tech.intouch.messages.NewMessageActivity.Companion.USER_KEY
import tech.intouch.models.ChatMessage
import tech.intouch.models.User
import tech.intouch.ui.register.RegisterActivity
import tech.intouch.views.LatestMessageRow
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
import tech.intouch.R
import tech.intouch.db.AppDatabase


class LatestMessagesActivity : AppCompatActivity() {

    private val adapter = GroupAdapter<ViewHolder>()
    private val latestMessagesMap = HashMap<String, ChatMessage>()


    companion object {
        var currentUser: User? = null
        val TAG = LatestMessagesActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        recyclerview_latest_messages.adapter = adapter

        swiperefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent))

        fetchCurrentUser()
        listenForLatestMessages()

        adapter.setOnItemClickListener { item, _ ->
            val intent = Intent(this, ChatLogActivity::class.java)
            val row = item as LatestMessageRow
            intent.putExtra(USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }


        new_message_fab.setOnClickListener {
            val intent = Intent(this, NewMessageActivity::class.java)
            startActivity(intent)
        }

        swiperefresh.setOnRefreshListener {
            fetchCurrentUser()
            listenForLatestMessages()
        }
    }

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it, this))
        }
        swiperefresh.isRefreshing = false
    }

    private fun listenForLatestMessages() {
        swiperefresh.isRefreshing = true
        //TODO add listener callback
    }

    private fun fetchCurrentUser() {
        val db = AppDatabase.getAppDatabase(applicationContext)
        val self = db.accountDao().getAll().firstOrNull()
        if (self == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        //TODO set self account into wifi listener
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            R.id.menu_sign_out -> {
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }


}
