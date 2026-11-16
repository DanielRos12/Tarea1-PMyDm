package com.example.tarea1_pmydm.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tarea1_pmydm.R

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnCrearCuenta).setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_registro)
        }
        view.findViewById<Button>(R.id.btnIniciarSesion).setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_login)
        }
    }
}
