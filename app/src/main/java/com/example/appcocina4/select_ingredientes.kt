package com.example.appcocina4

import android.app.ProgressDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.size
import com.google.firebase.firestore.FirebaseFirestore

class select_ingredientes : AppCompatActivity() {
    var listaingredientes = ArrayList<String?>()
    var listaNombresR = ArrayList<String?>()
    var db = FirebaseFirestore.getInstance()
    var tipo : Int = -1


    var Check1 = baseContext
    var Check2 = baseContext
    var TxtVisible = baseContext
    var TxtEspacio = baseContext


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_ingredientes)

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
                    }
                }
                j+=1
            }
        }.addOnFailureListener{
            Toast.makeText(this,"Error al cargar Ingredientes, Intenta mas tarde", Toast.LENGTH_SHORT).show()

        }
    }

    fun btn_buscarReceta(p0: View?){
//        val progressDialog = ProgressDialog(this)
//        progressDialog.setMessage("Cargando...")
//        progressDialog.setCancelable(false)
//        progressDialog.show()
        var linear1 : LinearLayout = findViewById<LinearLayout>(R.id.linear1)
        var linear2 : LinearLayout = findViewById<LinearLayout>(R.id.linear2)
        var tempArray = ArrayList<String?>()
        var i = 0
        while ( i < linear1.size){
            if (linear1.get(i).isPressed){
                tempArray.add(linear1.get(i).id.toString())
            }
        }
        i = 0
        while ( i < linear2.size){
            if (linear2.get(i).isPressed){
                tempArray.add(linear2.get(i).id.toString())
            }
        }

        for (a in tempArray){
            println(a)
        }

//        for (l in listaNombresR){
//
//            db.collection("recetas").document(l.toString().lowercase()).collection("Ingredientes").get().addOnSuccessListener {
//
//            }.addOnFailureListener {
//
//            }
//        }



    }






}