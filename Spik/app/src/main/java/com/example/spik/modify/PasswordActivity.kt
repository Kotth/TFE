package com.example.spik.modify

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spik.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_password.*

class PasswordActivity: AppCompatActivity() {

    //récupération de l'user
    private val user = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        // Renvoie vers la page de modif si clique sur le botuon de retour
        returnBack.setOnClickListener {
            backModify()
        }

        // Bouton de sauvegarde du nouveau mot de passe
        savePasswordButton.setOnClickListener {
            saveNewPassword()
        }
    }

    // Fonction de sauvegarde du nouveau mot de passe
    private fun saveNewPassword() {
        val password = passwordEdit.text.toString()
        val confirmPassword = passwordEditConfirm.text.toString()

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            // Si un champ est vide
            Toast.makeText(this, "Veuillez remplir tout les champs", Toast.LENGTH_SHORT).show()
        } else if (password != confirmPassword) {
            // si mots de passe différents
            Toast.makeText(this, "Les deux mots de passe doivent correspondre", Toast.LENGTH_SHORT).show()
        } else {
            // Changement de mot de passe
            user.currentUser!!.updatePassword(password)
                    .addOnCompleteListener {
                        val intent = Intent(this, ModifyActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        Toast.makeText(this, "Votre mot de passe a été modifié avec succès", Toast.LENGTH_SHORT).show()
                    }
        }
    }

    private fun backModify() {
        //Renvoie vers la page de modif
        val intent = Intent(this, ModifyActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    //Fonction pour changer l'action lorqu'on appuie sur la touche Back du menu de navigation
    override fun onBackPressed() {
        backModify()
    }

}