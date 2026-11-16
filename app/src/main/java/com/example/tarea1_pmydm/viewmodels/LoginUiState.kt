package com.example.tarea1_pmydm.viewmodels

import androidx.annotation.StringRes

/** Estado completo de la pantalla de Login (lo observa el fragment con StateFlow). */
data class LoginUiState(
    val userOrEmail: String = "",
    val password: String = "",
    @StringRes val userOrEmailError: Int? = null,
    @StringRes val passwordError: Int? = null,
    val isLoginEnabled: Boolean = false
)
