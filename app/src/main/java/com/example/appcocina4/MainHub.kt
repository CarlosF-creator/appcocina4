package com.example.appcocina4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.firestore.FirebaseFirestore

class MainHub : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var listanombres = ArrayList<String?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btnNuevaReceta = findViewById<Button>(R.id.botonNuevaReceta)
        btnNuevaReceta.isVisible = false
        var usuario = intent.getStringExtra("Nombre_U")
        obtenerNombres()
        verificarUsuario(usuario.toString().lowercase())
        sugerencias(usuario.toString().lowercase())
    }

    fun btningredientes(p0: View?){
        startActivity(Intent(this, Select_Ingredientes::class.java))
    }
    fun btnrecetas(p0: View?){
        var temprecetas = Intent(this, Recetas::class.java)
        temprecetas.putExtra("listanombres", listanombres)
        startActivity(temprecetas)
    }
    fun btnNuevaReceta(p0:View?){
        var crearRecetas = Intent(this, CrearRecetas::class.java)
        startActivity(crearRecetas)
    }
    fun obtenerNombres(){
        listanombres.clear()
        db.collection("recetas").get().addOnSuccessListener { documento ->
            for (d in documento){
                var temp = d.data?.get("publica").toString()
                if (temp.toString() == "true"){
                    listanombres.add(d.id)
                }else{
                    if (temp.toString() == "false"){

                    }else{
                        println("Errrrrrrrrrrrrrrooooor")
                    }
                }

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
    fun verificarUsuario(Nombre_U : String){
        db.collection("users").document(Nombre_U).get().addOnSuccessListener { document ->
            var temp : String? = document.data?.get("principiante").toString()
            if (temp != null){
                var rol = temp.toInt()
                var btnNuevaReceta = findViewById<Button>(R.id.botonNuevaReceta)
                if (rol == 0){
                    btnNuevaReceta.isVisible = true
                }

            }


        }.addOnFailureListener{
            Toast.makeText(this,"Fallo en la Verificacion del Usuario", Toast.LENGTH_SHORT).show()
        }

    }
}