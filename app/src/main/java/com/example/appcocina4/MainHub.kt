package com.example.appcocina4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore

class MainHub : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var listanombres = ArrayList<String?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        obtenerNombres()
    }

    fun btningredientes(p0: View?){
        startActivity(Intent(this, Select_Ingredientes::class.java))
    }
    fun btnrecetas(p0: View?){
        var temprecetas = Intent(this, Recetas::class.java)
        temprecetas.putExtra("listanombres", listanombres)
        startActivity(temprecetas)
    }
    fun obtenerNombres(){
        db.collection("recetas").get().addOnSuccessListener { documento ->
            for (d in documento){
                listanombres.add(d.id)
            }
        }
    }
}