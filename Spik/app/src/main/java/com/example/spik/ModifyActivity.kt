package com.example.spik

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_modify.*
import kotlinx.android.synthetic.main.activity_modify.langSpinner
import kotlinx.android.synthetic.main.activity_register.*

class ModifyActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify)

        //Affichage de la liste des langues
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(this, R.array.languages_available, R.layout.spinner_item)
        langSpinner.adapter = adapter
    }

}