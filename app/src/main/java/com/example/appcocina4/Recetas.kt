package com.example.appcocina4

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.ViewAnimator
import androidx.annotation.Dimension
import com.google.firebase.firestore.FirebaseFirestore

class Recetas() : AppCompatActivity() {
    var nombresRecetas = ArrayList<String?>()
    var portadas = ArrayList<Image?>()
    var db = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recetas)
        nombresRecetas = intent.getStringArrayListExtra("listanombres") as ArrayList<String?>

        instanciarBotones()

    }


    fun instanciarBotones(){
        var listaRecetas = ArrayList<String>()
        botones(nombresRecetas)
        //println(hola)
    }

    fun btndeRecetas(p0: View?){

        for ( a in nombresRecetas){
            println("Receta :$a" )
        }
        var prereceta = Intent(this, pre_receta::class.java)
        //prereceta.putExtra("nombre", nombresRecetas)
        startActivity(prereceta)
    }

    fun obtenerNombres(){
        var temp = ArrayList<String?>()
        db.collection("recetas").get().addOnSuccessListener { documento ->
        for (d in documento){
            nombresRecetas.add(d.id)
        }
        }
    }

    fun botones(listanombres : ArrayList<String?>){
        var radiobutton = findViewById<RadioGroup>(R.id.radiobutton)
        for (l in listanombres){
            var tempbtn = crearBoton()
            tempbtn.text = l
            radiobutton.addView(tempbtn)
        }

    }

    fun crearBoton():Button{
        var btnreceta = findViewById<Button>(R.id.btndereceta)
        var tempBtn : Button = Button(btnreceta.context)

        tempBtn.id = ViewAnimator.generateViewId()
        tempBtn.height = 400
        tempBtn.width = 735
        tempBtn.textSize = 20F
        tempBtn.setPadding(0,170,0,0)

        return tempBtn

    }
}