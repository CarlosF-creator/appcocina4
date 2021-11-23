package com.example.appcocina4

import android.app.ProgressDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
//import com.google.firebase.appcheck.FirebaseAppCheck
//import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File


class eval : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var db_Storage = Firebase.storage.reference
    var comentarios = ArrayList<Comentario>()

    var commentLayout: LinearLayout? = null
    var nombreR : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.eval)

        commentLayout = findViewById(R.id.comentarios)
        nombreR = intent.getStringExtra("nombreR")

        var tempNombre : String = ""
        if (nombreR != null){
            tempNombre = nombreR.toString()
        }
        val pr = ProgressDialog(this)
        pr.setMessage("Cargando...")
        pr.setCancelable(false)
        pr.show()

        obtenerComentarios()
        obtenerImagenPortada(tempNombre,pr)
    }

    fun obtenerImagenPortada(nombreR: String, pr : ProgressDialog){

        var tempNombre = traductordeÑ(nombreR).lowercase()
        var referencia = db_Storage.child("fotos_recetas/$nombreR/$tempNombre"+"P.jpg")
        if(referencia == null){
            println("xd")
        }
        val localfile2 = File.createTempFile(tempNombre+"P",".jpg")
        referencia.getFile(localfile2).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile2.absolutePath)
            findViewById<ImageView>(R.id.ImagenFinal).setImageBitmap(bitmap)
            if (pr.isShowing){
                pr.dismiss()
            }

        }.addOnFailureListener{
            if (pr.isShowing){
                pr.dismiss()
            }
            Toast.makeText(this,"Fallo en la carga de imagenes",Toast.LENGTH_SHORT).show()


        }
    }
    fun traductordeÑ(nombreR : String):String{
        var tempNombre = ""
        for (n in nombreR){
            if (n.equals('ñ')){
                tempNombre = tempNombre + 'n'
            } else{
                tempNombre = tempNombre + n
            }
        }
        return tempNombre
    }

    fun cleanComentarios() {
        comentarios.clear()
        commentLayout?.removeAllViewsInLayout()
    }

    fun mostrarComentarios() {
        for (comentario in comentarios) {
            var view = layoutInflater.inflate(R.layout.comentario, null)
            val cmItem: TextView = view.findViewById(R.id.cm_item)

            cmItem.text = comentario.text
            commentLayout?.addView(view)
        }
    }

    // obtener los comentarios de la receta
    fun obtenerComentarios() {
        if (nombreR != null) {
            db.collection("recetas").document(nombreR.toString()).collection("Comentarios").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var text = document.data?.get("text").toString()
                    // TODO: Modificar username
                    comentarios.add(Comentario(text, "Username"))
                }
                mostrarComentarios()
            }.addOnFailureListener { _ ->
                println("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
            }
        }else {
            print("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        }
    }

    fun guardarComentatio(nombreR : String, text : String, user : String) {
        if (nombreR != "no se encontro") {
            var tempComentario = Comentario(text, user)
            db.collection("recetas").document(nombreR).set(tempComentario)
        } else {
            print("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        }
    }
    fun btnSalir(p0: View?){
        finish()
        finish()
    }

    fun btnComentar(p0: View?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Cargando...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        var comentarLayout = findViewById<LinearLayout>(R.id.comentarLayout)
        var comentarioView = comentarLayout.findViewById<TextInputLayout>(R.id.comentario)
        var comantarioText = findViewById<TextInputEditText>(R.id.comentarioText)

        if (comantarioText.text != null && comantarioText.text!!.isNotEmpty()) {
            var tempComentario = Comentario(comantarioText.text.toString(), "Username")
            db.collection("recetas").document(nombreR.toString()).collection("Comentarios")
                .add(tempComentario).addOnSuccessListener {
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                }
                Toast.makeText(
                    applicationContext,
                    "Comentario Creado Correctamente",
                    Toast.LENGTH_SHORT
                ).show()
                    cleanComentarios()
                    obtenerComentarios()
                    mostrarComentarios()
            }.addOnFailureListener {
                Toast.makeText(
                    applicationContext,
                    "Ocurrio un Error, intentelo mas tarde",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}