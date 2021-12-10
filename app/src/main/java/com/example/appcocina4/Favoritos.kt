package com.example.appcocina4

import android.app.ProgressDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View.LAYOUT_DIRECTION_LOCALE
import android.text.*
import android.media.Image
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.*
import androidx.annotation.Dimension
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.size
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class Favoritos() : AppCompatActivity() {
    var nombresRecetas = ArrayList<String?>()
    var favoritos = ArrayList<String?>()
    var listabotones = ArrayList<Button>()
    var portadas = ArrayList<Image?>()
    var db = FirebaseFirestore.getInstance()
    var db_Storage = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recetas)
        favoritos = intent.getStringArrayListExtra("listafavoritos") as ArrayList<String?>
        nombresRecetas = intent.getStringArrayListExtra("listanombres") as ArrayList<String?>

        println("Favoritos: ${favoritos}")
        println("Nombres Recetas: ${nombresRecetas}")

        instanciarBotones()
    }

    fun instanciarBotones() {
        listabotones.clear()
        botones(favoritos)
        var botonreceta = findViewById<Button>(R.id.btndereceta)
        botonreceta.isVisible = false
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
                        prereceta.putExtra("nombre", btn.text.toString())
                        println("asdasdasdasdas dasdasdasdas das das : " +btn.text)
                        startActivity(prereceta)
                        break
                    }
                }
                break
            }
            a+=1
        }
    }

    fun botones(listanombres: ArrayList<String?>) {
        var radiobutton = findViewById<RadioGroup>(R.id.radiobutton)
        var index: Int = 0
        for (l in listanombres) {
            var tempbtn = crearBoton()
            tempbtn.id = index
            tempbtn.setTextColor(android.R.color.primary_text_dark_nodisable!!)
            tempbtn.setTextColor(getColor(R.color.design_default_color_on_secondary))
            tempbtn.setText(l, TextView.BufferType.SPANNABLE)

            obtenerImagenBtn(l.toString(), tempbtn)
            listabotones.add(tempbtn)
            radiobutton.addView(tempbtn, index)
            radiobutton.addView(TextView(baseContext))
            index += 1
        }
    }

    fun crearBoton():Button{
        var btnreceta = findViewById<Button>(R.id.btndereceta)
        var tempBtn : Button = Button(btnreceta.context)


        tempBtn.height = 400
        tempBtn.width = 700
        tempBtn.textSize = 30F
        tempBtn.setOnClickListener { btndeRecetas(p0 = View(this)) }
        tempBtn.setPadding(0,0,0,0)


        return tempBtn

    }
    fun obtenerImagenBtn(nombreR: String, tempbtn: Button) {

        var tempNombre = traductordeÑ(nombreR).lowercase()
        var referencia = db_Storage.child("fotos_recetas/$nombreR/$tempNombre" + "P.jpg")
        if (referencia == null) {
            println("xd")
        }
        val localfile2 = File.createTempFile(tempNombre + "P", ".jpg")
        referencia.getFile(localfile2).addOnSuccessListener {
            tempbtn.background = Drawable.createFromPath(localfile2.absolutePath)

        }.addOnFailureListener {
            Toast.makeText(this, "Fallo en la carga de imagenes", Toast.LENGTH_SHORT).show()


        }
    }

    fun traductordeÑ(nombreR: String): String {
        var tempNombre = ""
        for (n in nombreR) {
            if (n.equals('ñ')) {
                tempNombre = tempNombre + 'n'
            } else {
                tempNombre = tempNombre + n
            }
        }
        return tempNombre
    }
}