package com.example.appcocina4

import android.app.ProgressDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View.LAYOUT_DIRECTION_LOCALE
import android.text.*
import android.media.Image
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.Gravity
import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
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
import kotlinx.android.synthetic.main.activity_recetas.*
import org.w3c.dom.Text
import java.io.File

class Recetas() : AppCompatActivity() {
    var nombresRecetas = ArrayList<String?>()
    var recetasOriginales = ArrayList<String?>()
    var listabotones = ArrayList<Button>()
    var portadas = ArrayList<Image?>()
    var db = FirebaseFirestore.getInstance()
    var db_Storage = Firebase.storage.reference
    var context_txtRecetas = baseContext
    var context_txtEspacio = baseContext



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recetas)
        nombresRecetas = intent.getStringArrayListExtra("listanombres") as ArrayList<String?>
        recetasOriginales = intent.getStringArrayListExtra("listanombres") as ArrayList<String?>;

        context_txtRecetas = findViewById<TextView>(R.id.txt_Receta).context
        context_txtEspacio = findViewById<TextView>(R.id.txtEspacio).context

        instanciarBotones()

    }

    fun instanciarBotones(){
        listabotones.clear()
        botones(nombresRecetas)
        var botonreceta = findViewById<Button>(R.id.btndereceta)
        findViewById<TextView>(R.id.txt_Receta).isVisible = false
        botonreceta.isVisible = false
    }

    fun filtrar(p0: View?){

        var texto = buscar.text;

        if(texto.contains("l")){

            nombresRecetas.remove("lasaña")
        }

        //PARA REINICIAR LA VISTA ( BORRAR LOS DATOS )
        val intent = intent
        finish()
        startActivity(intent)
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



    fun botones(listanombres : ArrayList<String?>){
        var radiobutton = findViewById<RadioGroup>(R.id.radiobutton)
        var index : Int= 1
        for (l in listanombres){
            var temptxt = TextView(context_txtRecetas)
            var tempEspacio = TextView(context_txtEspacio)
            tempEspacio.textSize = 17F


            temptxt.gravity = Gravity.CENTER
            temptxt.setTextColor(Color.WHITE)
            temptxt.fontFeatureSettings = R.font.anton.toString()
            temptxt.setBackgroundResource(R.color.common_google_signin_btn_text_dark_disabled!!)


            temptxt.setText(l.toString().uppercase())

            temptxt.textSize = 24F



            var tempbtn = crearBoton()

            tempbtn.id = index+1
            tempbtn.setText(l.toString())

            tempbtn.textSize = 0F


            obtenerImagenBtn(l.toString(), tempbtn)




            listabotones.add(tempbtn)
            radiobutton.addView(temptxt,index)
            radiobutton.addView(tempbtn,index+1)
            radiobutton.addView(tempEspacio,index+2)

            index+=3
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

    fun obtenerImagenBtn(nombreR: String, tempbtn : Button){

        var tempNombre = traductordeÑ(nombreR).lowercase()
        var referencia = db_Storage.child("fotos_recetas/$nombreR/$tempNombre"+"P.jpg")
        if(referencia == null){
            println("xd")
        }
        val localfile2 = File.createTempFile(tempNombre+"P",".jpg")
        referencia.getFile(localfile2).addOnSuccessListener {
            tempbtn.background = Drawable.createFromPath(localfile2.absolutePath)


        }.addOnFailureListener{
            Toast.makeText(this,"Fallo en la carga de imagenes", Toast.LENGTH_SHORT).show()


        }
    }
    fun traductordeÑ(nombreR : String):String{
        var tempNombre = ""
        for (n in nombreR){
            if (n.equals('ñ')){
                tempNombre = tempNombre + 'n'
            } else{
                tempNombre = tempNombre + n
            }
        }
        return tempNombre
    }
}