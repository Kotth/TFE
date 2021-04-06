package com.example.spik

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_modify.*
import kotlinx.android.synthetic.main.activity_modify.langSpinner
import kotlinx.android.synthetic.main.activity_register.*

class ModifyActivity: AppCompatActivity() {

    private val user = FirebaseAuth.getInstance()
    private val uid = user.uid
    private val database = FirebaseDatabase.getInstance("https://spik-app-default-rtdb.europe-west1.firebasedatabase.app/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify)

        //Affichage de la liste des langues
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(this, R.array.languages_available, R.layout.spinner_item)
        langSpinner.adapter = adapter

        returnButton.setOnClickListener {
            returnToHome()
        }

        saveButton.setOnClickListener {
            modifyUser()
        }
    }

    //Fonction pour changer l'action lorqu'on appuie sur la touche Back du menu de navigation
    override fun onBackPressed() {
        //Renvoie vers la page d'accueil
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun modifyUser() {

    }

    private fun returnToHome() {
        //Renvoie vers la page d'accueil
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}