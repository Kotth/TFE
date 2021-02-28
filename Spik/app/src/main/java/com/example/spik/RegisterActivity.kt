package com.example.spik


import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class RegisterActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val adapter: ArrayAdapter<*> =
            ArrayAdapter.createFromResource(this, R.array.languages_available, R.layout.spinner_item)
        langSpinner.adapter = adapter

        registerButton.setOnClickListener {
            register()
        }

    }

    private fun register() {

            val email = emailAddressRegister.text.toString()
            val password = passwordRegister.text.toString()
        val pseudo = pseudoRegister.text.toString()

            if(email.isEmpty() || password.isEmpty() || pseudo.isEmpty()) {
                Toast.makeText(this, "Pseudo, Email ou mot de passe vide", Toast.LENGTH_SHORT).show()
                return
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener

                    pushUserToFirebase()

                    Toast.makeText(this, "Compte créé avec succès", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erreur lors de la création du compte: $it.message", Toast.LENGTH_SHORT).show()
                }
    }


    private fun pushUserToFirebase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val database = FirebaseDatabase.getInstance("https://spik-app-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/users/$uid")

        val user = User(uid, pseudoRegister.text.toString(), langSpinner.selectedItem.toString())

        database.setValue(user)

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}

class User(val uid: String, val username: String, val lang: String)