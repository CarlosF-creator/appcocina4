package com.example.appcocina4

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

var conteo = 0
class Pasoapaso : AppCompatActivity() {
    var listapasos = ArrayList<String>()
    var db = FirebaseFirestore.getInstance()
    fun Pasoapaso(receta : Recetas){


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pasoapaso)


        var listaimagenes = ArrayList<Image>()
        var direccion = "@drawable/"




        db.collection("recetas").get().addOnSuccessListener {
                lasana ->
                var temp1 = lasana.documents.get(0).data?.get("Instrucciones")
                var num =  lasana.documents.get(0).get("numeroPasos")
                println(temp1)
                println(num)

        }.addOnFailureListener{
                _ -> println("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        }

        for (l in listapasos){
            println( l )

        }
    }



    fun conteoPasos(numpasos : Int): Int {

    return numpasos + 1


    }

    fun btnSiguiente(p0: View?){
        for (l in listapasos){
            println(l)

        }
        var text_paso = findViewById<TextView>(R.id.text_paso)
        text_paso.text = listapasos[0].toString()
    }
    fun btnAtras(p0: View?){


    }








}