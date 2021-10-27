package com.example.appcocina4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class MainHub : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var listanombres = ArrayList<String?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var usuario = intent.getStringExtra("Usuario")
        obtenerNombres()
        sugerencias(usuario.toString())
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
    fun sugerencias(usuario:String){
        var btnsuge = findViewById<Button>(R.id.buttonsugerencias)

        btnsuge.setOnClickListener{
            var suge = findViewById<EditText>(R.id.editTextTextMultiLinesugerencias)
            if(suge.text.isNotEmpty()){
                db.collection("sugerencias").document(usuario).set(
                    hashMapOf("sugerencia" to suge.text.toString()))

            } else{
                    Toast.makeText(applicationContext,"favor escribir alguna sugerencia", Toast.LENGTH_SHORT).show()
            }
    } }
}