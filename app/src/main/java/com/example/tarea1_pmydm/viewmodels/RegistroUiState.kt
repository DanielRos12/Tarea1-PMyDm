package com.example.tarea1_pmydm.viewmodels

import androidx.annotation.StringRes

/** Estado completo de la pantalla de Registro. */
data class RegistroUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    @StringRes val nameError: Int? = null,
    @StringRes val emailError: Int? = null,
    @StringRes val passwordError: Int? = null,
    @StringRes val repeatPasswordError: Int? = null,
    val isRegisterEnabled: Boolean = false
)
