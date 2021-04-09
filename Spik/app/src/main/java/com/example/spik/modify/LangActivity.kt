package com.example.spik.modify

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spik.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_lang.*

class LangActivity: AppCompatActivity() {

    private val user = FirebaseAuth.getInstance()
    private val uid = user.uid
    private val database = FirebaseDatabase.getInstance("https://spik-app-default-rtdb.europe-west1.firebasedatabase.app/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lang)

        //Affichage de la liste des langues
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(this, R.array.languages_available, R.layout.spinner_item)
        changeLangSpinner.adapter = adapter

        returnModify.setOnClickListener {
            returnBack()
        }

        langSaveButton.setOnClickListener {
            changeLang()
        }
    }

    private fun changeLang() {
        val index = changeLangSpinner.selectedItemPosition

        if(index == 0) {
            Toast.makeText(this, "Veuillez sélectionner une langue correcte", Toast.LENGTH_SHORT).show()
        } else{
            database.getReference("/users/$uid").child("lang").setValue(index)
            //Renvoie vers la page de modif
            val intent = Intent(this, ModifyActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            Toast.makeText(this, "Langue modifiée avec succès", Toast.LENGTH_SHORT).show()
        }
    }

    private fun returnBack() {
        //Renvoie vers la page de modif
        val intent = Intent(this, ModifyActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    //Fonction pour changer l'action lorqu'on appuie sur la touche Back du menu de navigation
    override fun onBackPressed() {
        returnBack()
    }

}