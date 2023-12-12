package com.example.voltageapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.voltageapp.FetchData

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main();
    }

    private fun main () {
        FetchData();
        println("aktif");
    }

}

