package com.example.appcocina4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import com.google.android.material.chip.ChipGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class Select_Ingredientes : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    var db_Storage = Firebase.storage.reference

    var listaingredientes = ArrayList<String?>()
    var listaIng: ArrayList<String> = ArrayList()
    var listaCheckBox = ArrayList<CheckBox>()
    var linearpasos :TableLayout? = null

    var temptablecontext1 = baseContext
    var temptablecontext2 = baseContext
    var tempchipgroup1 = baseContext
    var temptablecontexttxtipoing = baseContext
    var temptablecontexttxtingredientes1 = baseContext
    var temptablecontexttxtingredientes2 = baseContext
    var temptablecontexttxtingredientes3 = baseContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_ingredientes)

//        var tempTextView = findViewById<TextView>(R.id.txt_tipoing)
//        var tempCheckBox1 = findViewById<CheckBox>(R.id.checkBoxTemp1)
//        var tempCheckBox2 = findViewById<CheckBox>(R.id.checkBoxTemp2)
//        var tempCheckBox3 = findViewById<CheckBox>(R.id.checkBoxTemp3)
//        var temptablett1 = findViewById<TableRow>(R.id.TablaTxt)
//        var temptablett2 = findViewById<TableRow>(R.id.TablaCheck)
//        var tempchipGroup = findViewById<ChipGroup>(R.id.Chipgroup)

//        temptablecontext1 = temptablett1.context
//        temptablecontext2 = temptablett2.context
//        temptablecontexttxtingredientes1 = tempCheckBox1.context
//        temptablecontexttxtingredientes2 = tempCheckBox2.context
//        temptablecontexttxtingredientes3 = tempCheckBox3.context
//        temptablecontexttxtipoing = tempTextView.context
//        tempchipgroup1 = tempchipGroup.context

//        linearpasos = findViewById<TableLayout>(R.id.LinearIngredientes)

        listaingredientes = intent.getStringArrayListExtra("listaingredientes") as ArrayList<String?>

//        tempCheckBox1.isVisible = false
//        tempCheckBox2.isVisible = false
//        tempCheckBox3.isVisible = false
//
//        temptablett1.isVisible = false
////        temptablett2.isVisible = false
//        tempTextView.isVisible = false


        instanciarCheckBox()



    }

    fun instanciarCheckBox(){
        listaCheckBox.clear()
        //checkBoxes(listaingredientes)
        listview()

    }



    fun listview(){
        var listView = findViewById<ListView>(R.id.listviews)
        var a = findViewById<TextView>(R.id.txtprueba)
        a.setText("asdasdasdsadasd : ")
        listView.addHeaderView(a)
        var arr = ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,listaingredientes)
        listView.adapter = arr

    }

    fun checkBoxes(listanombres : ArrayList<String?>){

        var index : Int= 0


        linearpasos?.removeAllViewsInLayout()

        for (l in listanombres){

            //obtenerIngredientes(linearpasos,l.toString(), tempingrediente)

            db.collection("ingredientes").document(l.toString()).collection("Ingredientes").get().addOnSuccessListener { inst ->





                var count = 0



                for (document in inst){

                    listaIng.add(document.id)

//                    if (count == 0){
//                        var temptipoing1: CheckBox = CheckBox(temptablecontexttxtingredientes1)
//                        temptipoing1.id = count
//
//                        temptipoing1.textSize = 20F
//                        temptipoing1.width = 1000
//
//                        temptipoing1.isVisible = true
//                        temptipoing1.text = document.id.toString()
//                        println("Ingrediente :" +document.id.toString())
//
//                        tempTabla2.addView(temptipoing1)
//
//                        if (tcount == tamano){
//                            linearpasos.addView(tempTabla2,index+1)
//                            tempTabla2.removeAllViewsInLayout()
//                            count = -1
//                        }
//                        tcount+=1
//                    }
//                    if (count == 1){
//                        var temptipoing2: CheckBox = CheckBox(temptablecontexttxtingredientes2)
//                        temptipoing2.id = count
//
//                        temptipoing2.textSize = 20F
//                        temptipoing2.width = 1000
//
//                        temptipoing2.isVisible = true
//                        temptipoing2.text = document.id.toString()
//                        println("Ingrediente :" +document.id.toString())
//
//                        tempTabla2.addView(temptipoing2)
//
//                        if (tcount == tamano){
//                            linearpasos.addView(tempTabla2,index+1)
//                            tempTabla2.removeAllViewsInLayout()
//                            count = -1
//                        }
//                        tcount+=1
//                    }
//                    if (count == 2){
//                        var temptipoing3: CheckBox = CheckBox(temptablecontexttxtingredientes3)
//                        temptipoing3.id = count
//
//                        temptipoing3.textSize = 20F
//                        temptipoing3.width = 1000
//
//                        temptipoing3.isVisible = true
//                        temptipoing3.text = document.id.toString()
//                        println("Ingrediente :" +document.id.toString())
//
//                        tempTabla2.addView(temptipoing3)
//                        linearpasos.addView(tempTabla2,index+1)
//                        tempTabla2.removeAllViewsInLayout()
//
//                        index+=1
//                        count = -1
//                        tcount+=1
//                    }



                    count+=1
                }
                obtenerIngredientes(listaIng,index,l.toString())

                index+=1
                count = 0




            }.addOnFailureListener { _ ->
                println("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa en numero")
            }


        }

    }

    fun obtenerIngredientes(lista : ArrayList<String>, index : Int, tipo : String){
        var i = 1
        var tempTabla2: TableRow = TableRow(temptablecontext2)
        var tempTabla1: TableRow = TableRow(temptablecontext1)
        var tempingrediente: TextView = TextView(temptablecontexttxtipoing)
        var tempChip : ChipGroup = ChipGroup(tempchipgroup1)

        var tempList : ArrayList<String>

        tempList = lista.clone() as ArrayList<String>
        tempingrediente.id = index
        tempingrediente.textSize = 24F
        tempingrediente.setText(tipo.toString()+" : ")

        tempingrediente.isVisible = true


        tempTabla1.addView(tempingrediente)

        linearpasos?.addView(tempTabla1,index)
        while (i <= tempList.size){
            var temptipoing3: CheckBox = CheckBox(temptablecontexttxtingredientes3)
            temptipoing3.id = i

            temptipoing3.textSize = 20F
            temptipoing3.width = 1000

            temptipoing3.isVisible = true
            temptipoing3.text = tempList[i-1]
            println(" "+i+" - "+tipo + " : " +tempList[i-1] + "  index : "+ index)

            tempTabla2.addView(temptipoing3)



            if (i == tempList.size){
                linearpasos?.addView(tempTabla2,index)

            }


            i+=2
        }


        tempTabla2 = TableRow(temptablecontext2)
        listaIng.clear()




    }



}