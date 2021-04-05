package com.example.spik

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    private val user = FirebaseAuth.getInstance()
    private val uid = FirebaseAuth.getInstance().uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        checkLogin()

        //Bouton de menu pour l'ouverture du drawer
        menuButton.setOnClickListener {
            menuDrawer.openDrawer(slider)
        }

        slider.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.toString()) {
                "Modifier le profil" -> {
                    val intent = Intent(this, ModifyActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    menuDrawer.closeDrawer(GravityCompat.START)
                    startActivity(intent)
                    true
                }
                "Déconnexion" -> {
                    val database = FirebaseDatabase.getInstance("https://spik-app-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/users/$uid")
                    database.child("online").setValue(false)
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

    override fun onStop() {
        super.onStop()
        val database = FirebaseDatabase.getInstance("https://spik-app-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/users/$uid")
        database.child("online").setValue(false)
    }

    override fun onRestart() {
        super.onRestart()
        val database = FirebaseDatabase.getInstance("https://spik-app-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/users/$uid")
        database.child("online").setValue(true)
    }

    private fun checkLogin() {
        if(uid == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            //Si connecté le passe online
            val database = FirebaseDatabase.getInstance("https://spik-app-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/users/$uid")
            database.child("online").setValue(true)
        }
    }

    //Fonction pour changer l'action lorqu'on appui sur la touche Back du menu de navigation
    override fun onBackPressed() {
        val drawer = menuDrawer
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            // Si le drawer est ouvert -> le ferm
            drawer.closeDrawer(GravityCompat.START)
        } else {
            // Sinon effectue l'action de base du bouton
            super.onBackPressed()
        }
    }


}