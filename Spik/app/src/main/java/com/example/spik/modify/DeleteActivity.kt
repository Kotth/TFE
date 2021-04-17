package com.example.spik.modify

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spik.homemessages.HomeActivity
import com.example.spik.R
import com.example.spik.registerlogin.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_delete.*

class DeleteActivity : AppCompatActivity() {

    // Récupération de l'user et de la database
    private val user = FirebaseAuth.getInstance()
    private val uid = user.uid
    private val database = FirebaseDatabase.getInstance("https://spik-app-default-rtdb.europe-west1.firebasedatabase.app/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete)

        // Renvoi vers la page d'acceuil si clique sur le bouton retour
        returnBackHome.setOnClickListener {
            back()
        }

        // Suppression du compte qi clique sur le bouton
        deleteButton.setOnClickListener {
            delete()
        }
    }

    private fun back() {
        //Renvoie vers la page d'accueil
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    //Fonction pour changer l'action lorqu'on appuie sur la touche Back du menu de navigation
    override fun onBackPressed() {
        back()
    }

    private fun delete() {
        // Suppression du compte
        user.currentUser!!.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Si réussite
                        database.getReference("/users/$uid").setValue(null)
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        Toast.makeText(this, "Compte supprimé avec succès", Toast.LENGTH_SHORT).show()
                    } else {
                        //Si erreur
                        Toast.makeText(this, "Erreur lors de la suppression du compte", Toast.LENGTH_SHORT).show()
                    }
                }
    }

}