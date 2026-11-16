package com.example.tarea1_pmydm

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment

/**
 * Única activity de la app. Solo contiene el FragmentContainerView (NavHostFragment)
 * que aloja todas las pantallas definidas en res/navigation/nav_graph.xml.
 */
class MainActivity : AppCompatActivity() {

    private val navController
        get() = (supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val container = findViewById<android.view.View>(R.id.fragmentContainerView)

        // Edge-to-edge (targetSdk 35+): dejamos hueco para barras del sistema y teclado.
        ViewCompat.setOnApplyWindowInsetsListener(container) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            )
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Solo la pantalla de Registro admite vertical y horizontal; el resto, solo vertical.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            requestedOrientation = if (destination.id == R.id.registroFragment) {
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            } else {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
    }

    /** Hace que el botón "arriba" (Up) navegue hacia atrás usando el grafo de navegación. */
    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp() || super.onSupportNavigateUp()
}
