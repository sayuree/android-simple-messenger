package kz.company.messenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActvity:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        logInUser()

        back_to_register_text_view.setOnClickListener {
            finish()
        }
    }

    private fun logInUser() {
        val email = username_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()
        if (email.isEmpty() || password.isEmpty())
            Toast.makeText(this,"Please enter email and password!", Toast.LENGTH_SHORT).show()
        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            Log.d("LoginActivity", "Sucessfully logged in!")
        }.addOnFailureListener {
            Log.d("LoginActivity", "Failed to log in ${it.message}")
        }
    }

}