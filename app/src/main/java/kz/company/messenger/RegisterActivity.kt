package kz.company.messenger

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

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
            registerUsers()
        }

        photoframe_button_register.setOnClickListener {
            Log.d("MainActivity","Trying to select photo")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }
    var selectedPhotoUri:Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //requestCode == 0 : check to which request we are responding to
        //resultCode == RESULT_OK : make sure the request was successful and the data you wanted is received
        if (requestCode == 0 && data != null && resultCode == RESULT_OK){
            Log.d("Register", "The photo is succesfully selected!")
            selectedPhotoUri = data.data
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            photoframe_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun uploadImageToFirebaseStorage(){
        // generating random string for file name
        if (selectedPhotoUri == null) return
        val filename:String = UUID.randomUUID().toString()
        val storage = Firebase.storage
        val reference = storage.getReference("/images/$filename")
        reference.putFile(selectedPhotoUri!!)
            .addOnCompleteListener{
                Log.d("Register","Successfully uploaded photo into Firebase Storage!")
                reference.downloadUrl.addOnSuccessListener {
                    Log.d("Register", "URL for a file: $it")
                    saveUserDataInFirestore(it.toString())
                }
            }

    }

    private fun saveUserDataInFirestore(userPhotoURL:String){
        if(userPhotoURL == null) return
        val database = Firebase.database
        val uid = Firebase.auth.uid
        val username = username_edittext_register.text.toString()
        val user = User(uid.toString(), userPhotoURL, username)
        val ref = database.getReference("/users/$uid")
        ref.setValue(user)
        Log.d("Register", "Successfully added user data to Firebase Database")

    }
    private fun  registerUsers(){
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
                uploadImageToFirebaseStorage()
            }.addOnFailureListener {
                Log.d("MainActivity", "Failed to register a user:${it.message}")
            }
    }

}

class User(val uid:String, val photoURL:String, val username:String)