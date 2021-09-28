package com.example.appcocina4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


enum class ProviderType{
    BASIC
}
open class MainHub : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    fun btnIngredientes () {
        setContentView(R.layout.ingredientes)

    }

    fun btnRecetas () {
        setContentView(R.layout.recetas)

    }




}