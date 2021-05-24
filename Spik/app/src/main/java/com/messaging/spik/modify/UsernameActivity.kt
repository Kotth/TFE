package com.messaging.spik.modify

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.messaging.spik.R
import kotlinx.android.synthetic.main.activity_username.*

class UsernameActivity: AppCompatActivity() {

    // Récupération de l'user et de la database
    private val user = FirebaseAuth.getInstance()
    private val uid = user.uid
    private val database = FirebaseDatabase.getInstance("https://spik-app-default-rtdb.europe-west1.firebasedatabase.app/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username)

        // Renvoi vers la page de modif si clique sur le bouton de retour
        returnEdit.setOnClickListener {
            returnback()
        }

        //Sauvegarde du pseudo au clique sur le bouton de sauvegarde
        saveUsernameButton.setOnClickListener {
            changeUsername()
        }
    }

    // Fonction de changement du pseudo
    private fun changeUsername() {
        // Récupération du pseudo
        val username = usernameChange.text.toString()
        // Changement de langue dans la database
        database.getReference("/users/$uid").child("username").setValue(username)
        Toast.makeText(this, "Pseudo modifié avec succès!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ModifyActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun returnback() {
        //Renvoie vers la page de modif
        val intent = Intent(this, ModifyActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    //Fonction pour changer l'action lorqu'on appuie sur la touche Back du menu de navigation
    override fun onBackPressed() {
        returnback()
    }
}