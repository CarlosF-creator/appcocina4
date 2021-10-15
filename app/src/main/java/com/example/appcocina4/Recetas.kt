package com.example.appcocina4

import android.content.Intent
import android.media.Image
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.ViewAnimator
import androidx.annotation.Dimension
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.size
import com.google.firebase.firestore.FirebaseFirestore

class Recetas() : AppCompatActivity() {
    var nombresRecetas = ArrayList<String?>()
    var listabotones = ArrayList<Button>()
    var portadas = ArrayList<Image?>()
    var db = FirebaseFirestore.getInstance()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recetas)
        nombresRecetas = intent.getStringArrayListExtra("listanombres") as ArrayList<String?>

        instanciarBotones()

    }

    fun instanciarBotones(){
        var listaRecetas = ArrayList<String>()
        botones(nombresRecetas)
        var botonreceta = findViewById<Button>(R.id.btndereceta)
        botonreceta.isVisible = false
        //println(hola)
    }

    fun btndeRecetas(p0: View?){
        var radiobutton = findViewById<RadioGroup>(R.id.radiobutton)
        var a :Int= 0

        while (a < radiobutton.size){
            val seleccion  = radiobutton[a]
            if (seleccion.isPressed){
                for (btn in listabotones){
                    if (btn.id == seleccion.id){
                        var prereceta = Intent(this, pre_receta::class.java)
                        prereceta.putExtra("nombre", btn.text)
                        startActivity(prereceta)
                        break
                    }
            }
                break
            }
            a+=1
        }






    }



    fun botones(listanombres : ArrayList<String?>){
        var radiobutton = findViewById<RadioGroup>(R.id.radiobutton)
        var index : Int= 0
        for (l in listanombres){
            var tempbtn = crearBoton()
            tempbtn.id = index
            tempbtn.text = l

            listabotones.add(tempbtn)
            radiobutton.addView(tempbtn,index)
            index+=1
        }

    }

    fun crearBoton():Button{
        var btnreceta = findViewById<Button>(R.id.btndereceta)
        var tempBtn : Button = Button(btnreceta.context)


        tempBtn.height = 400
        tempBtn.width = 735
        tempBtn.textSize = 20F
        tempBtn.setOnClickListener { btndeRecetas(p0 = View(this)) }
        tempBtn.setPadding(0,170,0,0)


        return tempBtn

    }

    fun obtenerNombres(){
        var temp = ArrayList<String?>()
        db.collection("recetas").get().addOnSuccessListener { documento ->
            for (d in documento){
                nombresRecetas.add(d.id)
            }
        }
    }
}