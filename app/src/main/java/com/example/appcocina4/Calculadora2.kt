package com.example.appcocina4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_calculadora.*

class Calculadora2 : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    var suggestion = ArrayList<String?>()
    var macros = ArrayList<String?>()
    var listaingredientes = ArrayList<String?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculadora2)

        listaingredientes = intent.getStringArrayListExtra("listaingredientes") as ArrayList<String?>



        obtenerNombresRecetas()
    }




    fun btn_suma(p0:View?){
        println("tamaño arreglo : "+ macros.size)
    }


    fun obtenerNombresRecetas(){
        suggestion.clear()
        for (grupIng in listaingredientes){
            db.collection("ingredientes").document(grupIng.toString()).collection("Ingredientes").get().addOnSuccessListener { documento ->
                for (d in documento) {
                    suggestion.add(d.id)
                    var temploquesea = d.data?.get("calorias").toString()
                    if (temploquesea != "null"){
                        macros.add(d.data?.get("calorias").toString())
                        println(d.data?.get("calorias").toString())
                }}
            }
        }
        weadeltobal()

    }

    fun weadeltobal(){
        var adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,suggestion)

        autoCompleteTextView.threshold=0
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setOnFocusChangeListener({ view, b->  if(b) autoCompleteTextView.showDropDown()})
        println("tamaño arreglo : "+ macros.size)

    }
}