package com.example.spik.homemessages

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.spik.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_message.*
import com.xwray.groupie.*

class MessageActivity: AppCompatActivity() {

    private val user = FirebaseAuth.getInstance()
    private val uid = user.uid
    private val database = FirebaseDatabase.getInstance("https://spik-app-default-rtdb.europe-west1.firebasedatabase.app/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val adapter = GroupAdapter<GroupieViewHolder>()

        backHomeButton.setOnClickListener{
            backHome()
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

class MessageItem: Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getLayout(): Int {
        TODO("Not yet implemented")
    }
}