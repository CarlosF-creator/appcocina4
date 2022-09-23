  package com.example.appcocina4

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.core.widget.addTextChangedListener
import com.example.appcocina4.databinding.ActivityPasoapasoBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File


  class Pasoapaso : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var db_Storage = Firebase.storage.reference
    var numpasos = 0
    var cont = 0
    var listapasos  = ArrayList<String?>()
    var listaimagenes = ArrayList<Bitmap>()
    var nombreR : String = ""
    var estadoTempo = 0
      var estadoPlay = 0



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
        var text_Temp_min = findViewById<TextView>(R.id.textNumTempMin)
        var text_Temp_mseg = findViewById<TextView>(R.id.textNumTempSeg)
        var text_Temp_dospuntos = findViewById<TextView>(R.id.textViewdospuntos)
        var btn_play = findViewById<Button>(R.id.btnPlay)

        text_numPaso.text = "Paso: ${cont+1}/$numpasos"

        text_Temp_min.isVisible = false
        text_Temp_mseg.isVisible = false
        text_Temp_dospuntos.isVisible = false
        btn_play.isVisible = false

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
    fun btnTempo(p0: View?){
        var scrollView3 = findViewById<ScrollView>(R.id.scrollView3)

        var text_Temp_min = findViewById<TextView>(R.id.textNumTempMin)
        var text_Temp_mseg = findViewById<TextView>(R.id.textNumTempSeg)
        var text_Temp_dospuntos = findViewById<TextView>(R.id.textViewdospuntos)
        var btn_play = findViewById<Button>(R.id.btnPlay)

        if(estadoTempo == 0){
            scrollView3.setPadding(0,95,0,0)

            text_Temp_min.isVisible = true
            text_Temp_mseg.isVisible = true
            text_Temp_dospuntos.isVisible = true
            btn_play.isVisible = true
            estadoTempo+=1
        }else{
            scrollView3.setPadding(0,0,0,0)

            text_Temp_min.isVisible = false
            text_Temp_mseg.isVisible = false
            text_Temp_dospuntos.isVisible = false
            btn_play.isVisible = false
            estadoTempo=0
        }
    }
      fun btnPlay(p0: View?){

          var btn_play = findViewById<Button>(R.id.btnPlay)
          var text_Temp_min = findViewById<TextView>(R.id.textNumTempMin)
          var text_Temp_mseg = findViewById<TextView>(R.id.textNumTempSeg)
          if(estadoPlay == 0){
              btn_play.foreground = getResources().getDrawable(android.R.drawable.ic_media_pause)
              text_Temp_min.isClickable = false
              text_Temp_mseg.isClickable = false
              estadoPlay+=1
          }else{
              btn_play.foreground = getResources().getDrawable(android.R.drawable.ic_media_play)
              text_Temp_min.isClickable = true
              text_Temp_mseg.isClickable = true
              estadoPlay=0
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

    fun onQueryTextChange(newText: String?): Boolean {

        return false
    }


}