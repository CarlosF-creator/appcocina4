package com.example.appcocina4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_calculadora.*


class Calculadora2 : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    var suggestion = ArrayList<String?>()
    var nombre = ArrayList<String?>()
    var calo = ArrayList<String?>()
    var prote = ArrayList<String?>()
    var grasas = ArrayList<String?>()
    var carbos = ArrayList<String?>()
    var listaingredientes = ArrayList<String?>()
    var posicion = -777
    var posicion2 = -777
    var nome = ArrayList<String?>()
    var calor = ArrayList<String?>()
    var protes = ArrayList<String?>()
    var grasass = ArrayList<String?>()
    var carbosh = ArrayList<String?>()
    var numbe = ArrayList<String?>()
    var calofinal = -676
    var calofinal2 = -676
    var calofinalDef = -676
    var carbofinal = -676
    var carbofinal2 = -676
    var carbofinalDef = -676
    var grasafinal = -676
    var grasafinal2 = -676
    var grasafinalDef = -676
    var protefinal = -676
    var protefinal2 = -676
    var protefinalDef = -676

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculadora2)

        listaingredientes = intent.getStringArrayListExtra("listaingredientes") as ArrayList<String?>

        obtenerNombresRecetas()
    }

    fun obtenerNombresRecetas(){
        suggestion.clear()
        nombre.clear()
        calo.clear()
        carbos.clear()
        grasas.clear()
        prote.clear()
        for (grupIng in listaingredientes){
            db.collection("ingredientes").document(grupIng.toString()).collection("Ingredientes").get().addOnSuccessListener { documento ->
                for (d in documento) {
                    suggestion.add(d.id)

                    var tempnombre = d.id.toString()
                    if (tempnombre!= "null"){
                        nombre.add(d.id).toString()
                    }
                    var tempcalorias = d.data?.get("calorias").toString()
                    if (tempcalorias != "null"){
                        calo.add(d.data?.get("calorias").toString())
                    }
                    var tempcarbos = d.data?.get("carbohidratos").toString()
                    if (tempcarbos != "null"){
                        carbos.add(d.data?.get("carbohidratos").toString())
                    }
                    var tempgrasas = d.data?.get("grasas").toString()
                    if (tempgrasas != "null"){
                        grasas.add(d.data?.get("grasas").toString())
                    }
                    var tempprote = d.data?.get("proteinas").toString()
                    if (tempprote != "null"){
                        prote.add(d.data?.get("proteinas").toString())
                    }
            }
        }}
        autocompletado()
        autocompletado2()

    }
    fun btn_suma(p0:View?){

        obtenering()
    }

    fun autocompletado(){
        var adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,suggestion)

        autoCompleteTextView.threshold=1
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setOnFocusChangeListener { view, b -> if (b) autoCompleteTextView.showDropDown() }
        nome.equals(autoCompleteTextView.text)

    }

    fun autocompletado2(){
        var adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,suggestion)

        autoCompleteTextView2.threshold=1
        autoCompleteTextView2.setAdapter(adapter)
        autoCompleteTextView2.setOnFocusChangeListener { view, b -> if (b) autoCompleteTextView2.showDropDown() }

    }

fun obtenering(){
    nome.add(autoCompleteTextView.text.toString())
    nome.add(autoCompleteTextView2.text.toString())
    numbe.add(editTextNumber2.text.toString())
    numbe.add(editTextNumber3.text.toString())
    var i= 0
    var j= 0
    for (item in nombre){
        if (nome[0].toString() == nombre[j].toString()){
            posicion = i
            break
        }
        i += 1
        j += 1
    }
    calor.add(calo[posicion])
    protes.add(prote[posicion])
    grasass.add(grasas[posicion])
    carbosh.add(carbos[posicion])

    //calofinal = (calor[0]?.toFloat()?.times(numbe[0]!!?.toFloat())!!)
    //carbofinal = (carbosh[0]?.toInt()?.toFloat()?.times(numbe[0]!!?.toInt())!!)
    grasafinal = (grasass[0]?.toInt()?.times(numbe[0]!!?.toInt())!!)
    protefinal = (protes[0]?.toInt()?.times(numbe[0]!!?.toInt())!!)


    var k= 0
    var l= 0
    for (item2 in nombre){
        if (nome[1].toString() == nombre[l].toString()){
            posicion2 = k
            break
        }
        k += 1
        l += 1
    }
    calor.add(calo[posicion2])
    protes.add(prote[posicion2])
    grasass.add(grasas[posicion2])
    carbosh.add(carbos[posicion2])

    calofinal2 = (calor[1]?.toInt()?.times(numbe[1]!!?.toInt())!!)
    carbofinal2 = (carbosh[1]?.toInt()?.times(numbe[1]!!?.toInt())!!)
    grasafinal2 = (grasass[1]?.toInt()?.times(numbe[1]!!?.toInt())!!)
    protefinal2 = (protes[1]?.toInt()?.times(numbe[1]!!?.toInt())!!)

    calofinalDef= calofinal+calofinal2
    carbofinalDef= carbofinal+carbofinal2
    grasafinalDef= grasafinal+grasafinal2
    protefinalDef= protefinal+protefinal2

    println(nome)

    println("calorias: " +calofinalDef.toString())
    println("carbos: " +carbofinalDef.toString())
    println("grasas: " +grasafinalDef.toString())
    println("protes: " +protefinalDef.toString())

}
}




