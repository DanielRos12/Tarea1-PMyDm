package com.example.tarea1_pmydm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<android.widget.Button>(R.id.btnCrearCuenta)
            .setOnClickListener {
                val intent = android.content.Intent(this, SignupActivity::class.java)
                startActivity(intent)
            }

    }
}
