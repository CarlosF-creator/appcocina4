package com.example.appcocina4

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class login : AppCompatActivity(), View.OnClickListener {

    private var db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
    }
    private fun setup(){
        var btnRegister = findViewById<Button>(R.id.button_crearCuenta)
        var button_olvidar = findViewById<Button>(R.id.button_olvidar)

        var email = findViewById<EditText>(R.id.edt_email)
        var usuario = findViewById<EditText>(R.id.edt_usuario)
        var pass1 = findViewById<EditText>(R.id.edt_pass1)
        var pass2 = findViewById<EditText>(R.id.edt_pass2)
        var tempP = findViewById<Switch>(R.id.switch_principiante)

        var principiante = -1
        if (tempP.isChecked){
            principiante = 2
        }else {
            principiante = 1
        }

        btnRegister.setOnClickListener{
            var newEmail = modificarEmail(email.text.toString())
            if(email.text.isNotEmpty() && usuario.text.isNotEmpty() && pass1.text.isNotEmpty() && pass2.text.isNotEmpty()){
                if (pass1.text.toString() != pass2.text.toString()){
                    Toast.makeText(applicationContext,"Compruebe si su contraseña esta correcta", Toast.LENGTH_SHORT).show()
                } else{

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(newEmail,pass1.text.toString()).addOnCompleteListener{

                        if(it.isSuccessful){
                            db.collection("users").document(newEmail).set(
                                hashMapOf("user" to usuario.text.toString(),
                                    "principiante" to principiante,
                                    "Uid" to FirebaseAuth.getInstance().uid)
                            )
                            var mainhub = Intent(this, MainHub::class.java)
                            startActivity(mainhub)
                            finish()
                        }else{
                            Toast.makeText(applicationContext,"Se ha producido un Error de Autentificacion, Comprueba tus datos", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            else{
                Toast.makeText(applicationContext,"Se ha producido un Error , Por favor comprueba tus datos", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun olvidar(p0: View?){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Olvidaste la contraseña")
        val view = layoutInflater.inflate(R.layout.cambiarcontrasena,null)
        val username = view.findViewById<EditText>(R.id.et_username)
        builder.setView(view)
        builder.setPositiveButton("Cambiar", DialogInterface.OnClickListener { _, _ ->
            forgotPassword(username)
        })
        builder.setNegativeButton("Cerrar", DialogInterface.OnClickListener { _, _ ->  })
        builder.show()
    }

    private fun forgotPassword(username : EditText){
        if (username.text.toString().isEmpty()) {
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            return
        }
        auth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"Mensaje enviado",Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onClick(p0: View?) {
        setContentView(R.layout.register)
        setup()
    }

    fun modificarEmail(email:String):String{
        var tempEmail : String = ""
        var valido = false
        for (e in email){
            if(e.isUpperCase()){
                if (e == '@'){
                    valido = true
                }
                tempEmail = tempEmail + e.lowercase()
            }else{
                if (e == '@'){
                    valido = true
                }
                tempEmail = tempEmail + e
            }
        }
        if (valido){
            return tempEmail.lowercase()
        }else{
            return tempEmail.lowercase()+"@gmail.com"
        }

    }

    fun botonLogin(p0: View?){
        var email = findViewById<EditText>(R.id.edt_usuario_ing)
        var pass = findViewById<EditText>(R.id.edt_pass_ing)
        if(email.text.isNotEmpty() &&  pass.text.isNotEmpty()){
            var newEmail = modificarEmail(email.text.toString())
            FirebaseAuth.getInstance().signInWithEmailAndPassword(newEmail,pass.text.toString()).addOnCompleteListener{
                if(it.isSuccessful){
                    //showHome(it.result?.user?.email ?:"",ProviderType.BASIC)
                    var mainhub = Intent(this, MainHub::class.java)
                    mainhub.putExtra("Nombre_U", newEmail)
                    startActivity(mainhub)
                    finish()
                }else{
                    Toast.makeText(applicationContext,"Se ha producido un Error de Autentificacion, Comprueba tus datos", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
            Toast.makeText(applicationContext,"Se ha producido un Error , Por favor comprueba tus datos", Toast.LENGTH_SHORT).show()
        }

    }
}