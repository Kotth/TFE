package com.example.spik.modify

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spik.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_password.*

class PasswordActivity: AppCompatActivity() {

    private val user = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        returnBack.setOnClickListener {
            backModify()
        }

        savePasswordButton.setOnClickListener {
            saveNewPassword()
        }
    }

    private fun saveNewPassword() {
        val password = passwordEdit.text.toString()
        val confirmPassword = passwordEditConfirm.text.toString()

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tout les champs", Toast.LENGTH_SHORT).show()
        } else if (password != confirmPassword) {
            Toast.makeText(this, "Les deux mots de passe doivent correspondre", Toast.LENGTH_SHORT).show()
        } else {
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