package com.example.appcocina4

import android.app.ActionBar
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
import android.service.autofill.FieldClassification
import android.view.Gravity
import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.ViewGroup
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

class Recetas2() : AppCompatActivity() , SearchView.OnQueryTextListener {
    var nombresRecetas = ArrayList<String?>()
    var recetasOriginales = ArrayList<String?>()
    var listabotones = ArrayList<Button>()
    var portadas = ArrayList<Image?>()
    var db = FirebaseFirestore.getInstance()
    var db_Storage = Firebase.storage.reference
    var context_txtRecetas = baseContext
    var context_txtEspacio = baseContext
    var context_btn = baseContext
    var linearlay = baseContext

    var tempArray = ArrayList<String?>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recetas3)
        nombresRecetas = intent.getStringArrayListExtra("listanombres") as ArrayList<String?>
        recetasOriginales = intent.getStringArrayListExtra("listanombres") as ArrayList<String?>

        var titulo = intent.getStringExtra("Titulo")

        var tituloView = findViewById<TextView>(R.id.titulo)

        tituloView.setText(titulo.toString())

        var botonreceta = findViewById<Button>(R.id.btndereceta2)
        var temptxtreceta = findViewById<TextView>(R.id.txt_Receta2)
        var temptxtespacio = findViewById<TextView>(R.id.txtEspacio3)





        context_btn = botonreceta.context
        context_txtRecetas = temptxtreceta.context
        context_txtEspacio = temptxtespacio.context


        botonreceta.isVisible = false
        temptxtespacio.isVisible = false
        temptxtreceta.isVisible = false

        var txtBuscar = findViewById<SearchView>(R.id.Buscador)

        instanciarBotones()

        txtBuscar.setOnQueryTextListener(this)


    }

    fun Buscador(txtBuscar : String){
        var largo = txtBuscar.length
        var radiobutton = findViewById<LinearLayout>(R.id.radiobutton)

        if (largo == 0){
            instanciarBotones()
        } else{
            tempArray.clear()
            for (l in nombresRecetas){
                if (l.toString().lowercase().contains(txtBuscar.lowercase())){
                    tempArray.add(l.toString().lowercase())
                }
            }
            listabotones.clear()
            botones(tempArray)
        }
    }

    fun instanciarBotones(){
        listabotones.clear()

        botones(nombresRecetas)

    }

    fun filtrar(p0: View?){

        //var texto = buscar.text;

        //if(texto.contains("l")){

        //    nombresRecetas.remove("lasaña")
        //}

        //PARA REINICIAR LA VISTA ( BORRAR LOS DATOS )
        // val intent = intent
        //finish()
        //startActivity(intent)
    }

    fun btndeRecetas(p0: View?){
        var linearL1 = findViewById<LinearLayout>(R.id.Linear1)
        var linearL2 = findViewById<LinearLayout>(R.id.Linear2)
        var a : Int= 0
        var b : Int=0

        while (a < linearL1.size){
            val seleccion  = linearL1[a]
            if (seleccion.isPressed){
                var countA : Int= 0
                for (btn in listabotones){
                    if (countA % 2 == 0 || countA == 0 ){
                        if (btn.id == seleccion.id){
                            var prereceta = Intent(this, pre_receta::class.java)
                            prereceta.putExtra("nombre", btn.text.toString())
                            println("asdasdasdasdas dasdasdasdas das das : " +btn.text)
                            startActivity(prereceta)
                            break
                        }
                    }
                    countA+=1
                }
                break
            }
            a+=1
        }
        while (b < linearL2.size){
            val seleccion  = linearL2[b]
            if (seleccion.isPressed){
                var countB : Int= 0
                for (btn in listabotones){
                    if (countB % 2 != 0 || countB != 0 ){
                        if (btn.id == seleccion.id){
                            var prereceta = Intent(this, pre_receta::class.java)
                            prereceta.putExtra("nombre", btn.text.toString())
                            println("asdasdasdasdas dasdasdasdas das das : " +btn.text)
                            startActivity(prereceta)
                            break
                        }
                    }
                    countB+=1
                }
                break
            }
            b+=1

        }
    }



    fun botones(listanombres : ArrayList<String?>){
        val linearL1 = findViewById<LinearLayout>(R.id.Linear1)
        val linearL2 = findViewById<LinearLayout>(R.id.Linear2)
        linearL1.removeAllViewsInLayout()
        linearL2.removeAllViewsInLayout()
        var index : Int= 0
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


            if (listabotones.size % 2 == 0 || listabotones.size == 0) {
                println("text :" + temptxt.text + " index:" + index)
                listabotones.add(tempbtn)
                linearL1.addView(temptxt,index)
                linearL1.addView(tempbtn,index+1)
                linearL1.addView(tempEspacio, index + 2)
                index+=3
            } else {
                println("text :" + temptxt.text + " index:" + index)
                listabotones.add(tempbtn)
                linearL2.addView(temptxt, (index) - 3)
                linearL2.addView(tempbtn, (index + 1) - 3)
                linearL2.addView(tempEspacio, (index + 2) - 3)
            }



        }

    }


    fun crearBoton():Button{

        var tempBtn : Button = Button(context_btn)


        tempBtn.layoutParams = LinearLayout.LayoutParams(700,350)
        //tempBtn.width = 600

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

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Buscador(newText.toString())
        return false
    }

}


