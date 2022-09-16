package com.example.appcocina4

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
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
    var listanombres = ArrayList<String?>()
    var listafavoritos = ArrayList<String?>()
    var userID: String? = null
    var userName : String? = null

    var commentLayout: LinearLayout? = null
    var nombreR : String? = null
    var estadoCorazon : Int = 0

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
        var tempNombre : String = ""
        if (nombreR != null){
            tempNombre = nombreR.toString()
        }
        val pr = ProgressDialog(this)
        pr.setMessage("Cargando...")
        pr.setCancelable(false)
        pr.show()

        obtenerNombreUsuario()
        //obtenerNombresRecetas()

        obtenerComentarios()
        obtenerImagenPortada(tempNombre,pr)
    }


    fun btnFavorito(p0: View?){
        var temprecetas = Intent(this, Recetas2::class.java)
        temprecetas.putExtra("listanombres", listanombres)
        startActivity(temprecetas)
    }
    fun btnCorazon(p0: View?){
        if (estadoCorazon == 1){
            cambioCorazonFav(0)
            cambiarEstadoFavorito(nombreR.toString(),estadoCorazon)
        }
        else{
            cambioCorazonFav(1)
            cambiarEstadoFavorito(nombreR.toString(),estadoCorazon)
        }
    }
    fun btnComentar(p0: View?) {
        var comantarioText = findViewById<TextInputEditText>(R.id.comentarioText)

        print(userName)
        if (comantarioText.text != null && comantarioText.text!!.isNotEmpty() && comantarioText.text.toString() != "") {
            var tempComentario = Comentario(comantarioText.text.toString(), userName)
            db.collection("recetas").document(nombreR.toString()).collection("Comentarios")
                .add(tempComentario).addOnSuccessListener {
                    Toast.makeText(
                        applicationContext,
                        "Comentario Creado Correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    comantarioText.setText("")
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
        else{
            Toast.makeText(this,"Comentario vacio... Intenta escribir algo", Toast.LENGTH_SHORT).show()
        }
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



    // obtener los comentarios de la receta
    fun obtenerComentarios() {
        if (nombreR != null) {
            db.collection("recetas").document(nombreR.toString()).collection("Comentarios").get()
                .addOnSuccessListener { documents ->
                for (document in documents) {
                    var text = document.data?.get("text").toString()
                    var userText = document.data?.get("user").toString()

                    comentarios.add(Comentario(""+userText+" : "+text, userText))
                }
                mostrarComentarios()
            }.addOnFailureListener { _ ->
                println("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
            }
        }else {
            print("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        }
    }

    fun obtenerFavoritos(){
        listanombres.clear()
        db.collection("users").document(userID.toString()).collection("Favoritos").get().addOnSuccessListener{ document ->
            for (d in document){
                var visible = d.data?.get("visible").toString()
                if (d.id == nombreR && visible == "true"){
                    cambioCorazonFav(1)
                }
                if (visible == "true"){
                    listanombres.add(d.id)
                }
            }
        }.addOnFailureListener{
            Toast.makeText(this,"Fallo en la Verificacion del Usuario", Toast.LENGTH_SHORT).show()
        }

    }

    fun obtenerNombreUsuario() {
        db.collection("users").get().addOnSuccessListener{ document ->
            for (d in document){
                if (d.data?.get("Uid") == FirebaseAuth.getInstance().uid){
                    userName = d.data?.get("user").toString()
                    userID = d.id
                    obtenerFavoritos()
                    break
                }
            }
        }.addOnFailureListener{
            Toast.makeText(this,"Fallo en la Verificacion del Usuario", Toast.LENGTH_SHORT).show()
        }
    }

    fun cambioCorazonFav(estado : Int){
        var btnfav = findViewById<Button>(R.id.btnFav)
        var cora_lleno = getResources().getDrawable(R.drawable.corazon_lleno)
        var cora_vacio = getResources().getDrawable(R.drawable.corazon_vacio)
        if (estado == 1){
            btnfav.background = cora_lleno
            estadoCorazon = 1
        }
        else{
            btnfav.background = cora_vacio
            estadoCorazon = 0
        }

    }

    fun cambiarEstadoFavorito(nombreR: String,estado: Int){
        if (estado == 1){
            //Guardar como favorito la receta actual
            db.collection("users").document(userID.toString()).collection("Favoritos").document(nombreR).set(
                hashMapOf("visible" to true)).addOnSuccessListener {
                obtenerFavoritos()
                Toast.makeText(this,"Receta guardada en Favoritos",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this,"Error al guardar en Favoritos",Toast.LENGTH_SHORT).show()
            }

        }
        else{
            //Eliminar de favoritos
            db.collection("users").document(userID.toString()).collection("Favoritos").document(nombreR).set(
                hashMapOf("visible" to false)).addOnSuccessListener {
                obtenerFavoritos()
                Toast.makeText(this,"Receta eliminada de Favoritos",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this,"Error al eliminar en Favoritos",Toast.LENGTH_SHORT).show()
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
}
