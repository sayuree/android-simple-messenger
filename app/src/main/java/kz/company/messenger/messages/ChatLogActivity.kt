package kz.company.messenger.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kz.company.messenger.R

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        supportActionBar?.title = "Dialogue"
    }
}