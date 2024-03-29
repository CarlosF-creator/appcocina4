package com.example.appcocina4

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
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
import kotlinx.android.synthetic.main.activity_calculadora.*
import org.w3c.dom.Text
import java.io.File
import kotlin.random.Random

class CrearRecetas : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var db_Storage = Firebase.storage.reference
    var listaPasos = ArrayList<EditText>()
    var listap = ArrayList<String?>()
    var listaRecetas = ArrayList<String?>()
    var temptablecontext1 = baseContext
    var temptablecontext2 = baseContext
    var temptablecontext3 = baseContext
    var temptablecontext5 = baseContext
    var temptablecontextpaso1 = baseContext
    var temptablecontexttxt2 = baseContext
    var btnSubirReceta = baseContext
    var text_Imagen = baseContext
    var text_Espacio = baseContext
    var textdetalle = baseContext
    var textnombre = baseContext
    var Lineartemp = baseContext

    var File = 1
    var count = 0
    var portada = false
    var j = 1

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
        var tempTextnombre = findViewById<EditText>(R.id.txtCrearIngNombre)
        var tempTextdetalle = findViewById<EditText>(R.id.txtCrearIngDetalle)
        var lineartemp : LinearLayout = findViewById<LinearLayout>(R.id.tempLinearIng)

        temptablecontext1 = temptablett1.context
        temptablecontext2 = temptablett2.context
        temptablecontext3 = temptablett3.context
        temptablecontext5 = temptablett5.context
        temptablecontextpaso1 = tempEditText.context
        temptablecontexttxt2 = tempTextView.context
        btnSubirReceta = tempBtnSubir.context
        textdetalle = tempTextdetalle.context
        textnombre = tempTextnombre.context
        Lineartemp = lineartemp.context

        text_Espacio = tempTextespacio.context

        tempEditText.isVisible = false
        tempTextView.isVisible = false
        temptablett1.isVisible = false
        temptablett2.isVisible = false
        lineartemp.isVisible = false


        tempBtnSubir.isVisible = false
        obtenerRecetasDisponibles()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var edt_titulo = findViewById<AutoCompleteTextView>(R.id.edt_Titulo)
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

        var edt_titulo = findViewById<AutoCompleteTextView>(R.id.edt_Titulo)
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

                Toast.makeText(applicationContext, "Receta Creada Correctamente", Toast.LENGTH_SHORT).show()
                obtenerListaPasos(newtitulo, edt_Npasos.text.toString().toInt())
                obtenerIngredientes(newtitulo)

            }.addOnFailureListener {

                Toast.makeText(applicationContext, "Ocurrio un Error, intentelo mas tarde", Toast.LENGTH_SHORT).show()
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
            tempPaso.width = 925

            if (listap.isNotEmpty() && index <= listap.size){
                tempPaso.setText(listap[index-1])
            }

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
            tempEspacio.text = "_____________________________________________________"

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
        var tempEdt = findViewById<AutoCompleteTextView>(R.id.edt_Titulo)
        var tempnombre = tempEdt.text.toString().lowercase()
        db.collection("recetas").document(tempnombre).get()
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
                    obtenerListaPasos(tempnombre,tempNp.text.toString().toInt())
                    obtenerIngredientes(tempnombre)

                }

            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Esta receta no existe", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    //Obtenemos todas las recetas disponibles
    fun obtenerRecetasDisponibles() {
        db.collection("recetas")
            .get().addOnSuccessListener { inst ->
                var tempCount = 0
                for (n in inst){
                    listaRecetas.add(n.id.toString())
                    if (tempCount == inst.size()-1){
                        autocompletado()
                    }
                    tempCount+=1
                }
            }.addOnFailureListener { _ ->
                println("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
            }
    }
    fun autocompletado(){
        var adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,listaRecetas)
        var edit_titulo = findViewById<AutoCompleteTextView>(R.id.edt_Titulo)
        edit_titulo.threshold=1
        edit_titulo.setAdapter(adapter)
        edit_titulo.setOnFocusChangeListener { view, b -> if (b) edit_titulo.showDropDown() }
    }

    //Obtenemos los pasos desde la base de Datos
    fun obtenerListaPasos(nombreR : String, npasos : Int) {
        listap.clear()
        if (nombreR != "no se encontro"){
            db.collection("recetas").document(nombreR).collection("Info").document("Instrucciones")
                .get().addOnSuccessListener { inst ->

                    var tempnum = 1
                    var Pasos = inst.toObject(Instrucciones::class.java)
                    if (Pasos != null){
                        listap = Pasos?.let { obtenerInstrucciones(it) }!!
                        instanciarPasos(npasos)
                    }

                }.addOnFailureListener { _ ->
                    println("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                }}else{
            print("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")

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
            db.collection("recetas").document(newtitulo).collection("Info").document("Instrucciones").set(instruc).addOnSuccessListener {

                if (progressDialog.isShowing) {
                    progressDialog.dismiss()

                    deshabilitarReceta(newtitulo)
                    SubirIngredientes(newtitulo)

                    Toast.makeText(applicationContext, "Se ha guardado correctamente", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {

                Toast.makeText(applicationContext, "Ocurrio un Error, intentelo mas tarde", Toast.LENGTH_SHORT).show()
            }
        } else {

            Toast.makeText(applicationContext, "Faltan Datos por rellenar", Toast.LENGTH_SHORT).show()
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
    fun deshabilitarReceta(newtitulo: String) {
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
                    "publica" to false
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
            db.collection("recetas").document(newtitulo).collection("Info").document("Instrucciones").set(instruc).addOnSuccessListener {
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                    habilitarReceta(newtitulo)
                    SubirIngredientes(newtitulo)
                    Toast.makeText(applicationContext, "Se a guardado correctamente", Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Ocurrio un Error, intentelo mas tarde", Toast.LENGTH_SHORT).show()

            }

        } else {
            Toast.makeText(applicationContext, "Faltan Datos por rellenar", Toast.LENGTH_SHORT).show()
        }
    }

    fun SubirIngredientes(nombreR: String){
        var linearGrande : LinearLayout = findViewById<LinearLayout>(R.id.linearLayout_Ing)
        var completado = false
        var i = 0
        if (linearGrande.size != 0){
            db.collection("recetas").document(nombreR).collection("Ingredientes").document().delete().addOnSuccessListener { doc ->
                print("se borro a")
            }.addOnFailureListener {
                Toast.makeText(this,"Error al actualizar Ingredientes, Intenta mas tarde", Toast.LENGTH_SHORT).show()
            }
        }
        while (i < linearGrande.size) {
            var templinear : LinearLayout = linearGrande[i] as LinearLayout
            var tempnombre : EditText = templinear[0] as EditText
            var tempdetalle : EditText = templinear[1] as EditText
            var ttnombre= tempnombre.text
            var ttdetalle= tempdetalle.text
            println("i:"+i+" nombre:"+tempnombre.text+" detalle:"+tempdetalle.text)

            db.collection("recetas").document(nombreR).collection("Ingredientes").document(i.toString()).set(
                hashMapOf(
                    "nombre" to ttnombre.toString().lowercase(),
                    "detalle" to ttdetalle.toString().lowercase()
                )
            ).addOnSuccessListener {
                completado = true
            }.addOnFailureListener {
                Toast.makeText(this,"Error Ingrediente, Intenta mas tarde", Toast.LENGTH_SHORT).show()
            }

            i+=1
        }
    }

    fun obtenerIngredientes(nombreR : String){
        if (nombreR != "no se encontro") {
            var linearGrande : LinearLayout = findViewById<LinearLayout>(R.id.linearLayout_Ing)
            linearGrande.removeAllViews()
            db.collection("recetas").document(nombreR).collection("Ingredientes").get().addOnSuccessListener { doc ->
                var i = 1
                while (i <= doc.size()){

                    var templinear : LinearLayout = LinearLayout(Lineartemp)
                    var tempnombre : EditText = EditText(textnombre)
                    var tempdetalle : EditText = EditText(textdetalle)

                    tempnombre.id = 1!!
                    tempnombre.textSize = 20F
                    tempnombre.width = 450
                    tempnombre.setText(doc.documents[i-1].get("nombre").toString())


                    tempdetalle.id = 2!!
                    tempdetalle.textSize = 20F
                    tempdetalle.width = 450
                    tempdetalle.setText(doc.documents[i-1].get("detalle").toString())


                    templinear.id = i
                    templinear.addView(tempnombre)
                    templinear.addView(tempdetalle)
                    linearGrande.addView(templinear)
                    j = linearGrande.size
                    i +=1
                }
            }.addOnFailureListener{
                Toast.makeText(this,"Error al cargar Ingredientes, Intenta mas tarde", Toast.LENGTH_SHORT).show()

            }
        }else{
            print("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa en numero")
        }
    }




    //Botones

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

    fun btn_masIng(p0: View?){
        var linearGrande : LinearLayout = findViewById<LinearLayout>(R.id.linearLayout_Ing)
        var templinear : LinearLayout = LinearLayout(Lineartemp)
        var tempnombre : EditText = EditText(textnombre)
        var tempdetalle : EditText = EditText(textdetalle)

        tempnombre.id = 1!!
        tempnombre.textSize = 20F
        tempnombre.width = 450
        tempnombre.hint = "Ingrediente "+j


        tempdetalle.id = 2!!
        tempdetalle.textSize = 20F
        tempdetalle.width = 450
        tempdetalle.hint = "Detalle "+j


        templinear.id = j
        templinear.addView(tempnombre)
        templinear.addView(tempdetalle)
        linearGrande.addView(templinear)


        j+=1
    }
    fun btn_menosIng(p0: View?){
        if (j > 1){
            var linearGrande : LinearLayout = findViewById<LinearLayout>(R.id.linearLayout_Ing)
            linearGrande.removeViewAt(linearGrande.size-1)
            j-=1
        }
    }
}

