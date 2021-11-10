package com.example.appcocina4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.size

class Select_Ingredientes : AppCompatActivity() {

    var listaingredientes = ArrayList<String?>()
    var listaCheckBox = ArrayList<CheckBox>()

    var temptablecontext1 = baseContext
    var temptablecontext2 = baseContext
    var temptablecontexttxtipoing = baseContext
    var temptablecontexttxtingredientes = baseContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_ingredientes)

        var tempTextView = findViewById<TextView>(R.id.txt_tipoing)
        var tempCheckBox = findViewById<CheckBox>(R.id.checkBoxTemp)
        var temptablett1 = findViewById<TableRow>(R.id.TablaTxt)
        var temptablett2 = findViewById<TableRow>(R.id.TablaCheck)

        temptablecontext1 = temptablett1.context
        temptablecontext2 = temptablett2.context
        temptablecontexttxtingredientes = tempCheckBox.context
        temptablecontexttxtipoing = tempTextView.context

        listaingredientes = intent.getStringArrayListExtra("listaingredientes") as ArrayList<String?>

        tempCheckBox.isVisible = false
        tempTextView.isVisible = false
        temptablett1.isVisible = false
        temptablett2.isVisible = false


        instanciarCheckBox()



    }

    fun instanciarCheckBox(){
        listaCheckBox.clear()
        checkBoxes(listaingredientes)

    }




    fun checkBoxes(listanombres : ArrayList<String?>){
        var linearpasos = findViewById<TableLayout>(R.id.LinearIngredientes)
        var index : Int= 0
        var tempingrediente: TextView = TextView(temptablecontexttxtipoing)

        tempingrediente.id = index
        tempingrediente.textSize = 20F
        tempingrediente.setText("Carbohidratos : ")

        linearpasos.removeAllViews()

        for (l in listanombres){
            var tempTabla1: TableRow = TableRow(temptablecontext1)
            var tempTabla2: TableRow = TableRow(temptablecontext2)
            var temptipoing: CheckBox = CheckBox(temptablecontexttxtingredientes)



            temptipoing.id = index
            temptipoing.text = l
            temptipoing.textSize = 20F
            temptipoing.width = 1000

            temptipoing.isVisible = true
            tempingrediente.isVisible = true


            tempTabla2.addView(temptipoing)
            linearpasos.addView(tempTabla2)


            index+=1
        }

    }



}