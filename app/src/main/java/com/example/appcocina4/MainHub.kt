package com.example.appcocina4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore

class MainHub : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btningredientes(p0: View?){
        startActivity(Intent(this, Select_Ingredientes::class.java))
    }
    fun btnrecetas(p0: View?){
       startActivity(Intent(this, Recetas::class.java))




    }
}