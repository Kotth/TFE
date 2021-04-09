package com.example.spik.modify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spik.homemessages.HomeActivity
import com.example.spik.R
import kotlinx.android.synthetic.main.activity_modify.*

class ModifyActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify)

        returnButton.setOnClickListener {
            returnToHome()
        }

        passwordButton.setOnClickListener {
            modifyPassword()
        }

        langButton.setOnClickListener {
            modifyLang()
        }
    }

    //Fonction pour changer l'action lorqu'on appuie sur la touche Back du menu de navigation
    override fun onBackPressed() {
        returnToHome()
    }

    private fun returnToHome() {
        //Renvoie vers la page d'accueil
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun modifyLang() {
        val intent = Intent(this, LangActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun modifyPassword() {
        //Renvoie vers la page de modif du mdp
        val intent = Intent(this, PasswordActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}