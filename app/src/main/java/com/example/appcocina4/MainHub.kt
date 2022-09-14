package com.example.appcocina4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.firebase.ui.auth.AuthUI

class MainHub : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var listanombres = ArrayList<String?>()
    var listafavoritos = ArrayList<String?>()
    var listaingredientes = ArrayList<String?>()
    var userID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btnNuevaReceta = findViewById<Button>(R.id.botonNuevaReceta)
        btnNuevaReceta.isVisible = false
        //var txtSugerencias = findViewById<EditText>(R.id.editTextTextMultiLinesugerencias)

        obtenerNombresRecetas()
        obtenerIngredientes()
        obtenerNombreUsuario()
        CerrarSesion()
    }



    fun btnFavorito(p0: View?){
        var temprecetas = Intent(this, Recetas::class.java)
        temprecetas.putExtra("listanombres", listafavoritos)
        startActivity(temprecetas)
    }


    fun btningredientes(p0: View?){
        var tempingredientes = Intent(this, Select_Ing::class.java)
        tempingredientes.putExtra("listaingredientes", listaingredientes)
        tempingredientes.putExtra("listanombres", listanombres)
        startActivity(tempingredientes)
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
    fun btnCalculadora(p0: View?){
        var tempCalculadora = Intent(this, Calculadora2::class.java)
        tempCalculadora.putExtra("listaingredientes", listaingredientes)
        startActivity(tempCalculadora)
    }
    fun obtenerNombresRecetas(){
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
    fun obtenerFavoritos(userID : String){
        listafavoritos.clear()
        db.collection("users").document(userID.toString()).collection("Favoritos").get().addOnSuccessListener{ document ->
            for (d in document){
                var visible = d.data?.get("visible").toString()
                if (visible == "true"){
                    listafavoritos.add(d.id)
                }
            }
            println("favoritos ingresados")
        }.addOnFailureListener{
            Toast.makeText(this,"Fallo en la Verificacion del Usuario", Toast.LENGTH_SHORT).show()
        }

    }

    fun obtenerIngredientes(){
        listaingredientes.clear()
        db.collection("ingredientes").get().addOnSuccessListener { documento ->
            for (d in documento){
                listaingredientes.add(d.id)

            }
        }
    }

/*
    fun sugerencias(usuario:String){
        var btnsuge = findViewById<Button>(R.id.buttonsugerencias)

        btnsuge.setOnClickListener{
            var suge = findViewById<EditText>(R.id.editTextTextMultiLinesugerencias)
            if(suge.text.isNotEmpty()){
                db.collection("sugerencias").document(usuario).set(
                    hashMapOf("sugerencia" to suge.text.toString())).addOnSuccessListener {
                    Toast.makeText(applicationContext,"La sugerencia se a enviado correctamente", Toast.LENGTH_SHORT).show()
                }

            } else{
                Toast.makeText(applicationContext,"favor escribir alguna sugerencia", Toast.LENGTH_SHORT).show()
            }
        } }
*/
    fun obtenerNombreUsuario(){
        db.collection("users").get().addOnSuccessListener{ document ->
            for (d in document){
                if (d.data?.get("Uid") == FirebaseAuth.getInstance().uid){
                    verificarUsuario(d.id.lowercase())
                    //sugerencias(d.id.lowercase())
                    obtenerFavoritos(d.id.lowercase())
                    findViewById<TextView>(R.id.Nombre_usuario).setText("Bienvenido "+d.data?.get("user").toString())
                    break
                }
            }
        }.addOnFailureListener{
            Toast.makeText(this,"Fallo en la Verificacion del Usuario", Toast.LENGTH_SHORT).show()
        }
    }
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
    fun CerrarSesion(){
        var btnCerrar = findViewById<Button>(R.id.buttonCerrarSesion)
        btnCerrar.setOnClickListener {
            btnCerrar.isEnabled = false
            AuthUI.getInstance().signOut(this).addOnSuccessListener {
                startActivity(Intent(this, login::class.java))
                Toast.makeText(this,"Hasta pronto",Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                btnCerrar.isEnabled = true
                Toast.makeText(this,"Ocurrio un error ${it.message}",Toast.LENGTH_SHORT).show()

            }
}}}