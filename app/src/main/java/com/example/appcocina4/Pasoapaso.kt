package com.example.appcocina4

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore

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
            for (x in lasana.documents.get(0)["Instrucciones"].toString()){
                listapasos.add(x.toString())

            }

        }.addOnFailureListener{
                _ -> println("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        }

        for (l in listapasos){
            println(l)

        }
    }



    fun conteoPasos(numpasos : Int): Int {

    return numpasos + 1


    }

    fun btnSiguiente(p0: View?){
        for (l in listapasos){
            println(l)

        }

    }
    fun btnAtras(p0: View?){


    }






}