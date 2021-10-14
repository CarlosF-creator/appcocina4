package com.example.appcocina4

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.appcocina4.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern


class Pasoapaso : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    var numpasos = 0
    var cont = 1
    var listapasos  = ArrayList<String?>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pasoapaso)

        listapasos = intent.getStringArrayListExtra("lista") as ArrayList<String?>
        numpasos = intent.getIntExtra("num",-500)

        var text_paso = findViewById<TextView>(R.id.text_paso)
        var text_numPaso = findViewById<TextView>(R.id.textNumPasos)

        text_numPaso.text = "Paso: $cont/$numpasos"
        text_paso.text = listapasos[cont-1]

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
            mas()

            text_paso.text = listapasos[cont-1]
            text_numPaso.text = "Paso: $cont/$numpasos"
        }else{
            setContentView(R.layout.eval)
        }
    }


    fun btnAtras(p0: View?){
        if(cont > 1){
            var text_numPaso = findViewById<TextView>(R.id.textNumPasos)
            var text_paso = findViewById<TextView>(R.id.text_paso)
            menos()
            text_paso.text = listapasos[cont-1]
            text_numPaso.text = "Paso: $cont/$numpasos"
        }
    }










}