package com.example.appcocina4

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.safetynet.SafetyNetApi.AttestationResponse
import com.google.firebase.FirebaseApp
//import com.google.firebase.appcheck.FirebaseAppCheck
//import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File


class pre_receta : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var listapasos = ArrayList<String?>()
    var listaimagenes = ArrayList<Bitmap?>()
    var pasos_totales = -1
    var descripcion = ""
    var tempbitmap : Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_receta)

        var nombre : String? = "no se encontro"
        var nombreweno : String = "no se encontro"

        nombre = intent.getStringExtra("nombre")

        if (nombre != null){
            nombreweno = nombre.toString()
        }else{
            println("Nombre Null")
        }



//        Firebase.initialize(this)
//        val firebaseAppCheck = FirebaseAppCheck.getInstance()
//        val tempcosa = YourCustomAppCheckProviderFactory()
//        firebaseAppCheck.installAppCheckProviderFactory(tempcosa)

//        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(applicationContext)
//            == ConnectionResult.SUCCESS
//        ) {
//            SafetyNet.getClient(this).attest(
//                (Build.DEVICE + System.currentTimeMillis()).toByteArray(),
//                DatabaseC
//            )
//                .addOnSuccessListener { response: AttestationResponse? ->
//                    Log.v(
//                        "ATTEST",
//                        "SUCCESSFUL"
//                    )
//                }
//                .addOnFailureListener { e: Exception? ->
//                    if (e is ApiException) {
//                        // An error with the Google Play services API contains some
//                        // additional details.
//                        val apiException = e as ApiException
//                        // You can retrieve the status code using the
//                        // apiException.getStatusCode() method.
//                        Log.v("ATTEST", "ERROR" + apiException.getStatus().getStatusMessage())
//                    } else {
//                    }
//                    Log.v("ATTEST", "ERROR")
//                }
//        } else {
//            Toast.makeText(this, "Update Google Services", Toast.LENGTH_SHORT).show()
//        }
//        FirebaseApp.initializeApp( this)
//        val firebaseAppCheck = FirebaseAppCheck.getInstance()
//        firebaseAppCheck.installAppCheckProviderFactory(
//            SafetyNetAppCheckProviderFactory.getInstance()
//        )

        obtenerNumeroPasos(nombreweno)
        rellenarDatos(nombre)
        obtenerDescripcion(nombreweno)
        obtenerTpreparacion(nombreweno)
        obtenerListaPasos(nombreweno)





    }

    //Boton Cocinar
    fun btnCocinar(p0: View?) {
        if (listaimagenes.isEmpty()){
            println("la wea mala")
        }else{
            println("la wea wena mano")
        }

        var pasoapaso = Intent(this, Pasoapaso::class.java)
        pasoapaso.putExtra("lista", listapasos)
        pasoapaso.putExtra("num", pasos_totales)
        pasoapaso.putExtra("imagenes", listaimagenes)
        startActivity(pasoapaso)

    }





    //Coloca el nombre de la receta entre otras cosas
    fun rellenarDatos(nombre: String?){
        var Titulo = findViewById<TextView>(R.id.Titulo)


        Titulo.text = nombre

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
                    obtenerImagenes(nombreR)
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
        return tempArray
    }


    fun obtenerImagenes(nombreR: String) {

        var count = 0
        val nombreT = traductordeÑ(nombreR)

        println("pasos totales : " + pasos_totales)
        while (count < pasos_totales){


            var tempNombre : String= nombreT + count

            println("nombre Imagen : "+ tempNombre)
            var bitmap : Bitmap? = obtenerBitmap(tempNombre,nombreR,count)
            listaimagenes.add(bitmap)
            SystemClock.sleep(250)

            count+=1
        }


    }

    fun obtenerBitmap(tempNombre: String,nombreR: String,count : Int):Bitmap?{

        var referencia = Firebase.storage.reference.child("fotos_recetas/$nombreR/$tempNombre"+".jpg")
        var localfile = File.createTempFile(tempNombre,".jpg")

        referencia.getFile(localfile).addOnSuccessListener {

            tempbitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            //binding.ImagenPortada.setImageBitmap(bitmap)

            println("count in  : "+ count)

            //count+=1

        }.addOnFailureListener{
            println("la wea malaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        }.addOnCanceledListener {
            println("la wea malaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa (Cancelado)")
        }
        return tempbitmap

    }

    fun obtenerImagenPortada(nombreR: String,pr : ProgressDialog){

        var tempNombre = traductordeÑ(nombreR)
        var referencia = Firebase.storage.reference.child("fotos_recetas/$nombreR/$tempNombre"+"P.jpg")
        val localfile2 = File.createTempFile("tempfileP",".jpg")
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