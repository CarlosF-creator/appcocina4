package com.example.appcocina4

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
//import com.google.firebase.appcheck.FirebaseAppCheck
//import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.eval.*
import java.io.File


class eval : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var db_Storage = Firebase.storage.reference
    var comentarios = ArrayList<Comentario>()
    var listanombres = ArrayList<String?>()
    var listafavoritos = ArrayList<String?>()
    var userID: String? = null;

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

        //obtenerComentarios()

        obtenerFavoritos()


        switch1.setOnCheckedChangeListener { button, isChecked ->
            if(isChecked){
                println("Usuario : ${userID}");

                db.collection("favoritos").get().addOnSuccessListener { documentos ->
                    for(d in documentos){
                        if(d.data["uid"] == userID){
                            var recetas = d.data["recetas"] as ArrayList<String?>;
                            var index = recetas.indexOf(nombreR);

                            println("Favoritos: ${recetas} index ${nombreR} ${index}");
                            if(index == -1){
                                recetas.add(nombreR);
                                val data = hashMapOf("recetas" to recetas)
                                db.collection("favoritos").document(d.id).set(data, SetOptions.merge())
                                listafavoritos.add(nombreR)
                            }
                        }
                    }
                }
            }else{
                println("Usuario : ${userID}");
                db.collection("favoritos").get().addOnSuccessListener { documentos ->
                    for(d in documentos){
                        if(d.data["uid"] == userID){
                            var recetas = d.data["recetas"] as ArrayList<String?>;
                            var index = recetas.indexOf(nombreR);
                            println("Favoritos: ${recetas} index ${nombreR} ${index}");
                            if(index != -1){
                                recetas.remove(nombreR);
                                val data = hashMapOf("recetas" to recetas)
                                db.collection("favoritos").document(d.id).set(data, SetOptions.merge())
                                println("Documento: ${d.id}");
                                listafavoritos.remove(nombreR)
                            }
                        }
                    }
                }
            }
        }

        var tempNombre : String = ""
        if (nombreR != null){
            tempNombre = nombreR.toString()
        }
        val pr = ProgressDialog(this)
        pr.setMessage("Cargando...")
        pr.setCancelable(false)
        pr.show()

        obtenerNombresRecetas()
        obtenerFavoritos()
        obtenerComentarios()
        obtenerImagenPortada(tempNombre,pr)
    }


    fun btnFavorito(p0: View?){
        var tempFavoritos = Intent(this,Favoritos::class.java)
        tempFavoritos.putExtra("listanombres",listanombres)
        tempFavoritos.putExtra("listafavoritos",listafavoritos)
        startActivity(tempFavoritos)
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
    fun obtenerFavoritos(){
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        userID = userid;
        db.collection("favoritos").get().addOnSuccessListener {
                documentos -> for(documento in documentos){
            if(documento.data["uid"] == userid){
                listafavoritos = documento.data["recetas"] as ArrayList<String?>;

                if(listafavoritos.contains(nombreR)){
                    switch1.isChecked = true;
                }else{
                    switch1.isChecked = false;
                }
            }
        }
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
