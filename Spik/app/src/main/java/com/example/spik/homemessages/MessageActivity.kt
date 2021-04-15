package com.example.spik.homemessages

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.spik.R
import com.example.spik.models.Message
import com.example.spik.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_message.*
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.left_row.view.*
import kotlinx.android.synthetic.main.right_row.view.*

class MessageActivity: AppCompatActivity() {

    private val user = FirebaseAuth.getInstance()
    private val uid = user.uid
    private val database = FirebaseDatabase.getInstance("https://spik-app-default-rtdb.europe-west1.firebasedatabase.app/")
    private val toUser = intent.getParcelableExtra<User>("user")
    private val toUid = toUser!!.uid
    private val ref = database.getReference("/messages/$uid/$toUid")
    private val messagesId: String? = ref.key
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        recyclerviewMessage.adapter = adapter

        getMessage()

        sendButton.setOnClickListener {
            sendMessage()
        }

        backHomeButton.setOnClickListener{
            backHome()
        }
    }

    private fun getMessage() {
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)

                if (message!!.fromId == user.uid) {
                    adapter.add(MessageToItem(message.text))
                } else {
                    adapter.add(MessageFromItem(message.text))
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }

    private fun sendMessage() {
        val text = editTextMessage.text.toString()
        val message = Message(messagesId!!, text, uid!!, toUser!!.uid!!)
        val refTwo = database.getReference("/messages/$toUid/$uid")
        ref.setValue(message)
                .addOnSuccessListener {
                    editTextMessage.text.clear()
                    recyclerviewMessage.scrollToPosition(adapter.itemCount -1)
                }
        refTwo.setValue(message)
                .addOnSuccessListener {
                    editTextMessage.text.clear()
                    recyclerviewMessage.scrollToPosition(adapter.itemCount -1)
                }
    }

    private fun backHome() {
        //Renvoie vers la page d'accueil
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onBackPressed() {
        backHome()
    }
}

class MessageFromItem(val text: String): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textViewFrom
    }

    override fun getLayout(): Int {
        return R.layout.left_row
    }
}

class MessageToItem(val text: String): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textViewTo
    }

    override fun getLayout(): Int {
        return R.layout.right_row
    }
}