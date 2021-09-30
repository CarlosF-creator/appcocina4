package com.example.appcocina4

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore

class Recetas() : AppCompatActivity() {
/*
    fun Recetas(listaPasos: ArrayList<String>, listaImagenes: ArrayList<Image>){
        var listaPasos = listaPasos
        var listaImagenes = listaImagenes
    }
*/

    //var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recetas)
    }

    fun instanciarBotones(){
        var listaRecetas = ArrayList<String>()
        //var hola = db.collection("recetas").get()
        //println(hola)
    }

    fun btnLasaña(p0: View?){
        startActivity(Intent(this, pre_receta::class.java))
    }
}