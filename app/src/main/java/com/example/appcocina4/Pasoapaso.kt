package com.example.appcocina4

import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.example.appcocina4.databinding.ActivityMainBinding
import com.example.appcocina4.databinding.ActivityPasoapasoBinding
import com.example.appcocina4.databinding.ActivityPreRecetaBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern


class Pasoapaso : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var numpasos = 0
    var cont = 1
    var listapasos  = ArrayList<String?>()
    var listaimagenes = ArrayList<Bitmap>()
    var nombreR : String? = null



    lateinit var binding : ActivityPasoapasoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasoapasoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listapasos = intent.getStringArrayListExtra("lista") as ArrayList<String?>
        numpasos = intent.getIntExtra("num",-500)
        listaimagenes = intent.getStringArrayListExtra("imagenes") as ArrayList<Bitmap>
        nombreR = intent.getStringExtra("nombreR")

        var text_paso = findViewById<TextView>(R.id.text_paso)
        var text_numPaso = findViewById<TextView>(R.id.textNumPasos)

        text_numPaso.text = "Paso: $cont/$numpasos"
        text_paso.text = listapasos[cont-1]
        if(listaimagenes != null && listaimagenes.isNotEmpty()){
            binding.ImagenPaso.setImageBitmap(listaimagenes[0])
        }else{
            binding.ImagenPaso.setImageBitmap(null)
        }



    }


    fun mas(){
        cont += 1
    }
    fun menos(){
        cont -= 1
    }


    fun btnSiguiente(p0: View?){
        if (cont < numpasos){
            var text_numPaso = findViewById<TextView>(R.id.textNumPasos)
            var text_paso = findViewById<TextView>(R.id.text_paso)
            if(listaimagenes != null && listaimagenes.isNotEmpty()){
                binding.ImagenPaso.setImageBitmap(listaimagenes[cont-1])
            }
            mas()

            text_paso.text = listapasos[cont-1]
            text_numPaso.text = "Paso: $cont/$numpasos"
        }else{
            var eval = Intent(this, eval::class.java)
            eval.putExtra("nombreR", nombreR)
            startActivity(eval)
        }
    }


    fun btnAtras(p0: View?){
        if(cont > 1){
            var text_numPaso = findViewById<TextView>(R.id.textNumPasos)
            var text_paso = findViewById<TextView>(R.id.text_paso)
            if(listaimagenes != null && listaimagenes.isNotEmpty()){
                binding.ImagenPaso.setImageBitmap(listaimagenes[cont-1])
            }
            menos()

            text_paso.text = listapasos[cont-1]
            text_numPaso.text = "Paso: $cont/$numpasos"
        }else{
            finish()
        }


    }




}