package com.example.appcocina4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_calculadora.*

class Calculadora2 : AppCompatActivity() {

    var suggestion = ArrayList<String?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculadora2)

        var suggestion = ArrayList<String?>()
        var adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,suggestion)

        autoCompleteTextView.threshold=0
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setOnFocusChangeListener({ view, b->  if(b) autoCompleteTextView.showDropDown()})
    }
}