  package com.example.appcocina4

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.appcocina4.databinding.ActivityMainBinding
import com.example.appcocina4.databinding.ActivityPasoapasoBinding
import com.example.appcocina4.databinding.ActivityPreRecetaBinding
import kotlin.math.roundToInt
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.util.regex.Pattern


class Pasoapaso : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var db_Storage = Firebase.storage.reference
    var numpasos = 0
    var cont = 0
    var listapasos  = ArrayList<String?>()
    var listaimagenes = ArrayList<Bitmap>()
    var nombreR : String = ""



    lateinit var binding : ActivityPasoapasoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasoapasoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listapasos = intent.getStringArrayListExtra("lista") as ArrayList<String?>
        numpasos = intent.getIntExtra("num",-500)
        //listaimagenes = intent.getStringArrayListExtra("imagenes") as ArrayList<Bitmap>
        var nombre = intent.getStringExtra("nombreR")

        var text_paso = findViewById<TextView>(R.id.text_paso)
        var text_numPaso = findViewById<TextView>(R.id.textNumPasos)

        text_numPaso.text = "Paso: ${cont+1}/$numpasos"




        if (nombre != null){
            var nombreweno :String = nombre.toString()
            nombreR = nombreweno
        }else{
            println("Nombre Null")
        }
        if (listapasos.isNotEmpty()){
            text_paso.text = listapasos[cont]
            //obtenerImagenPaso(nombreR, 0)
            obtenerImagenesPasos(nombreR, numpasos, 0)
        }
    }


    fun obtenerImagenesPasos(nombreR: String, nPasos: Int, count: Int){
        var validador = false
        val pr = ProgressDialog(this)
        pr.setMessage("Cargando...")
        pr.setCancelable(false)
        pr.show()
        for (i in 0..(nPasos-1)) {


            var tempNombre = traductordeÑ(nombreR).lowercase() + i.toString()
            var nombreImagen = ""

            try {
                var tempReferencia = db_Storage.child("fotos_recetas/$nombreR/$tempNombre" + ".jpg")
                nombreImagen = ".jpg"
            } catch (e: Exception) {
                nombreImagen = ".JPG"
            }
            var referencia = db_Storage.child("fotos_recetas/$nombreR/$tempNombre" + nombreImagen)
            val localfile2 = File.createTempFile(tempNombre, nombreImagen)
            referencia.getFile(localfile2).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile2.absolutePath)
                listaimagenes.add(bitmap)

                println(listaimagenes.size)
                if (listaimagenes.size == (nPasos/2).toInt()){
                    aplicarImagenes(count)
                    if (pr.isShowing){
                        pr.dismiss()
                    }
                }

            }.addOnFailureListener {

                Toast.makeText(this, "Fallo en la carga de imagenes", Toast.LENGTH_SHORT).show()

            }
        }

    }

    fun aplicarImagenes(count: Int){
        if (listaimagenes.size != 0){
            findViewById<ImageView>(R.id.Imagen_Paso).setImageBitmap(listaimagenes[count])
        }
        else{
            Toast.makeText(this, "Fallo al mostrar las imagenes", Toast.LENGTH_SHORT).show()
        }
    }


    fun mas(){
        cont += 1
    }
    fun menos(){
        cont -= 1
    }


    fun btnSiguiente(p0: View?){
        if (cont < numpasos-1){
            mas()
            var text_numPaso = findViewById<TextView>(R.id.textNumPasos)
            var text_paso = findViewById<TextView>(R.id.text_paso)
            println(cont)
            //obtenerImagenPaso(nombreR , cont)
            aplicarImagenes(cont)

            text_paso.text = listapasos[cont]
            text_numPaso.text = "Paso: ${cont+1}/$numpasos"
        }else{
            var eval = Intent(this, eval::class.java)
            eval.putExtra("nombreR", nombreR)
            startActivity(eval)
        }
    }


    fun btnAtras(p0: View?){
        if(cont > 0){
            menos()
            var text_numPaso = findViewById<TextView>(R.id.textNumPasos)
            var text_paso = findViewById<TextView>(R.id.text_paso)
            println(cont)
            //obtenerImagenPaso(nombreR , cont)
            aplicarImagenes(cont)

            text_paso.text = listapasos[cont]
            text_numPaso.text = "Paso: ${cont+1}/$numpasos"
        }else{
            finish()
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