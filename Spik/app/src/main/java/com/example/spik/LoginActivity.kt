package com.example.spik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //firebase login
        connexionButton.setOnClickListener {
            connexion()
        }

        //open register page
        textRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null){
            reload();
        }
    }


    private fun connexion() {
        val email = emailAddressConnexion.text.toString()
        val password = passwordConnexion.text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener

                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

                Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.d("LoginActivity", "Erreur ici")
                Toast.makeText(this, "Impossible de se connecter: $it.message", Toast.LENGTH_SHORT).show()
            }
    }

    private fun reload() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}