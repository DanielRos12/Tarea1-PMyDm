package com.example.tarea1_pmydm

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val correo = findViewById<EditText>(R.id.etCorreoLogin)
        val contrasena = findViewById<EditText>(R.id.etContrasenaLogin)
        val boton = findViewById<Button>(R.id.btnLogin)
        val recuperar = findViewById<TextView>(R.id.tvRecuperar)
        val resultado = findViewById<TextView>(R.id.tvResultadoLogin)

        boton.setOnClickListener {
            resultado.text = ""

            if (!android.util.Patterns.EMAIL_ADDRESS
                    .matcher(correo.text.toString().trim()).matches()) {
                correo.error = "Introduce un correo válido"
                correo.requestFocus()
            } else if (contrasena.text.toString().isEmpty()) {
                contrasena.error = "Introduce tu contraseña"
                contrasena.requestFocus()
            } else {
                resultado.text = "Datos introducidos correctamente"
                Toast.makeText(
                    this,
                    "Formulario validado",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        recuperar.setOnClickListener {
            Toast.makeText(
                this,
                "Próximamente: recuperar contraseña",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
