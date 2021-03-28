package com.example.spik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val user = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Bouton de menu pour l'ouverture du drawer
        menuButton.setOnClickListener {
            menuDrawer.openDrawer(slider)
        }

        slider.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.toString()) {
                 "Modifier le profil" -> {
                    Log.d("HomeActivity", "1")
                    true
                }
                "Déconnexion" -> {
                    user.signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    Toast.makeText(this, "Déconnecté avec succès", Toast.LENGTH_SHORT).show()
                    true
                }
                "Supprimer le compte" -> {
                    Log.d("HomeActivity", "3")
                    true
                }
                else -> {
                    true
                }
            }
        }

    }

}