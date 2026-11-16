package com.example.tarea1_pmydm.viewmodels

import androidx.annotation.StringRes

/** Acciones del usuario que la vista envía al ViewModel. */
sealed interface LoginUiEvent {
    data class UserOrEmailChanged(val value: String) : LoginUiEvent
    data class PasswordChanged(val value: String) : LoginUiEvent
    data object Submit : LoginUiEvent
}

/** Efectos de un solo uso que el ViewModel pide a la vista (navegar, mostrar Toast). */
sealed interface LoginEffect {
    data object NavigateToHome : LoginEffect
    data class ShowError(@StringRes val message: Int) : LoginEffect
}
