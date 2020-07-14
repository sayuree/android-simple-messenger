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
            Log.d("MainActivity", "Try to navigate to login page")
        }

        register_button_register.setOnClickListener{
            val email = email_edittext_register.text.toString()
            val password = password_edittext_register.text.toString()

            Log.d("MainActivity", "Email: $email")
            Log.d("MainActivity", "Password: $password")

        }
    }

}
