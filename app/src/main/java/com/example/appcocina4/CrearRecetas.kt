package com.example.appcocina4

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.LayoutDirection
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.size
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import org.w3c.dom.Text
import java.io.File
import kotlin.random.Random

class CrearRecetas : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var db_Storage = Firebase.storage.reference
    var listaPasos = ArrayList<EditText>()
    var temptablecontext1 = baseContext
    var temptablecontext2 = baseContext
    var temptablecontext3 = baseContext
    var temptablecontext5 = baseContext
    var temptablecontextpaso1 = baseContext
    var temptablecontexttxt2 = baseContext
    var btnSubirReceta = baseContext
    var text_Imagen = baseContext
    var text_Espacio = baseContext
    var File = 1
    var count = 0
    var portada = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_recetas)
        var tempTextView = findViewById<TextView>(R.id.text_pasonum)
        var tempTextespacio = findViewById<TextView>(R.id.Espacio)
        var tempEditText = findViewById<EditText>(R.id.edt_pasoapaso)
        var tempBtnSubir = findViewById<Button>(R.id.btn_Imagen)
        var temptablett1 = findViewById<TableRow>(R.id.table1)
        var temptablett2 = findViewById<TableRow>(R.id.table2)
        var temptablett3 = findViewById<TableRow>(R.id.table3)
        var temptablett5 = findViewById<TableRow>(R.id.table5espacio)

        temptablecontext1 = temptablett1.context
        temptablecontext2 = temptablett2.context
        temptablecontext3 = temptablett3.context
        temptablecontext5 = temptablett5.context
        temptablecontextpaso1 = tempEditText.context
        temptablecontexttxt2 = tempTextView.context
        btnSubirReceta = tempBtnSubir.context

        text_Espacio = tempTextespacio.context

        tempEditText.isVisible = false
        tempTextView.isVisible = false
        temptablett1.isVisible = false
        temptablett2.isVisible = false

        tempBtnSubir.isVisible = false
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var edt_titulo = findViewById<EditText>(R.id.edt_Titulo)
        var btn_portada = findViewById<Button>(R.id.btnImagenPortada)
        var nombreReceta = edt_titulo.text.toString().lowercase()
        var nombreImagen = ""

        if (portada){
            nombreImagen = traductordeÑ(nombreReceta)+ "P"
        }else{
            nombreImagen = traductordeÑ(nombreReceta)+ count
        }
        if (requestCode == File) {
            if (resultCode == RESULT_OK) {
                val FileUri = data!!.data
                val Folder: StorageReference = db_Storage.child("fotos_recetas/$nombreReceta")
                val file_name: StorageReference = Folder.child(nombreImagen +".jpg")
                FileUri!!.lastPathSegment
                file_name.putFile(FileUri).addOnSuccessListener { taskSnapshot ->
                    Toast.makeText(applicationContext, "Se subió correctamente", Toast.LENGTH_SHORT).show()
                    Log.d("Mensaje", "Se subió correctamente")
                    if (portada){
                        findViewById<TextView>(R.id.txt_ImagenPortada).setText(nombreImagen+".jpg")
                        portada = false
                    }else{
                        count +=1
                    }
                    }
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


    fun btnConfirmar(p0: View?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Cargando...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        var edt_titulo = findViewById<EditText>(R.id.edt_Titulo)
        var edt_descripcion = findViewById<EditText>(R.id.edt_descripcion)
        var edt_Npasos = findViewById<EditText>(R.id.edt_NPasos)
        var edt_Tpreparacion = findViewById<EditText>(R.id.edt_Tpreparacion)
        var linearLayout = findViewById<LinearLayout>(R.id.LinearPasos)
        linearLayout.clearDisappearingChildren()


        var newtitulo = edt_titulo.text.toString().lowercase()
        if (edt_titulo.text.isNotEmpty() && edt_descripcion.text.isNotEmpty() && edt_Npasos.text.isNotEmpty() && edt_Tpreparacion.text.isNotEmpty()) {

            db.collection("recetas").document(newtitulo).set(
                hashMapOf(
                    "descripcion" to edt_descripcion.text.toString(),
                    "numeroPasos" to edt_Npasos.text.toString().toInt(),
                    "tPreparacion" to edt_Tpreparacion.text.toString().toInt(),
                    "publica" to false
                )
            ).addOnSuccessListener {
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                }
                Toast.makeText(
                    applicationContext,
                    "Receta Creada Correctamente",
                    Toast.LENGTH_SHORT
                ).show()
                instanciarPasos(edt_Npasos.text.toString().toInt())
            }.addOnFailureListener {
                Toast.makeText(
                    applicationContext,
                    "Ocurrio un Error, intentelo mas tarde",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun instanciarPasos(numPasos: Int) {
        var linearpasos = findViewById<TableLayout>(R.id.LinearPasos)
        var tempTextView = findViewById<TextView>(R.id.text_pasonum)
        var tempEditText = findViewById<EditText>(R.id.edt_pasoapaso)


        linearpasos.removeAllViews()
        listaPasos.clear()
        var index: Int = 1
        println("numero de pasos : " + numPasos)
        while (index < numPasos + 1) {
            var tempTabla1: TableRow = TableRow(temptablecontext1)
            var tempTabla2: TableRow = TableRow(temptablecontext2)
            var tempTabla3: TableRow = TableRow(temptablecontext3)
            var tempTabla5: TableRow = TableRow(temptablecontext5)
            var tempPaso: EditText = EditText(temptablecontextpaso1)
            var temptxt: TextView = TextView(temptablecontexttxt2)
            var tempbtn: Button = Button(btnSubirReceta)
            var tempEspacio : TextView = TextView(text_Espacio)


            tempPaso.id = index
            tempPaso.hint = "Explicacion del Paso " + index
            tempPaso.textSize = 20F
            tempPaso.width = 1000


            listaPasos.add(tempPaso)

            tempPaso.isVisible = true
            temptxt.isVisible = true
            tempbtn.isVisible = true


            temptxt.id = index
            temptxt.height = 100
            temptxt.width = 500
            temptxt.textSize = 20F
            temptxt.setText("Paso " + index + " :")
            tempbtn.text = "Subir Imagen"
            tempbtn.setOnClickListener { btn_SubirImagen(p0 = View(this)) }
            tempEspacio.height = 50
            tempEspacio.text = "___________________________________________________"


            tempTabla1.addView(temptxt)
            tempTabla2.addView(tempPaso)
            tempTabla3.addView(tempbtn)
            tempTabla5.addView(tempEspacio)


            linearpasos.addView(tempTabla1)
            linearpasos.addView(tempTabla2)
            linearpasos.addView(tempTabla3)
            linearpasos.addView(tempTabla5)



            index += 1
        }
    }

    fun btnBuscar(p0: View?) {
        var tempEdt = findViewById<EditText>(R.id.edt_Titulo)

        db.collection("recetas").document(tempEdt.text.toString().lowercase()).get()
            .addOnSuccessListener { tempData ->
                var tempDes = findViewById<EditText>(R.id.edt_descripcion)
                var tempNp = findViewById<EditText>(R.id.edt_NPasos)
                var tempTp = findViewById<EditText>(R.id.edt_Tpreparacion)
                var tempEstado = findViewById<TextView>(R.id.Estado)

                val tdes: String? = tempData.data?.get("descripcion").toString()
                val tNp: String? = tempData.data?.get("numeroPasos").toString()
                val tTp: String? = tempData.data?.get("tPreparacion").toString()
                val tEstado: String? = tempData.data?.get("publica").toString()


                if (tdes.equals("null") and tNp.equals("null") and tEstado.equals("null")) {
                    Toast.makeText(applicationContext, "Esta receta no existe", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (tdes != null) {
                        tempDes.setText(tdes.toString())
                    }
                    if (tNp != null) {
                        tempNp.setText(tNp.toString())
                    }
                    if (tTp != null) {
                        tempTp.setText(tTp.toString())
                    }
                    if (tEstado != null) {
                        if (tEstado.toString() == "true") {
                            tempEstado.text = "Visible : Si"
                        }
                        if (tEstado.toString() == "false") {
                            tempEstado.text = "Visible : No"
                        }
                    }
                    instanciarPasos(tempNp.text.toString().toInt())
                }

            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Esta receta no existe", Toast.LENGTH_SHORT)
                    .show()
            }
    }


    fun btnGuardar(p0: View?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Cargando...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        var edt_titulo = findViewById<EditText>(R.id.edt_Titulo)
        var edt_descripcion = findViewById<EditText>(R.id.edt_descripcion)
        var edt_Npasos = findViewById<EditText>(R.id.edt_NPasos)
        var edt_Tpreparacion = findViewById<EditText>(R.id.edt_Tpreparacion)

        var newtitulo = edt_titulo.text.toString().lowercase()
        if (edt_titulo.text.isNotEmpty() && edt_descripcion.text.isNotEmpty() && edt_Npasos.text.isNotEmpty() && edt_Tpreparacion.text.isNotEmpty()) {
            var index: Int = 1
            var instruc = Instrucciones()
            for (l in listaPasos) {
                var numpaso = "paso" + index
                instruc.set(numpaso, l.text.toString())
                index += 1
            }
            db.collection("recetas").document(newtitulo).collection("Info")
                .document("Instrucciones").set(
                instruc
            ).addOnSuccessListener {
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "Se ha guardado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }.addOnFailureListener {
                Toast.makeText(
                    applicationContext,
                    "Ocurrio un Error, intentelo mas tarde",
                    Toast.LENGTH_SHORT
                ).show()

            }
        } else {
            Toast.makeText(applicationContext, "Faltan Datos por rellenar", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun habilitarReceta(newtitulo: String) {
        var edt_titulo = findViewById<EditText>(R.id.edt_Titulo)
        var edt_descripcion = findViewById<EditText>(R.id.edt_descripcion)
        var edt_Npasos = findViewById<EditText>(R.id.edt_NPasos)
        var edt_Tpreparacion = findViewById<EditText>(R.id.edt_Tpreparacion)

        if (edt_titulo.text.isNotEmpty() && edt_descripcion.text.isNotEmpty() && edt_Npasos.text.isNotEmpty() && edt_Tpreparacion.text.isNotEmpty()) {
            db.collection("recetas").document(newtitulo).set(
                hashMapOf(
                    "descripcion" to edt_descripcion.text.toString(),
                    "numeroPasos" to edt_Npasos.text.toString().toInt(),
                    "tPreparacion" to edt_Tpreparacion.text.toString().toInt(),
                    "publica" to true
                )
            ).addOnSuccessListener {
                println("Se guardo todo correctamente")
            }
        } else {
            Toast.makeText(applicationContext, "Faltan Datos por rellenar", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun btnGuandarYSubir(p0: View?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Cargando...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        var edt_titulo = findViewById<EditText>(R.id.edt_Titulo)
        var edt_descripcion = findViewById<EditText>(R.id.edt_descripcion)
        var edt_Npasos = findViewById<EditText>(R.id.edt_NPasos)
        var edt_Tpreparacion = findViewById<EditText>(R.id.edt_Tpreparacion)

        var newtitulo = edt_titulo.text.toString().lowercase()
        if (edt_titulo.text.isNotEmpty() && edt_descripcion.text.isNotEmpty() && edt_Npasos.text.isNotEmpty() && edt_Tpreparacion.text.isNotEmpty()) {
            var index: Int = 1
            var instruc = Instrucciones()
            for (l in listaPasos) {
                var numpaso = "paso" + index
                instruc.set(numpaso, l.text.toString())
                index += 1
            }
            db.collection("recetas").document(newtitulo).collection("Info")
                .document("Instrucciones").set(
                instruc
            ).addOnSuccessListener {
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                    habilitarReceta(newtitulo)
                    Toast.makeText(
                        applicationContext,
                        "Se a guardado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }.addOnFailureListener {
                Toast.makeText(
                    applicationContext,
                    "Ocurrio un Error, intentelo mas tarde",
                    Toast.LENGTH_SHORT
                ).show()

            }

        } else {
            Toast.makeText(applicationContext, "Faltan Datos por rellenar", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun btn_SubirImagen(p0: View?) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, File)
    }
    fun btn_SubirImagenPortada(p0: View?) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        portada = true
        startActivityForResult(intent, File)
    }
}

