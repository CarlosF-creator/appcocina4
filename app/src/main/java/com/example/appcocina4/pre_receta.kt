package com.example.appcocina4

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore

class pre_receta : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_receta)
    }

    fun btnCocinar(p0: View?){

        startActivity(Intent(this, Pasoapaso::class.java))




    }
}