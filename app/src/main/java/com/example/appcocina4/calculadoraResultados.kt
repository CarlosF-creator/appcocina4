package com.example.appcocina4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class calculadoraResultados : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculadora_resultados)

        var calorias = intent.getStringExtra("calofinalDef")
        var carboh = intent.getStringExtra("carbofinalDef")
        var grasa = intent.getStringExtra("grasafinalDef")
        var prote = intent.getStringExtra("protefinalDef")

        findViewById<TextView>(R.id.textView33).text = "Calorias: " +calorias
        findViewById<TextView>(R.id.textView34).text = "Carbohidratos: " +carboh
        findViewById<TextView>(R.id.textView35).text = "Grasas: " +grasa
        findViewById<TextView>(R.id.textView36).text = "Proteinas: " +prote
    }



}