package com.example.spik.registerlogin


import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spik.R
import com.example.spik.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.spinner_item.*
import java.util.*


class RegisterActivity: AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance("https://spik-app-default-rtdb.europe-west1.firebasedatabase.app/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Affichage de la liste des langues
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(this, R.array.languages_available, R.layout.spinner_item)
        langSpinner.adapter = adapter


        //Actions lors d'un click sur le bouton d'inscription
        registerButton.setOnClickListener {
            register()
        }

    }

    private fun register() {

        //Récupération de toutes les inputs pour l'inscription
        val email = emailAddressRegister.text.toString()
        val password = passwordRegister.text.toString()
        val pseudo = pseudoRegister.text.toString()
        val index = langSpinner.selectedItemPosition

        //Vérification que tout les champs ont été remplis
        if(email.isEmpty() || password.isEmpty() || pseudo.isEmpty() || index == 0) {
            if(email.isEmpty()) {
                Toast.makeText(this, "Email incorrect", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Mot de passe incorrect", Toast.LENGTH_SHORT).show()
            } else if (pseudo.isEmpty()) {
                Toast.makeText(this, "Pseudo incorrect", Toast.LENGTH_SHORT).show()
            } else if (index == 0) {
                Toast.makeText(this, "Langue incorrecte", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Erreur", Toast.LENGTH_SHORT).show()
            }
        }

        //Création d'un nouvel utilisateur
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener

                    //Push des données de l'utilisateur dans la base de données
                    pushUserToFirebase()

                    //Message si succès
                    Toast.makeText(this, "Compte créé avec succès", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    //Message si erreur
                    Toast.makeText(this, "Erreur lors de la création du compte: $it.message", Toast.LENGTH_SHORT).show()
                }
    }


    private fun pushUserToFirebase() {

        val uid = FirebaseAuth.getInstance().uid

        //Reference vers la db
        database.getReference("/users/$uid")

        //Création de l'objet user
        val user = User(uid!!, pseudoRegister.text.toString(), langSpinner.selectedItemPosition, false, "")

        //Envoi vers la db
        database.getReference("/users/$uid").setValue(user)
                //Si la requete réussit
                .addOnSuccessListener {
                    Toast.makeText(this, "Compte créé avec succès", Toast.LENGTH_SHORT).show()
                    //Renvoie vers la page de connexion
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                //Si la requête échoue
                .addOnFailureListener {
                    Toast.makeText(this, "Erreur lors de la création du compte: $it.message", Toast.LENGTH_SHORT).show()
                }
    }

    //Fonction pour changer l'action lorqu'on appuie sur la touche Back du menu de navigation
    override fun onBackPressed() {
        //Renvoie vers la page de login
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}