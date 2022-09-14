package com.example.appcocina4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_calculadora.*

class activity_calculadora : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculadora)

        var suggestion = arrayOf("Apple","Google","Samsung","Huawei","HTC")
        var adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,suggestion)

        autoCompleteTextView.threshold=0
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setOnFocusChangeListener({ view, b->  if(b) autoCompleteTextView.showDropDown()})



    }
}