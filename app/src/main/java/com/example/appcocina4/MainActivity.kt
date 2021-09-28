package com.example.appcocina4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

var login :Boolean = false



class MainActivity : AppCompatActivity() , View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (login){
            setContentView(R.layout.activity_main)
        }
        else {
            setContentView(R.layout.login)

            //var btnRegister = findViewById<Button>(R.id.button_register)
            //btnRegister!!.setOnClickListener(this)

        }
    }
    private fun setup(){
        var btnRegister = findViewById<Button>(R.id.button_crearCuenta)

        var email = findViewById<EditText>(R.id.edt_email)
        var usuario = findViewById<EditText>(R.id.edt_usuario)
        var pass1 = findViewById<EditText>(R.id.edt_pass1)
        var pass2 = findViewById<EditText>(R.id.edt_pass2)
        var principiante = findViewById<Switch>(R.id.switch_principiante)

        btnRegister.setOnClickListener{
            if(email.text.isNotEmpty() && usuario.text.isNotEmpty() && pass1.text.isNotEmpty() && pass2.text.isNotEmpty()){
                if (pass1.text.toString() != pass2.text.toString()){
                    Toast.makeText(getApplicationContext(),"Compruebe si su contraseña esta correcta", Toast.LENGTH_SHORT).show()
                } else{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(),pass1.text.toString()).addOnCompleteListener{

                    if(it.isSuccessful){
                        setContentView(R.layout.activity_main)
                        MainHub()
                    }else{
                        Toast.makeText(getApplicationContext(),"Se ha producido un Error de Autentificacion, Comprueba tus datos", Toast.LENGTH_SHORT).show()
                    }
                }}
            }
            else{
                Toast.makeText(getApplicationContext(),"Se ha producido un Error , Por favor comprueba tus datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Funciones Login
    override fun onClick(p0: View?) {
        setContentView(R.layout.register)
        setup()
    }

    fun botonLogin(p0: View?){
        var btnLogin = findViewById<Button>(R.id.button_Login)
        var email = findViewById<EditText>(R.id.edt_usuario_ing)
        var pass = findViewById<EditText>(R.id.edt_pass_ing)
        if(email.text.isNotEmpty() &&  pass.text.isNotEmpty()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email.text.toString(),pass.text.toString()).addOnCompleteListener{
                if(it.isSuccessful){
                    //showHome(it.result?.user?.email ?:"",ProviderType.BASIC)
                    setContentView(R.layout.activity_main)
                }else{
                    Toast.makeText(getApplicationContext(),"Se ha producido un Error de Autentificacion, Comprueba tus datos", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Se ha producido un Error , Por favor comprueba tus datos", Toast.LENGTH_SHORT).show()
        }

    }


}