package com.example.voltageapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import com.android.volley.toolbox.JsonArrayRequest


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
        val stateVoltage = findViewById<TextView>(R.id.stateVoltage);
        val checkValue = message.toInt();



        if(checkValue < 190){

            stateVoltage.setBackgroundResource(R.drawable.round_drop);
            stateVoltage.text = "DROP";
        } else if (checkValue > 239){

            stateVoltage.setBackgroundResource(R.drawable.round_over);
            stateVoltage.text = "OVER";
        } else {

            stateVoltage.setBackgroundResource(R.drawable.round_normal);
            stateVoltage.text = "NORMAL";
        }
        valueVoltage.text = message

    }

}

