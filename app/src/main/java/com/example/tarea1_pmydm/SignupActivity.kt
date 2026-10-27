package com.example.tarea1_pmydm

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val nombre = findViewById<EditText>(R.id.etNombre)
        val correo = findViewById<EditText>(R.id.etCorreo)
        val contrasena = findViewById<EditText>(R.id.etContrasena)
        val repetir = findViewById<EditText>(R.id.etRepetirContrasena)
        val boton = findViewById<Button>(R.id.btnRegistrarse)
        val resultado = findViewById<TextView>(R.id.tvResultado)

        boton.setOnClickListener {
            resultado.text = ""

            if (nombre.text.toString().trim().isEmpty()) {
                nombre.error = "Introduce tu nombre"
                nombre.requestFocus()
            } else if (!android.util.Patterns.EMAIL_ADDRESS
                    .matcher(correo.text.toString().trim()).matches()) {
                correo.error = "Introduce un correo válido"
                correo.requestFocus()
            } else if (contrasena.text.toString().length < 6) {
                contrasena.error = "Usa al menos 6 caracteres"
                contrasena.requestFocus()
            } else if (contrasena.text.toString() !=
                repetir.text.toString()) {
                repetir.error = "Las contraseñas no coinciden"
                repetir.requestFocus()
            } else {
                resultado.text = "¡Datos correctos!"
                Toast.makeText(
                    this,
                    "Formulario validado correctamente",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
