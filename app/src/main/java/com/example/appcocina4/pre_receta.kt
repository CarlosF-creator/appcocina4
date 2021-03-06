package com.example.appcocina4
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
//import com.bumptech.glide.Glide
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.safetynet.SafetyNetApi.AttestationResponse
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.appcheck.FirebaseAppCheck
//import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
class pre_receta : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var db_Storage = Firebase.storage.reference
    var listapasos = ArrayList<String?>()
    var listaimagenes = ArrayList<Bitmap?>()
    var pasos_totales = -1
    var descripcion = ""
    var tempbitmap : Bitmap? = null
    var nombreR : String? = null
    var Tcount = 0
    var estrellas: RatingBar? = null

    var Check1 = baseContext
    var TxtIng = baseContext
    var linearlay = baseContext

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_receta)
        var tempcheck = findViewById<CheckBox>(R.id.checkBoxing)
        var temptextview = findViewById<TextView>(R.id.textViewdetalle)
        var linear1 : LinearLayout = findViewById<LinearLayout>(R.id.linearing1)

        Check1 = tempcheck.context
        TxtIng = temptextview.context
        linearlay = linear1.context

        linear1.isVisible = false
        tempcheck.isVisible = false
        temptextview.isVisible = false

        var nombre : String? = "no se encontro"
        var nombreweno : String = "no se encontro"
        nombre = intent.getStringExtra("nombre")
        if (nombre != null){
            nombreweno = nombre.toString()
            nombreR = nombreweno
        }else{
            println("Nombre Null")
        }

         estrellas = findViewById<RatingBar>(R.id.ratingBar)



        obtenerEvaluaciones()
        obtenerNumeroPasos(nombreweno)
        rellenarDatos(nombre)
        obtenerDescripcion(nombreweno)
        obtenerTpreparacion(nombreweno)
        obtenerListaPasos(nombreweno)
        obtenerIngredientes(nombreweno)
    }
    //Boton Cocinar
    fun btnCocinar(p0: View?) {
        if (listaimagenes.isEmpty()){
            println("la wea mala")
        }

        var pasoapaso = Intent(this, Pasoapaso::class.java)
        pasoapaso.putExtra("lista", listapasos)
        pasoapaso.putExtra("num", pasos_totales)
        pasoapaso.putExtra("imagenes", listaimagenes)
        pasoapaso.putExtra("nombreR", nombreR)
        startActivity(pasoapaso)
    }
    //Coloca el nombre de la receta entre otras cosas
    fun rellenarDatos(nombre: String?){
        var Titulo = findViewById<TextView>(R.id.Titulo)
        var newTitulo = ""
        if (nombre != null){
            newTitulo = nombre
        }
        Titulo.text = Mayusculas(newTitulo)
    }
    fun Mayusculas(nombreR: String):String {
        var index = 0
        var espacio = false
        var tempNombre : String = ""
        for (n in nombreR){
            if (index == 0 || espacio){
                tempNombre = tempNombre + n.uppercaseChar()
                espacio = false
            }else{
                tempNombre = tempNombre + n
                if (n == ' '){
                    espacio = true
                }
            }
            index += 1
        }
        return tempNombre
    }
    //Obtenemos los pasos desde la base de Datos
    fun obtenerListaPasos(nombreR : String) {
        if (nombreR != "no se encontro"){
        db.collection("recetas").document(nombreR).collection("Info").document("Instrucciones")
            .get().addOnSuccessListener { inst ->
            var tempnum = 1
            var Pasos = inst.toObject(Instrucciones::class.java)
            listapasos = Pasos?.let { obtenerInstrucciones(it) }!!
        }.addOnFailureListener { _ ->
            println("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        }}else{
            print("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        }
    }
    //Obtenemos el numero de pasos desde la base de datos
    fun obtenerNumeroPasos(nombreR : String) {
        if (nombreR != "no se encontro") {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Cargando...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            db.collection("recetas").document(nombreR).get().addOnSuccessListener { inst ->
                val num : String?= inst.data?.get("numeroPasos").toString()
                if (num != null){
                    pasos_totales = num.toInt()
                    println(pasos_totales)
                    //obtenerImagenes(nombreR)
                    obtenerImagenPortada(nombreR,progressDialog)
                }else{
                    print("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa en numero")
                }
            }.addOnFailureListener { _ ->
                println("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa en numero")
            }
        }else{
            print("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa en numero")
        }
    }
    //Obtiene el tiempo de duracion de la receta
    fun obtenerTpreparacion(nombreR : String) {
        if (nombreR != "no se encontro") {
            db.collection("recetas").document(nombreR).get().addOnSuccessListener { inst ->
                val tiempo : String?= inst.data?.get("tPreparacion").toString()
                if (tiempo != null){
                    var txt_tiempo = findViewById<TextView>(R.id.Txt_Tpreparacion)
                    var temp = tiempo.toInt()
                    txt_tiempo.text = "Tiempo de Preparacion: "+ temp +"min"
                }
            }.addOnFailureListener { _ ->
                println("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa en numero")
            }
        }else{
            print(nombreR)
        }
    }
    //Obtiene la Descripcion de la receta
    fun obtenerDescripcion(nombreR : String){
        if (nombreR != "no se encontro") {
            db.collection("recetas").document(nombreR).get().addOnSuccessListener { inst ->
                val des = inst.data?.get("descripcion").toString()
                //SystemClock.sleep(500)
                var txt_des = findViewById<TextView>(R.id.Txt_Descripcion)
                if (des != null){
                    txt_des.text = des
                }
            }.addOnFailureListener { _ ->
                println("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa en numero")
            }
        }else{
            print("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa en numero")
        }
    }
    //Descomponemos el Map que obtuvimos de Firebase
    fun obtenerInstrucciones(inst: Instrucciones): ArrayList<String?> {
        val tempArray = ArrayList<String?>()
        if (inst.paso1 != null) {
            tempArray.add(inst.paso1)
        }
        if (inst.paso2 != null) {
            tempArray.add(inst.paso2)
        }
        if (inst.paso3 != null) {
            tempArray.add(inst.paso3)
        }
        if (inst.paso4 != null) {
            tempArray.add(inst.paso4)
        }
        if (inst.paso5 != null) {
            tempArray.add(inst.paso5)
        }
        if (inst.paso6 != null) {
            tempArray.add(inst.paso6)
        }
        if (inst.paso7 != null) {
            tempArray.add(inst.paso7)
        }
        if (inst.paso8 != null) {
            tempArray.add(inst.paso8)
        }
        if (inst.paso9 != null) {
            tempArray.add(inst.paso9)
        }
        if (inst.paso10 != null) {
            tempArray.add(inst.paso10)
        }
        if (inst.paso11 != null) {
            tempArray.add(inst.paso11)
        }
        if (inst.paso12 != null) {
            tempArray.add(inst.paso12)
        }
        if (inst.paso13 != null) {
            tempArray.add(inst.paso13)
        }
        if (inst.paso14 != null) {
            tempArray.add(inst.paso14)
        }
        if (inst.paso15 != null) {
            tempArray.add(inst.paso15)
        }
        if (inst.paso16 != null) {
            tempArray.add(inst.paso16)
        }
        if (inst.paso17 != null) {
            tempArray.add(inst.paso17)
        }
        if (inst.paso18 != null) {
            tempArray.add(inst.paso18)
        }
        if (inst.paso19 != null) {
            tempArray.add(inst.paso19)
        }
        if (inst.paso20 != null) {
            tempArray.add(inst.paso20)
        }
        if (inst.paso21 != null) {
            tempArray.add(inst.paso21)
        }
        if (inst.paso22 != null) {
            tempArray.add(inst.paso22)
        }
        if (inst.paso23 != null) {
            tempArray.add(inst.paso23)
        }
        if (inst.paso24 != null) {
            tempArray.add(inst.paso24)
        }
        if (inst.paso25 != null) {
            tempArray.add(inst.paso25)
        }
        if (inst.paso26 != null) {
            tempArray.add(inst.paso26)
        }
        if (inst.paso27 != null) {
            tempArray.add(inst.paso7)
        }
        if (inst.paso28 != null) {
            tempArray.add(inst.paso8)
        }
        if (inst.paso29 != null) {
            tempArray.add(inst.paso9)
        }
        if (inst.paso30 != null) {
            tempArray.add(inst.paso10)
        }
        if (inst.paso31 != null) {
            tempArray.add(inst.paso11)
        }
        if (inst.paso32 != null) {
            tempArray.add(inst.paso12)
        }
        if (inst.paso33 != null) {
            tempArray.add(inst.paso13)
        }
        if (inst.paso34 != null) {
            tempArray.add(inst.paso14)
        }
        if (inst.paso35 != null) {
            tempArray.add(inst.paso15)
        }
        if (inst.paso36 != null) {
            tempArray.add(inst.paso16)
        }
        if (inst.paso37 != null) {
            tempArray.add(inst.paso17)
        }
        if (inst.paso38 != null) {
            tempArray.add(inst.paso18)
        }
        if (inst.paso39 != null) {
            tempArray.add(inst.paso19)
        }
        if (inst.paso40 != null) {
            tempArray.add(inst.paso20)
        }
        return tempArray
    }

    fun obtenerImagenPortada(nombreR: String,pr : ProgressDialog){
        var tempNombre = traductorde??(nombreR).lowercase()
        var referencia = db_Storage.child("fotos_recetas/$nombreR/$tempNombre"+"P.jpg")
        if(referencia == null){
            println("xd")
        }
        val localfile2 = File.createTempFile(tempNombre+"P",".jpg")
        referencia.getFile(localfile2).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile2.absolutePath)
            findViewById<ImageView>(R.id.Imagen_Portada).setImageBitmap(bitmap)
            if (pr.isShowing){
                pr.dismiss()
            }
        }.addOnFailureListener{
            Toast.makeText(this,"Fallo en la carga de imagenes",Toast.LENGTH_SHORT).show()
            if (pr.isShowing){
                pr.dismiss()
            }
        }
    }
    fun traductorde??(nombreR : String):String{
        var tempNombre = ""
        for (n in nombreR){
            if (n.equals('??')){
                tempNombre = tempNombre + 'n'
            } else{
                tempNombre = tempNombre + n
            }
        }
        return tempNombre
    }

    fun obtenerEvaluaciones(){
        var promedio = 0
        var count = 0
        db.collection("evaluacion").get().addOnSuccessListener{ document ->
            for (d in document){
                promedio += d.getLong("puntuacion")!!.toInt();
                count++

            }
            document.count()

            estrellas!!.setRating((promedio/count).toFloat())
        }.addOnFailureListener{
            Toast.makeText(this,"No hay evaluaciones para esta receta", Toast.LENGTH_SHORT).show()
        }
    }
    fun obtenerIngredientes(nombreR : String){
        if (nombreR != "no se encontro") {
            db.collection("recetas").document(nombreR).collection("Ingredientes").get().addOnSuccessListener { doc ->
                var linearGrande : LinearLayout = findViewById<LinearLayout>(R.id.linearing)
                var j = 0
                while (j < doc.size()){

                    var tempLinearlay : LinearLayout = LinearLayout(linearlay)
                    var tempCheck1 : CheckBox = CheckBox(Check1)
                    var tempdetalle : TextView = TextView(TxtIng)


                    tempCheck1.id = j
                    tempCheck1.textSize = 22F
                    tempCheck1.setTextColor(Color.WHITE)
                    tempCheck1.setText(Mayusculas(doc.documents.get(j).id))


                    tempLinearlay.addView(tempCheck1)

                    tempdetalle.id = j
                    tempdetalle.textSize = 22.5F
                    tempdetalle.setTextColor(Color.WHITE)
                    tempdetalle.setText(" - "+doc.documents.get(j).get("detalle").toString())




                    tempLinearlay.addView(tempdetalle)

                    linearGrande.addView(tempLinearlay)


                    println(doc.documents.get(j).id+"  "+ doc.documents.get(j).get("detalle").toString())
                    j+=1
                }
            }.addOnFailureListener{
                Toast.makeText(this,"Error al cargar Ingredientes, Intenta mas tarde", Toast.LENGTH_SHORT).show()

            }
        }else{
            print("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa en numero")
        }
    }
    
}
