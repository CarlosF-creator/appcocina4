  package com.example.appcocina4

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Layout
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.core.widget.addTextChangedListener
import com.example.appcocina4.databinding.ActivityPasoapasoBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import kotlin.math.round


  class Pasoapaso : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var db_Storage = Firebase.storage.reference
    var numpasos = 0
    var cont = 0
      var userName = ""
    var listapasos  = ArrayList<String?>()
    var listaimagenes = ArrayList<Bitmap?>()
      var comentarios = ArrayList<Comentario>()
      var commentLayout: LinearLayout? = null
    var nombreR : String = ""
    var estadoTempo = 0
      var estadoPlay = 0
      var estadoCron = 0
      var estadoHer = 0
      var estadoCom = 0



    lateinit var binding : ActivityPasoapasoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasoapasoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listapasos = intent.getStringArrayListExtra("lista") as ArrayList<String?>
        numpasos = intent.getIntExtra("num",-500)
        //listaimagenes = intent.getStringArrayListExtra("imagenes") as ArrayList<Bitmap>
        var nombre = intent.getStringExtra("nombreR")

        commentLayout = findViewById(R.id.comentarios)

        var text_paso = findViewById<TextView>(R.id.text_paso)
        var text_numPaso = findViewById<TextView>(R.id.textNumPasos)
        var text_Temp_min = findViewById<TextView>(R.id.textNumTempMin)
        var text_Temp_mseg = findViewById<TextView>(R.id.textNumTempSeg)
        var text_Temp_dospuntos = findViewById<TextView>(R.id.textViewdospuntos)
        var btn_play = findViewById<Button>(R.id.btnPlay)
        var btnAlarma = findViewById<Button>(R.id.btnAlarma)
        var btnTempo = findViewById<Button>(R.id.btnTempo)
        var btnChat = findViewById<Button>(R.id.btnComentarios)
        var layComentar = findViewById<LinearLayout>(R.id.layoutcomentar)
        var scrollCom = findViewById<ScrollView>(R.id.scrollCom)

        text_numPaso.text = "Paso: ${cont+1}/$numpasos"

        text_Temp_min.isVisible = false
        text_Temp_mseg.isVisible = false
        text_Temp_dospuntos.isVisible = false
        btn_play.isVisible = false
        btnAlarma.isVisible = false
        btnTempo.isVisible = false
        btnChat.isVisible = false
        layComentar.isVisible = false
        scrollCom.isVisible = false

        if (nombre != null){
            var nombreweno :String = nombre.toString()
            nombreR = nombreweno
        }else{
            println("Nombre Null")
        }
        if (listapasos.isNotEmpty()){
            text_paso.text = listapasos[cont]
            //obtenerImagenPaso(nombreR, 0)
            obtenerImagenesPasos(nombreR, numpasos, 0)
        }
        obtenerNombreUsuario()


    }

    fun btnSiguiente(p0: View?){
        if (cont < numpasos-1){
            mas()
            var text_numPaso = findViewById<TextView>(R.id.textNumPasos)
            var text_paso = findViewById<TextView>(R.id.text_paso)
            println(cont)

            aplicarImagenes(cont)

            text_paso.text = listapasos[cont]
            text_numPaso.text = "Paso: ${cont+1}/$numpasos"
            if (estadoCom!=0){
                cleanComentarios()
                obtenerComentarios()
            }

        }else{
            var eval = Intent(this, eval::class.java)
            eval.putExtra("nombreR", nombreR)
            startActivity(eval)
        }
    }


    fun btnAtras(p0: View?){
        if(cont > 0){
            menos()
            var text_numPaso = findViewById<TextView>(R.id.textNumPasos)
            var text_paso = findViewById<TextView>(R.id.text_paso)
            println(cont)
            //obtenerImagenPaso(nombreR , cont)
            aplicarImagenes(cont)

            text_paso.text = listapasos[cont]
            text_numPaso.text = "Paso: ${cont+1}/$numpasos"
            if (estadoCom!=0){
                cleanComentarios()
                obtenerComentarios()
            }
        }else{
            finish()
        }
    }
      fun btnHerramientas(p0: View?){
          var btnTempo = findViewById<Button>(R.id.btnTempo)
          var btnChat = findViewById<Button>(R.id.btnComentarios)
          if(estadoHer == 0){
              btnTempo.isVisible = true
              btnChat.isVisible = true

              estadoHer+=1
          }else{
              btnTempo.isVisible = false
              btnChat.isVisible = false
              estadoHer=0
          }
      }
    @SuppressLint("ResourceAsColor")
    fun btnTempo(p0: View?){


        var text_Temp_min = findViewById<TextView>(R.id.textNumTempMin)
        var text_Temp_mseg = findViewById<TextView>(R.id.textNumTempSeg)
        var text_Temp_dospuntos = findViewById<TextView>(R.id.textViewdospuntos)
        var btn_Tempo = findViewById<Button>(R.id.btnTempo)
        var btn_play = findViewById<Button>(R.id.btnPlay)

        if(estadoTempo == 0){
            text_Temp_min.isVisible = true
            text_Temp_mseg.isVisible = true
            text_Temp_dospuntos.isVisible = true
            btn_play.isVisible = true
            btn_Tempo.setBackgroundColor(ContextCompat.getColor(baseContext,R.color.tempo_Pres))
            estadoTempo+=1
        }else{
            text_Temp_min.isVisible = false
            text_Temp_mseg.isVisible = false
            text_Temp_dospuntos.isVisible = false
            btn_play.isVisible = false
            btn_Tempo.setBackgroundColor(ContextCompat.getColor(baseContext,R.color.tempo_noPres))
            estadoTempo=0
        }
    }
      fun btnPlay(p0: View?){

          var btn_play = findViewById<Button>(R.id.btnPlay)
          var text_Temp_min = findViewById<TextView>(R.id.textNumTempMin)
          var text_Temp_mseg = findViewById<TextView>(R.id.textNumTempSeg)

          val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
          imm.hideSoftInputFromWindow(p0?.windowToken,0)

          if (text_Temp_min.text.toString() == ""){
              text_Temp_min.text = "00"
          }
          if (text_Temp_mseg.text.toString() == ""){
              text_Temp_mseg.text = "00"
          }
          if ((text_Temp_min.text.toString() == "00" && text_Temp_mseg.text.toString() == "00")){
              text_Temp_min.text = ""
              text_Temp_mseg.text = ""
              Toast.makeText(this, "Tiempo invalido", Toast.LENGTH_SHORT).show()
          }
          else{
              if(estadoPlay == 0){
              btn_play.foreground = getResources().getDrawable(android.R.drawable.ic_media_pause)
              estadoPlay+=1

              }else{
                  btn_play.foreground = getResources().getDrawable(android.R.drawable.ic_media_play)
                  estadoPlay=0
              }
                  var tiempomilisegundos = ((text_Temp_min.text.toString().toInt()*60)*1000)+(text_Temp_mseg.text.toString().toLong()*1000).toLong()
                  object : CountDownTimer(tiempomilisegundos,1000){
                      override fun onTick(millisUntilFinished: Long) {
                          if (estadoPlay == 1){
                              var tempSeg = (millisUntilFinished/1000).toInt()
                              var temptiempomin = tempSeg/60
                              var resto = tempSeg%60

                              text_Temp_min.text = temptiempomin.toString().padStart(length = 2,padChar = '0')
                              text_Temp_mseg.text = resto.toString().padStart(length = 2,padChar = '0')
                          }
                          else{
                              this.cancel()
                          }
                      }

                      override fun onFinish() {
                          Alarma()

                          text_Temp_min.text = ""
                          text_Temp_mseg.text = ""
                          btn_play.foreground = getResources().getDrawable(android.R.drawable.ic_media_play)
                          estadoPlay=0
                          this.cancel()

                      }
                  }.start()

          }

      }
      fun Alarma(){
          var btnAlarma = findViewById<Button>(R.id.btnAlarma)
          val notificacion = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
          var r = RingtoneManager.getRingtone(this@Pasoapaso,notificacion)
          r.play()
          btnAlarma.isVisible = true
          btnAlarma.setOnClickListener {
              r.stop()
              btnAlarma.isVisible = false
          }
          
      }
      fun btnComentarios(p0: View?){
          var scrollView3 = findViewById<ScrollView>(R.id.scrollView3)
          var btnComentario = findViewById<Button>(R.id.btnComentarios)
          var textTituloPaso = findViewById<TextView>(R.id.textTituloPaso)
          var layComentar = findViewById<LinearLayout>(R.id.layoutcomentar)
          var scrollCom = findViewById<ScrollView>(R.id.scrollCom)
          if(estadoCom == 0){
              btnComentario.foreground = getResources().getDrawable(R.drawable.nochat)

              commentLayout?.isVisible = true
              scrollView3.isVisible = false
              layComentar.isVisible = true
              scrollCom.isVisible = true
              textTituloPaso.text = "Comentarios"
              estadoCom+=1
              obtenerComentarios()

          }else{
              btnComentario.foreground = getResources().getDrawable(R.drawable.chat)

              estadoCom=0
              commentLayout?.isVisible = false
              layComentar.isVisible = false
              textTituloPaso.text = "Desarrollo"
              scrollView3.isVisible = true
              scrollCom.isVisible = false
              cleanComentarios()
          }
      }
      fun btnComentar(p0: View?) {
          var comantarioText = findViewById<EditText>(R.id.editTextComentar)
          val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
          imm.hideSoftInputFromWindow(p0?.windowToken,0)
          print(userName)
          if (comantarioText.text != null && comantarioText.text!!.isNotEmpty() && comantarioText.text.toString() != "") {
              var tempComentario = Comentario(comantarioText.text.toString(), userName,comentarios.size)
              db.collection("recetas").document(nombreR.toString()).collection("ComentariosP").document((cont+1).toString())
                  .collection("comentarios").document(comentarios.size.toString())
                  .set(tempComentario).addOnSuccessListener {
                      Toast.makeText(
                          applicationContext,
                          "Comentario Creado Correctamente",
                          Toast.LENGTH_SHORT
                      ).show()
                      comantarioText.setText("")
                      cleanComentarios()
                      obtenerComentarios()
                  }.addOnFailureListener {
                      Toast.makeText(
                          applicationContext,
                          "Ocurrio un Error, intentelo mas tarde",
                          Toast.LENGTH_SHORT
                      ).show()
                  }
          }
          else{
              Toast.makeText(this,"Comentario vacio... Intenta escribir algo", Toast.LENGTH_SHORT).show()
          }
      }

      fun obtenerImagenesPasos(nombreR: String, nPasos: Int, count: Int){
        var validador = false
        val pr = ProgressDialog(this)
        pr.setMessage("Cargando...")
        pr.setCancelable(false)
        pr.show()
          var contexBitmap : Bitmap? = null
          var estadoimg = 0
        for (i in 0..(nPasos-1)) {
            listaimagenes.add(i,contexBitmap)

            var tempNombre = traductordeÑ(nombreR).lowercase() + i.toString()
            var nombreImagen = ""
            println(tempNombre)
            try {
                var tempReferencia = db_Storage.child("fotos_recetas/$nombreR/$tempNombre" + ".jpg")
                nombreImagen = ".jpg"
            } catch (e: Exception) {
                nombreImagen = ".JPG"
            }
            var referencia = db_Storage.child("fotos_recetas/$nombreR/$tempNombre" + nombreImagen)
            val localfile2 = File.createTempFile(tempNombre, nombreImagen)
            referencia.getFile(localfile2).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile2.absolutePath)
                listaimagenes.set(i,bitmap)

                if (i >= (nPasos/2).toInt()){
                    if (estadoimg == 0 && listaimagenes.get(0) != null){
                        aplicarImagenes(0)
                        estadoimg+=1
                    }

                    if (pr.isShowing){
                        pr.dismiss()
                    }
                }

            }.addOnFailureListener {
                if (pr.isShowing){
                    pr.dismiss()
                }
                Toast.makeText(this, "Fallo en la carga de imagenes", Toast.LENGTH_SHORT).show()

            }
        }

    }
      // obtener los comentarios de la receta
      fun obtenerComentarios() {
          if (nombreR != null) {
              db.collection("recetas").document(nombreR.toString()).collection("ComentariosP").document((cont+1).toString()).collection("comentarios").get()
                  .addOnSuccessListener { documents ->
                      var tempCount = 0
                      println(" size = "+ documents.size()+" count="+cont)
                      for (document in documents) {
                          var text = document.data?.get("text").toString()
                          var userText = document.data?.get("user").toString()

                          comentarios.add(index = document.id.toInt(),Comentario(" "+userText+" : "+text, userText))
                          println("tempcount ="+tempCount+" size = "+ documents.size())
                          if (tempCount == documents.size()-1){
                              mostrarComentarios()
                          }
                          tempCount+=1
                      }

                  }.addOnFailureListener { _ ->
                      println("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                  }
          }else {
              print("error aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
          }
      }

      fun obtenerNombreUsuario() {
          db.collection("users").get().addOnSuccessListener{ document ->
              for (d in document){
                  if (d.data?.get("Uid") == FirebaseAuth.getInstance().uid){
                      userName = d.data?.get("user").toString()
                      break
                  }
              }
          }.addOnFailureListener{
              Toast.makeText(this,"Fallo en la Verificacion del Usuario", Toast.LENGTH_SHORT).show()
          }
      }

      fun mostrarComentarios() {
          for (com in 0..comentarios.size-1) {
              var view = layoutInflater.inflate(R.layout.comentario, null)
              val cmItem: TextView = view.findViewById(R.id.cm_item)
              cmItem.text = comentarios.get(com).text
              commentLayout?.addView(view)
          }
      }

    fun aplicarImagenes(count: Int){
        if (listaimagenes.size != 0){
            findViewById<ImageView>(R.id.Imagen_Paso).setImageBitmap(listaimagenes.get(count))
        }
        else{
            Toast.makeText(this, "Fallo al mostrar las imagenes", Toast.LENGTH_SHORT).show()
        }
    }


    fun mas(){
        cont += 1
    }
    fun menos(){
        cont -= 1
    }

      fun cleanComentarios() {
          comentarios.clear()
          commentLayout?.removeAllViewsInLayout()
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