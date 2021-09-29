package com.example.appcocina4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore

class Recetas : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recetas)
    }

    fun instanciarBotones(){
        var listaRecetas = ArrayList<String>()
        db.collection("recetas").document().get().addOnCanceledListener {

        }

    }
}