package kz.company.messenger

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.provider.FontsContractCompat.FontRequestCallback.RESULT_OK
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URI

class RegisterActivity : AppCompatActivity() {
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

        photoframe_button_register.setOnClickListener {
            Log.d("MainActivity","Trying to select photo")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //requestCode == 0 : check to which request we are responding to
        //resultCode == RESULT_OK : make sure the request was successful and the data you wanted is received
        if (requestCode == 0 && data != null && resultCode == RESULT_OK){
            Log.d("Register", "The photo is succesfully selected!")
            val uri:Uri? = data.data
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            photoframe_button_register.setBackgroundDrawable(bitmapDrawable)
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
