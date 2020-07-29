package kz.company.messenger.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*
import kz.company.messenger.R
import kz.company.messenger.registerlogin.User

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"
        val adapter = GroupAdapter<GroupieViewHolder>()
        recyclerView.adapter = adapter
//        adapter.add(UserItem())
        fetchUserData()
    }

    private fun fetchUserData(){
        val ref  = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            val adapter = GroupAdapter<GroupieViewHolder>()
            override fun onCancelled(error: DatabaseError) {
                Log.d("NewMessages", "Reading data is not permitted: $error")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    Log.d("NewMessagesActivity", it.toString())
                    val user = it.getValue(User::class.java)
                    if(user != null) {
                        adapter.add(UserItem(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val intent = Intent(this@NewMessageActivity, ChatLogActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                recyclerView.adapter = adapter
            }

        })
    }
}

class UserItem(val user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.userName_new_message.text = user.username
        Picasso.get().load(user.photoURL).into(viewHolder.itemView.account_photo_new_message)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}