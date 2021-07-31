package tech.intouch.ui.register

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.room.Room
import tech.intouch.messages.LatestMessagesActivity
import tech.intouch.models.User
import tech.intouch.ui.login.LoginActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_register.*
import tech.intouch.R
import tech.intouch.db.AppDatabase
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private var selectedPhotoUri: Uri? = null

    companion object {
        val TAG = RegisterActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.abs_layout)
        supportActionBar?.elevation = 0.0f

        register_button_register.setOnClickListener {
            performRegistration()
        }

        already_have_account_text_view.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.enter, R.anim.exit)
        }

        selectphoto_button_register.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data ?: return
            Log.d(TAG, "Photo was selected")
            // Get and resize profile image
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            contentResolver.query(selectedPhotoUri!!, filePathColumn, null, null, null)?.use {
                it.moveToFirst()
                Picasso.get().load(selectedPhotoUri).into(selectphoto_imageview_register)

            }
            selectphoto_button_register.alpha = 0f
        }
    }

    private fun performRegistration() {
        val name = name_edittext_register.text.toString()

        if (name.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        already_have_account_text_view.visibility = View.GONE
        loading_view.visibility = View.VISIBLE

        val db = AppDatabase.getAppDatabase(applicationContext)

        val self = User(UUID.randomUUID().toString(), name, selectedPhotoUri?.toString())
        db.accountDao().insertAll(self)
    }
}

