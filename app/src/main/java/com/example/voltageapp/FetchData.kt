package com.example.voltageapp
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

fun FetchData () {
    val JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"
    // Konfigurasi koneksi ke database
//    val url =
//    val user =
//    val password = ""

    var conn: Connection? = null
    var stmt:java.sql.Statement? = null
    var rs: ResultSet? = null

    try {

        Class.forName(JDBC_DRIVER)
        // Membuat koneksi
        conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/monitoringtegangan", "root", "")

        // Membuat statement
        stmt = conn.createStatement()

        // Eksekusi query
        val query = "SELECT * FROM tegangan"
         rs = stmt.executeQuery(query)

        // Menampilkan hasil query
        while (rs.next()) {
            val nilaiSensor = rs.getInt("nilai_sensor")
            println("Nilai Sensor: $nilaiSensor")
        }

        // Menutup resource
//        rs.close()
//        stmt.close()
//        conn.close()

        println("jalan");
    } catch (e: Exception) {
        // Menangani exception
        e.printStackTrace()
        println(e);
    }
}