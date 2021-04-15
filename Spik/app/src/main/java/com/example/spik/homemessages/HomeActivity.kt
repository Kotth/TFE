package com.example.spik.homemessages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.spik.R
import com.example.spik.models.User
import com.example.spik.modify.DeleteActivity
import com.example.spik.modify.ModifyActivity
import com.example.spik.registerlogin.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.random.Random


class HomeActivity : AppCompatActivity() {

    private val user = FirebaseAuth.getInstance()
    private val uid = user.uid
    private val database = FirebaseDatabase.getInstance("https://spik-app-default-rtdb.europe-west1.firebasedatabase.app/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        checkLogin()

        //Bouton de menu pour l'ouverture du drawer
        menuButton.setOnClickListener {
            menuDrawer.openDrawer(slider)
        }

        plusButton.setOnClickListener {
            newMessage()
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
                    database.getReference("/users/$uid")
                    database.getReference("/users/$uid").child("online").setValue(false)
                    user.signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    Toast.makeText(this, "Déconnecté avec succès", Toast.LENGTH_SHORT).show()
                    true
                }
                "Supprimer le compte" -> {
                    val intent = Intent(this, DeleteActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    menuDrawer.closeDrawer(GravityCompat.START)
                    startActivity(intent)
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
        database.getReference("/users/$uid").child("online").setValue(false)
    }

    override fun onRestart() {
        super.onRestart()
        database.getReference("/users/$uid").child("online").setValue(true)
    }

    private fun checkLogin() {
        if(uid == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            //Si connecté le passe online
            database.getReference("/users/$uid").child("online").setValue(true)
        }
    }

    //Fonction pour changer l'action lorqu'on appuie sur la touche Back du menu de navigation
    override fun onBackPressed() {
        val drawer = menuDrawer
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            // Si le drawer est ouvert -> le ferme
            drawer.closeDrawer(GravityCompat.START)
        } else {
            // Sinon effectue l'action de base du bouton
            super.onBackPressed()
        }
    }

    private fun newMessage() {
        val ref = database.getReference("/users")
        var thisUser = User(null, null, null, false)
        var userList: MutableList<User> = mutableListOf()

        ref.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(users: DataSnapshot) {
                        users.children.forEach{
                            if (it.key == uid)  {
                                thisUser = it.getValue(User::class.java)!!
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@HomeActivity, "Erreur veuillez réessayer", Toast.LENGTH_SHORT).show()
                    }
                }
        )

        ref.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(users: DataSnapshot) {
                        users.children.forEach {
                            if (it.key != uid) {
                                val temp: User? = it.getValue(User::class.java)
                                if(temp!!.online && temp.lang == thisUser.lang) {
                                    userList.add(temp)
                                }
                            }
                        }
                        if(userList.isEmpty()) {
                            Toast.makeText(this@HomeActivity, "Aucune personne trouvée", Toast.LENGTH_SHORT).show()
                        } else {
                            val intent = Intent(this@HomeActivity, MessageActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.putExtra("user", userList.random())
                            startActivity(intent)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@HomeActivity, "Erreur veuillez réessayer", Toast.LENGTH_SHORT).show()
                    }
                }
        )
    }

}

