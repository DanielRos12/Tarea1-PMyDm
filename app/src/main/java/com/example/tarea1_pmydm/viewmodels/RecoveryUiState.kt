package com.example.tarea1_pmydm.viewmodels

import androidx.annotation.StringRes

/** Estado de la pantalla de Recuperación de contraseña. */
data class RecoveryUiState(
    val userOrEmail: String = "",
    @StringRes val userOrEmailError: Int? = null,
    val isRecoverEnabled: Boolean = false
)
