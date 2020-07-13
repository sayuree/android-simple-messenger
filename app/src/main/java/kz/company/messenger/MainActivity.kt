package kz.company.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        already_have_an_account_text_view.setOnClickListener{
            val intent = Intent(this, LoginActvity::class.java)
            startActivity(intent)
            Log.d("MainActivity", "Try to navigate")
        }
    }

}
