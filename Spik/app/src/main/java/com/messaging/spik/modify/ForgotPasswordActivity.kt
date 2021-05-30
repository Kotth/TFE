package com.messaging.spik.modify

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.messaging.spik.R
import com.messaging.spik.registerlogin.LoginActivity
import kotlinx.android.synthetic.main.activity_passwordforgot.*

class ForgotPasswordActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passwordforgot)

        forgotPasswordButton.setOnClickListener {
            reinitialize()
        }
    }

    private fun reinitialize() {
        val emailAddress = emailForgot.text.toString()

        if (emailAddress.isEmpty()) {
            Toast.makeText(
                this,
                "Veuillez indiquer une adresse email correcte.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Firebase.auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onBackPressed()
                        Toast.makeText(
                            this,
                            "Email pour réinitialiser votre mot de passe envoyé.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
        }
    }

    //Fonction pour changer l'action lorqu'on appuie sur la touche Back du menu de navigation
    override fun onBackPressed() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}