package com.example.appcocina4

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import com.google.firebase.firestore.FirebaseFirestore

class Select_Ing : AppCompatActivity() {

    var listaingredientes = ArrayList<String?>()
    var newListaRecetas = ArrayList<String?>()
    var listaNombresR = ArrayList<String?>()
    var listaCheck = ArrayList<CheckBox>()
    var db = FirebaseFirestore.getInstance()
    var tipo : Int = -1


    var Check1 = baseContext
    var Check2 = baseContext
    var TxtVisible = baseContext
    var TxtEspacio = baseContext


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_ing)

        listaingredientes = intent.getStringArrayListExtra("listaingredientes") as ArrayList<String?>
        listaNombresR = intent.getStringArrayListExtra("listanombres") as ArrayList<String?>
        tipo = intent.getIntExtra("tipo",-1)

        var tempCheck1 = findViewById<CheckBox>(R.id.check1)
        var tempCheck2 = findViewById<CheckBox>(R.id.check2)
        var tempTxtVisible= findViewById<TextView>(R.id.txtVisible)
        var tempTxtEspacio = findViewById<TextView>(R.id.txtEspacio2)
        var tempbtn = findViewById<Button>(R.id.btn_Singredientes)


        if (tipo == 2){
            tempbtn.setText("Listo")
        }



        Check1 = tempCheck1.context
        Check2 = tempCheck2.context
        TxtVisible = tempTxtVisible.context
        TxtEspacio = tempTxtEspacio.context

        tempCheck1.isVisible = false
        tempCheck2.isVisible = false
        tempTxtVisible.isVisible = false
        tempTxtEspacio.isVisible = false


        var i = 0
        while (i < listaingredientes.size){
            instanciarCheck(listaingredientes[i].toString(),i)
            i+=1
        }



    }

    fun instanciarCheck(NombreI : String, index : Int){

        db.collection("ingredientes").document(NombreI).collection("Ingredientes").get().addOnSuccessListener { doc ->
            var j = 0
            var linear1 : LinearLayout = findViewById<LinearLayout>(R.id.linear1)
            var linear2 : LinearLayout = findViewById<LinearLayout>(R.id.linear2)

            while (j < doc.size()){
                if (j == 0){
                    var tempTxtvisible = TextView(TxtVisible)
                    var tempTxtEspacio = TextView(TxtEspacio)
                    tempTxtvisible.setTextColor(Color.WHITE)
                    tempTxtvisible.textSize = 20F
                    tempTxtvisible.setText(NombreI+" :")

                    tempTxtEspacio.textSize = 20F
                    tempTxtEspacio.setText("           ")

                    linear1.addView(tempTxtvisible)
                    linear2.addView(tempTxtEspacio)
                }

                var tempCheck1 : CheckBox = CheckBox(Check1)
                var tempCheck2 : CheckBox = CheckBox(Check2)

                if ( j%2==0 || j == 0){
                    tempCheck1.id = j
                    tempCheck1.textSize = 20F
                    tempCheck1.setTextColor(Color.WHITE)
                    tempCheck1.setText(doc.documents.get(j).id)

                    linear1.addView(tempCheck1)
                    listaCheck.add(tempCheck1)
                    if (j == doc.size()-1){
                        var tempTxtEspacio2 = TextView(TxtEspacio)

                        tempTxtEspacio2.textSize = 20F
                        tempTxtEspacio2.setText("           ")
                        linear2.addView(tempTxtEspacio2)
                    }
                }else{
                    if ( j%2!=0 ){
                        tempCheck2.id = j
                        tempCheck2.textSize = 20F
                        tempCheck2.setTextColor(Color.WHITE)
                        tempCheck2.setText(doc.documents.get(j).id)

                        linear2.addView(tempCheck2)
                        listaCheck.add(tempCheck2)
                    }
                }
                j+=1
            }
        }.addOnFailureListener{
            Toast.makeText(this,"Error al cargar Ingredientes, Intenta mas tarde", Toast.LENGTH_SHORT).show()

        }
    }

    fun btn_buscarReceta(p0: View?){
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Cargando...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        var tempArray = ArrayList<String?>()
        var tempArray2 = ArrayList<String?>()

        var count = 1
        for (check in listaCheck){
            if (check.isChecked){
                tempArray.add(check.text.toString().lowercase())
                println("ingrediente "+ count + " : "+ check.text.toString().lowercase())
                count +=1

            }
        }

        var i = 0
        var j = 0
        var k = 0
        var salir = false

        newListaRecetas.clear()
        println(listaNombresR.size)
        if (tempArray.isNotEmpty()){
            while (j < listaNombresR.size){
                var tempnombre = listaNombresR[j].toString().lowercase()
                println(tempnombre)
                tempArray2.clear()
                db.collection("recetas").document(tempnombre).collection("Ingredientes").get().addOnSuccessListener { doc ->
                    println("            ")

                    tempArray2.clear()
                    for (a in doc.documents){
                        tempArray2.add(a.id)
                    }
                    if (tempArray2.isNotEmpty()){
                        var tempResult : String? = filtro( tempnombre , tempArray , tempArray2)
                        if ( tempResult != null && tempResult.isNotEmpty()) {
                            println("Receta Agregada: "+tempResult)
                            newListaRecetas.add(tempResult)
                        }
                        i+=1
                    }



                }.addOnFailureListener {
                    Toast.makeText(this,"Algo Salio Mal :c", Toast.LENGTH_SHORT).show()
                }.addOnCanceledListener{
                    Toast.makeText(this,"Algo Salio Mal :c", Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener {

                    if (i == listaNombresR.size  ){
                        println("Lista : "+newListaRecetas)
                        if (newListaRecetas.isNotEmpty()){
                            var tempRecetas = Intent(this, Recetas2::class.java)
                            tempRecetas.putExtra("listanombres", newListaRecetas)
                            startActivity(tempRecetas)
                        }

                    }
                }
                j+=1

                if (j == listaNombresR.size-1 ){
                    if (progressDialog.isShowing){
                        progressDialog.dismiss()
                    }

                }
            }

        } else{
            if (progressDialog.isShowing){
                progressDialog.dismiss()
            }
            Toast.makeText(this,"Debe seleccionar al menos 1 ingrediente", Toast.LENGTH_SHORT).show()

        }

    }


    // -------------- Filtra las recetas con los ingredientes de cada una y los ingredientes seleccionados ------------------------
    fun filtro(nombreR : String, misIng : ArrayList<String?>, dbIng : ArrayList<String?>): String?{

        var k = 0
        var i = 0
        while (i < misIng.size){
            k = 0
            while (k < dbIng.size){
                //println(dbIng[k].toString().lowercase() + " = " + misIng[i].toString().lowercase())
                if (dbIng[k].toString().lowercase().contains(misIng[i].toString().lowercase())){
                    //println("Nuevo : "+nombreR)
                    return nombreR
                }
                k += 1
            }
            i += 1
        }
        return null
    }




}