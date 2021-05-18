package com.messaging.spik.homemessages

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.messaging.spik.R
import com.messaging.spik.models.User
import com.messaging.spik.modify.DeleteActivity
import com.messaging.spik.modify.ModifyActivity
import com.messaging.spik.registerlogin.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    // Récupération des instances de l'user et de la database
    private val user = FirebaseAuth.getInstance()
    private val uid = user.uid
    private val database = FirebaseDatabase.getInstance("https://spik-app-default-rtdb.europe-west1.firebasedatabase.app/")
    private lateinit var thisUser: User
    private val ref = database.getReference("/users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Vérification si l'utilisateur est connecté
        checkLogin()

        // Si connecté, verifié si personne ne lance de conversation avec
        if(uid != null) {
            checkConnexion()
        }

        //Bouton de menu pour l'ouverture du drawer
        menuButton.setOnClickListener {
            menuDrawer.openDrawer(slider)
        }

        // Lancement d'une conversation
        plusButton.setOnClickListener {
            newMessage()
        }

        // Création du drawerMenu
        slider.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.toString()) {
                //Envoie vers la page de modif du profil
                "Modifier le profil" -> {
                    val intent = Intent(this, ModifyActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    menuDrawer.closeDrawer(GravityCompat.START)
                    startActivity(intent)
                    true
                }
                // Déconnexion
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
                //Suppression du compte
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

    private fun loadUser() {
        //Récupération des donnés de l'utilisateur local
        ref.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(users: DataSnapshot) {
                    users.children.forEach {
                        if (it.key == uid) {
                            thisUser = it.getValue(User::class.java)!!
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            }
        )
    }

    //Listener pour vérifier si on reçoit des demandes de connexion
    private fun checkConnexion() {
        database.getReference("/users/$uid").child("connectedTo").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val toUid = snapshot.getValue(String::class.java)
                if (toUid != "") {
                    //Si connectedTo non vide alors ojn cherche à se connecter
                    database.getReference("/users").child(toUid!!).addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            // Récupération de l'utilisateur avec qui se connecter
                            val toUser = snapshot.getValue(User::class.java)
                            val username = toUser!!.username
                            database.getReference("/users/$toUid").child("connectedTo").setValue(uid)
                            database.getReference("/users/$uid").child("online").setValue(false)
                            //lancer la page de messages avec l'utilisateur concerné
                            val intent = Intent(this@HomeActivity, MessageActivity::class.java)
                            intent.putExtra("USER", toUser)
                            startActivity(intent)
                            Toast.makeText(this@HomeActivity, "Discussion lancée avec $username", Toast.LENGTH_SHORT).show()
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // Si on quitte l'appli passe l'utilisateur en offline
    override fun onStop() {
        super.onStop()
        database.getReference("/users/$uid").child("online").setValue(false)
    }

    //Au redémarrage la passe online
    override fun onRestart() {
        super.onRestart()
        database.getReference("/users/$uid").child("online").setValue(true)
    }

    // Verification si un user est connecté à l'appli
    private fun checkLogin() {
        if(uid == null) {
            //Si pas connecté renvoie vers la page de login
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            //Si connecté le passe online et charge les textes
            database.getReference("/users/$uid").child("online").setValue(true)
            loadUser()
            searchOnlineUsers()
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

    // Recherche d'une conversation
    private fun newMessage() {
        var userList: MutableList<User> = mutableListOf()

        // Recherche dans tout les utilisateurs des utilisateurs compatibles
        ref.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(users: DataSnapshot) {
                    users.children.forEach {
                        if (it.key != uid && it.key != "null") {
                            val temp: User? = it.getValue(User::class.java)
                            if (temp!!.online && temp.lang == thisUser.lang) {
                                //Si utilisateur trouvé, on le met dans la liste
                                userList.add(temp)
                            }
                        }
                    }
                    if (userList.isEmpty()) {
                        // Si la liste est vide, impossible de créer une conversation
                        Toast.makeText(
                            this@HomeActivity,
                            "Aucun utilisateur disponible",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        //Récupération d'un utilisateur aléatoire
                        val toUser = userList.random()
                        val toUid = toUser.uid
                        //Lancement de la conversation et de la connexion
                        database.getReference("/users/$toUid").child("connectedTo").setValue(uid)
                        val intent = Intent(this@HomeActivity, MessageActivity::class.java)
                        intent.putExtra("USER", toUser)
                        startActivity(intent)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            }
        )
    }

    //Recherche d'utilisateurs en ligne
    private fun searchOnlineUsers() {
        var userList: MutableList<User> = mutableListOf()
        var langList: MutableList<User> = mutableListOf()

        // Recherche dans tout les utilisateurs des utilisateurs compatibles
        ref.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(users: DataSnapshot) {
                    users.children.forEach {
                        if (it.key != uid && it.key != "null") {
                            val temp: User? = it.getValue(User::class.java)
                            //Si utilisateur trouvé, on le met dans la liste
                            if (temp!!.online) {
                                userList.add(temp)
                                if (temp.lang == thisUser.lang) {
                                    langList.add(temp)
                                }
                            }
                            val str = userList.size.toString() + " utilisateurs en ligne\n" + langList.size.toString() + " dans votre langue"
                            number2.text = str
                            userList.clear()
                            langList.clear()
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            }
        )
    }

}

