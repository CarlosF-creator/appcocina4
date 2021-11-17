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
import com.google.firebase.firestore.FirebaseFirestore
import java.security.Principal


class MainActivity : AppCompatActivity()  {
    private val authUser:FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goTo()
    }
    fun goTo(){
        if (authUser.currentUser != null){
            startActivity(Intent(this,MainHub::class.java))
            finish()
        }else{
            startActivity(Intent(this, login::class.java))
            finish()
        }
    }
}