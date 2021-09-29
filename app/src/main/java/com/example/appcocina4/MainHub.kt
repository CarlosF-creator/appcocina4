package com.example.appcocina4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore

class MainHub : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btningredientes(p0: View?){
        setContentView(R.layout.ingredientes)
    }
    fun btnrecetas(p0: View?){
       startActivity(Intent(this, Recetas::class.java))


    }
}