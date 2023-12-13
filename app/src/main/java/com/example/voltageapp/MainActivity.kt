package com.example.voltageapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import org.json.JSONArray
import com.android.volley.toolbox.JsonArrayRequest
import com.example.voltageapp.FetchData



class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler.postDelayed(pollingRunnable, 0)


    }

    private val pollingRunnable = object : Runnable {
        override fun run() {
            pollSensorValue("https://7936-103-156-128-34.ngrok-free.app/monitoringtegangan/api.php") // Ganti dengan URL API-mu
            handler.postDelayed(this, 1000) // Lakukan polling lagi setelah 5 detik
        }
    }

    fun pollSensorValue(url: String) {
        // Buat instance dari RequestQueue
        val requestQueue = Volley.newRequestQueue(/* context */ this)

        // Buat request menggunakan JsonArrayRequest untuk metode GET
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONArray> { response ->
                // Handle respons JSONArray
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val nilaiSensor = jsonObject.getString("nilai_sensor")
                    Log.d("API Response", "Nilai Sensor: $nilaiSensor")
                    teks(nilaiSensor);
                }
            },
            Response.ErrorListener { error ->
                // Handle kesalahan
                Log.e("API Error", "Error: ${error.message}")
            }
        )

        // Tambahkan request ke dalam queue untuk dieksekusi
        requestQueue.add(jsonArrayRequest)

        println("aktif");
    }

//    private fun main () {
//        val url = "https://7936-103-156-128-34.ngrok-free.app/monitoringtegangan/api.php"
//
//        while (true) {
//            try {
//                pollSensorValue(url)
//                Thread.sleep(5000) // Tunggu 5 detik sebelum melakukan polling lagi
//            } catch (e: Exception) {
//                println("Error: ${e.message}")
//            }
//        }
//    }

    fun teks (message: String){
        val valueVoltage = findViewById<TextView>(R.id.valueVoltage);
        val checkValue = message.toInt();
        if(checkValue < 190){
            valueVoltage.setTextColor(ContextCompat.getColor(this,R.color.yellow_soft));
        } else if (checkValue > 239){
            valueVoltage.setTextColor(ContextCompat.getColor(this,R.color.red_soft));
        } else {
            valueVoltage.setTextColor(ContextCompat.getColor(this,R.color.green_soft));
        }
        valueVoltage.text = message

    }

}

