package com.example.appcocina4

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.appcheck.FirebaseAppCheck
//import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.firestore.FirebaseFirestore


class eval : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var comentarios = ArrayList<Comentario>()

    var commentLayout: LinearLayout? = null
    var nombreR : String? = null

    var estrellas: RatingBar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.eval)

        commentLayout = findViewById(R.id.comentarios)
        nombreR = intent.getStringExtra("nombreR")

        estrellas = findViewById<RatingBar>(R.id.ratingBar2)


        estrellas!!.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener()
        { ratingBar: RatingBar, fl: Float, b: Boolean ->


            escribirEstrella(ratingBar.rating.toInt())

            Log.d("rate", ratingBar.rating.toString())

        }


        obtenerComentarios()
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

    fun btnComentar(p0: View?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Cargando...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        var comentarLayout = findViewById<LinearLayout>(R.id.comentarLayout)
        var comentarioView = comentarLayout.findViewById<TextInputLayout>(R.id.comentario)
        var comantarioText = comentarioView.findViewById<TextInputEditText>(R.id.comentarioText)

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



    fun escribirEstrella( numStar: Int){
        val evaluacion = hashMapOf(
            "user" to FirebaseAuth.getInstance().uid,
            "puntuacion" to numStar,
            "receta" to nombreR
        )

        db.collection("evaluacion").add(evaluacion).addOnSuccessListener { print("DocumentSnapshot successfully written!") }
            .addOnFailureListener { e ->print("E::: $e")}
    }
}