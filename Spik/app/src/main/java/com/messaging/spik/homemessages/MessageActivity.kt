package com.messaging.spik.homemessages

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.messaging.spik.R
import com.messaging.spik.models.Message
import com.messaging.spik.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_message.*
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.left_row.view.*
import kotlinx.android.synthetic.main.right_row.view.*

class MessageActivity: AppCompatActivity() {

    // Récupération des instances et des valeurs globales
    private val user = FirebaseAuth.getInstance()
    private val uid = user.uid
    private val database = FirebaseDatabase.getInstance("https://spik-app-default-rtdb.europe-west1.firebasedatabase.app/")
    private val adapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var toUser: User
    private lateinit var toUid: String
    private lateinit var reference: DatabaseReference
    private lateinit var messagesId: String
    private var check = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        // Récupération de l'user renvoyer lors de la sélection
        toUser = intent.getParcelableExtra("USER")!!
        toUid = toUser.uid.toString()
        reference = database.getReference("/messages/$uid/$toUid")
        recyclerviewMessage.adapter = adapter

        // Listener de messages
        getMessage()

        // Vérification si l'utilisateur quitte la conversation
        checkConnexion()

        // Envoi du message au clique
        sendButton.setOnClickListener {
            sendMessage()
        }

        // Renvoie vers la page d'acceuil au clique
        backHomeButton.setOnClickListener{
            backHome()
        }
    }

    //Fonction de vérification de la connexion entre utilisateurs
    private fun checkConnexion() {
        //listener sur l'user connecté
        database.getReference("/users/$toUid").child("connectedTo").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val toUid = snapshot.getValue(String::class.java)
                val username = toUser.username
                // Si connectedTo est un string vide --> fin de la conversation
                if (toUid == "") {
                    if (check) {
                        Toast.makeText(this@MessageActivity, "Discussion terminée par $username", Toast.LENGTH_SHORT).show()
                    }
                    check = false
                    backHome()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // Listener de nouveaux messages
    private fun getMessage() {
        reference.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    if (message.fromId == uid) {
                        adapter.add(MessageToItem(message.text))
                    } else {
                        adapter.add(MessageFromItem(message.text))
                    }
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

    //Envoi d'un nouveau message
    private fun sendMessage() {
        val ref = database.getReference("/messages/$uid/$toUid").push()
        val refTwo = database.getReference("/messages/$toUid/$uid").push()
        messagesId = ref.key.toString()
        val text = editTextMessage.text.toString()
        val message = Message(messagesId, text, uid!!, toUser.uid!!)

        //Push vers 2 parties de la db pour rendre lisible par les 2 utilisateurs
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

    // Retour vers l'acceuil
    private fun backHome() {
        val username = toUser.username
        //Suppression de tout les messages
        if(check) {
            database.getReference("/messages/$uid/$toUid").setValue(null)
            database.getReference("/messages/$toUid/$uid").setValue(null)
            database.getReference("/users/$uid").child("connectedTo").setValue("")
            database.getReference("/users/$toUid").child("connectedTo").setValue("")
            Toast.makeText(this@MessageActivity, "Discussion avec $username terminée", Toast.LENGTH_SHORT).show()
            check = false
        }
        //Remise de l'utilisateur online
        database.getReference("/users/$uid").child("online").setValue(true)
        //Renvoie vers la page d'accueil
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    // Si on presse le bouton de retour de la navbar --> Retour vers l'acceuil
    override fun onBackPressed() {
        backHome()
    }
}

//Affichage des messages arrivants
class MessageFromItem(val text: String): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textViewFrom.text = text
    }

    override fun getLayout(): Int {
        return R.layout.left_row
    }
}

//Affichage des messages envoyés
class MessageToItem(val text: String): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textViewTo.text = text
    }

    override fun getLayout(): Int {
        return R.layout.right_row
    }
}