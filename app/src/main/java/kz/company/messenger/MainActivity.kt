package kz.company.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        already_have_an_account_text_view.setOnClickListener{
            val intent = Intent(this, LoginActvity::class.java)
            startActivity(intent)
            Log.d("MainActivity", "Try to navigate to login page")
        }

        register_button_register.setOnClickListener{
            register_users()
        }
    }

    private fun  register_users(){
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        // Base Case: Avoiding the crush of an app in case of empty strings of password and email
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Please, fill Email and Password fields!", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity", "Email: $email")
        Log.d("MainActivity", "Password: $password")
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                // Case 1: Registration is failed
                if(!it.isSuccessful) {
                    Log.d("MainActivity", "Registration failed")
                    return@addOnCompleteListener
                }
                // Case 2: Registration is successful
                Log.d("MainActivity","Successfully registered user with uid:${it.result?.user?.uid}")
            }.addOnFailureListener {
                Log.d("MainActivity", "Failed to register a user:${it.message}")
            }
    }

}
