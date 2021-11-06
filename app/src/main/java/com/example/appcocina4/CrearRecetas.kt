package com.example.appcocina4

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.LayoutDirection
import android.view.View
import android.widget.*
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.size
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class CrearRecetas : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var listaPasos = ArrayList<EditText>()
    var temptablecontext1 = baseContext
    var temptablecontext2 = baseContext
    var temptablecontextpaso1 = baseContext
    var temptablecontexttxt2 = baseContext
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_recetas)
        var tempTextView = findViewById<TextView>(R.id.text_pasonum)
        var tempEditText = findViewById<EditText>(R.id.edt_pasoapaso)
        var temptablett1 = findViewById<TableRow>(R.id.table1)
        var temptablett2 = findViewById<TableRow>(R.id.table2)

        temptablecontext1 = temptablett1.context
        temptablecontext2 = temptablett2.context
        temptablecontextpaso1 = tempEditText.context
        temptablecontexttxt2 = tempTextView.context

        tempEditText.isVisible = false
        tempTextView.isVisible = false
        temptablett1.isVisible = false
        temptablett2.isVisible = false
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
            var tempPaso: EditText = EditText(temptablecontextpaso1)
            var temptxt: TextView = TextView(temptablecontexttxt2)
            tempPaso.id = index
            tempPaso.hint = "Explicacion del Paso " + index
            tempPaso.textSize = 20F
            tempPaso.width = 1000


            listaPasos.add(tempPaso)

            tempPaso.isVisible = true
            temptxt.isVisible = true

            temptxt.id = index
            temptxt.height = 100
            temptxt.width = 500
            temptxt.textSize = 20F
            temptxt.setText("Paso " + index + " :")

            tempTabla1.addView(temptxt)
            //tempTabla1.weightSum = 1F
            tempTabla2.addView(tempPaso)
            //tempTabla2.weightSum = 1F
            linearpasos.addView(tempTabla1)
            linearpasos.addView(tempTabla2)


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
            var index : Int = 1
            var instruc = Instrucciones()
            for ( l in listaPasos){
                var numpaso = "paso"+index
                instruc.set(numpaso,l.text.toString())
                index += 1
            }
            db.collection("recetas").document(newtitulo).collection("Info").document("Instrucciones").set(
                instruc).addOnSuccessListener {
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Se ha guardado correctamente", Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Ocurrio un Error, intentelo mas tarde", Toast.LENGTH_SHORT).show()

            }
        } else{
            Toast.makeText(applicationContext, "Faltan Datos por rellenar", Toast.LENGTH_SHORT).show()
        }
    }

    fun habilitarReceta(newtitulo : String){
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
        } else{
            Toast.makeText(applicationContext, "Faltan Datos por rellenar", Toast.LENGTH_SHORT).show()
        }
    }

    fun btnGuandarYSubir(p0: View?){
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
            var index : Int = 1
            var instruc = Instrucciones()
            for ( l in listaPasos){
                var numpaso = "paso"+index
                instruc.set(numpaso,l.text.toString())
                index += 1
            }
            db.collection("recetas").document(newtitulo).collection("Info").document("Instrucciones").set(
                instruc).addOnSuccessListener {
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                    habilitarReceta(newtitulo)
                    Toast.makeText(applicationContext, "Se a guardado correctamente", Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Ocurrio un Error, intentelo mas tarde", Toast.LENGTH_SHORT).show()

            }

        } else{
            Toast.makeText(applicationContext, "Faltan Datos por rellenar", Toast.LENGTH_SHORT).show()
        }
    }
}


