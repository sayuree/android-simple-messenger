package kz.company.messenger.registerlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kz.company.messenger.R
import kz.company.messenger.messages.LatestMessagesActivity

class LoginActvity:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d("LoginActivity", "Successfully navigated!")
        back_to_register_text_view.setOnClickListener {
            finish()
        }

        login_button.setOnClickListener {
            logInUser()
        }
    }

    private fun logInUser() {
        val email = username_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password!", Toast.LENGTH_SHORT).show()
            return
        }
        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            Log.d("LoginActivity", "Sucessfully logged in!")
            Toast.makeText(this, "Logged in!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LatestMessagesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            Log.d("LoginActivity", "Failed to log in ${it.message}")
        }
    }

}