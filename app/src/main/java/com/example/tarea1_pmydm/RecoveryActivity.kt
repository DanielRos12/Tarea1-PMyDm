package com.example.tarea1_pmydm

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RecoveryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recovery)

        val correo = findViewById<EditText>(R.id.etCorreoRecovery)
        val boton = findViewById<Button>(R.id.btnEnviarRecovery)
        val resultado = findViewById<TextView>(R.id.tvResultadoRecovery)

        boton.setOnClickListener {
            resultado.text = ""

            if (!android.util.Patterns.EMAIL_ADDRESS
                    .matcher(correo.text.toString().trim()).matches()) {

                correo.error = "Introduce un correo válido"
                correo.requestFocus()

            } else {

                resultado.text = "Solicitud realizada correctamente"

                Toast.makeText(
                    this,
                    "Instrucciones solicitadas",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
