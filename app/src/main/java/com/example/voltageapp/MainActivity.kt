package com.example.voltageapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
//import com.android.volley.Request
//import com.android.volley.Response
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import com.android.volley.toolbox.JsonArrayRequest
import okhttp3.*
import okio.ByteString
import okhttp3.Request


class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var webSocket: WebSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        handler.postDelayed(pollingRunnable, 0)

        // Inisialisasi dan buka koneksi WebSocket
        initializeWebSocket("ws://192.168.0.111:8080")

    }

    private fun initializeWebSocket(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient.Builder()
            .build()

        webSocket = client.newWebSocket(request, MyWebSocketListener())
    }

    inner class MyWebSocketListener : WebSocketListener() {
        private var updateHandler: Handler? = null

        fun setUpdateHandler(handler: Handler) {
            this.updateHandler = handler
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            // WebSocket terbuka
            handler.post {
                println("WebSocket terbuka")
                println("websocket terbuka res $response")

            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            // Handle pesan teks dari server
            handler.post {
//                teks("Received message: $text")
                println("Message $text");
            }

            processData(text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            // Handle pesan byte (jika diperlukan)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            // WebSocket ditutup
            handler.post {
//                teks("WebSocket ditutup. Code: $code, Reason: $reason")
                println("Code $code, Reason $reason")
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            // Handle kesalahan
            handler.post {
//                teks("WebSocket error: ${t.message}")
                println("Fail $t.message")
                println("Fail Res $response")
            }
        }

        private fun processData(jsonString: String) {
            // Parse JSON ke bentuk data yang dapat diolah
            val jsonArray = JSONArray(jsonString)

            // Lakukan tindakan sesuai dengan data yang diterima dari server
            // Misalnya, tampilkan data di antarmuka pengguna atau lakukan operasi lain
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                // Ambil nilai atribut tertentu dari jsonObject
                val nilaiSensor = jsonObject.getString("nilai_sensor")
                // Lakukan tindakan sesuai dengan nilaiSensor
                // Misalnya, tampilkan nilaiSensor di logcat atau antarmuka pengguna
                println("Nilai Sensor: $nilaiSensor")
                teks(nilaiSensor);
                updateHandler?.post {
                    teks(nilaiSensor);
                }

            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        // Tutup koneksi WebSocket saat aplikasi dihancurkan
        webSocket.close(1000, "Normal closure")
    }

//    private fun teks(nilaiSensor: String) {
//        // Tambahkan logika untuk memperbarui tampilan atau melakukan tindakan lain
//        // berdasarkan nilaiSensor yang diterima dari WebSocket
//        println("Nilai Sensor: $nilaiSensor")
//    }
//    private val pollingRunnable = object : Runnable {
//        override fun run() {
//            pollSensorValue("https://7936-103-156-128-34.ngrok-free.app/monitoringtegangan/api.php") // Ganti dengan URL API-mu
//            handler.postDelayed(this, 1000) // Lakukan polling lagi setelah 5 detik
//        }
//    }

//    fun pollSensorValue(url: String) {
//        // Buat instance dari RequestQueue
//        val requestQueue = Volley.newRequestQueue(/* context */ this)
//
//        // Buat request menggunakan JsonArrayRequest untuk metode GET
//        val jsonArrayRequest = JsonArrayRequest(
//            Request.Method.GET, url, null,
//            Response.Listener<JSONArray> { response ->
//                // Handle respons JSONArray
//                for (i in 0 until response.length()) {
//                    val jsonObject = response.getJSONObject(i)
//                    val nilaiSensor = jsonObject.getString("nilai_sensor")
//                    Log.d("API Response", "Nilai Sensor: $nilaiSensor")
//                    teks(nilaiSensor);
//                }
//            },
//            Response.ErrorListener { error ->
//                // Handle kesalahan
//                Log.e("API Error", "Error: ${error.message}")
//            }
//        )
//
//        // Tambahkan request ke dalam queue untuk dieksekusi
//        requestQueue.add(jsonArrayRequest)
//
//        println("aktif");
//    }

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

