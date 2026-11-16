package com.example.tarea1_pmydm.fragments

import android.widget.EditText
import android.widget.Toast

/**
 * Muestra un Toast con el mensaje de error del campo cuando el usuario sale de él
 * y el campo sigue siendo inválido. Respeta el listener de foco que ya tuviera el campo.
 */
fun EditText.showErrorToastOnFocusLost(errorProvider: () -> Int?) {
    val previous = onFocusChangeListener
    setOnFocusChangeListener { v, hasFocus ->
        previous?.onFocusChange(v, hasFocus)
        if (!hasFocus) {
            errorProvider()?.let { Toast.makeText(v.context, it, Toast.LENGTH_SHORT).show() }
        }
    }
}
