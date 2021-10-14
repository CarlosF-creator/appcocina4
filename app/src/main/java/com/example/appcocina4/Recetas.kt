package com.example.appcocina4

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.ViewAnimator
import com.google.firebase.firestore.FirebaseFirestore

class Recetas() : AppCompatActivity() {
    var nombresRecetas = ArrayList<String?>()
    var portadas = ArrayList<Image?>()
    var db = FirebaseFirestore.getInstance()

    fun Recetas(listaPasos: ArrayList<String?>, listaImagenes: ArrayList<Image>?, numPasos : Int){
        var listaPasos = listaPasos
        var listaImagenes = listaImagenes
        var numPasos = numPasos
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recetas)
        //instanciarBotones()
    }

    fun instanciarBotones(){
        var listaRecetas = ArrayList<String>()
        nombresRecetas = obtenerNombres()
        botones(nombresRecetas)

        //println(hola)
    }

    fun btndeRecetas(p0: View?){

        startActivity(Intent(this, pre_receta::class.java))

    }

    fun btnxd(p0: View?){
        startActivity(Intent(this, Select_Ingredientes::class.java))
    }

    fun obtenerNombres() : ArrayList<String?>{
        var temp = ArrayList<String?>()
        db.collection("recetas").get().addOnSuccessListener { documento ->
        for (d in documento){
            temp.add(d.id)
        }
        }
        return temp
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
        return tempBtn

    }
}