package com.messaging.spik.registerlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.messaging.spik.homemessages.HomeActivity
import com.messaging.spik.R
import com.google.firebase.auth.FirebaseAuth
import com.messaging.spik.modify.ForgotPasswordActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Actions du bouton de connexion
        connexionButton.setOnClickListener {
            connexion()
        }

        //Bouton pour ouvrir la page d'inscription
        textRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        forgotText.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }


    private fun connexion() {
        val email = emailAddressConnexion.text.toString()
        val password = passwordConnexion.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener

                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                    Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Impossible de se connecter: $it.message", Toast.LENGTH_SHORT).show()
                }
        }  else {
            Toast.makeText(this, "Veuillez saisir des informations correctes", Toast.LENGTH_SHORT).show()
        }

    }
}