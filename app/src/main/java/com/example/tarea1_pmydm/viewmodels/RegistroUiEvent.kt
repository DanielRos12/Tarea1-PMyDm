package com.example.tarea1_pmydm.viewmodels

import androidx.annotation.StringRes

/** Acciones del usuario que la vista envía al ViewModel. */
sealed interface RegistroUiEvent {
    data class NameChanged(val value: String) : RegistroUiEvent
    data class EmailChanged(val value: String) : RegistroUiEvent
    data class PasswordChanged(val value: String) : RegistroUiEvent
    data class RepeatPasswordChanged(val value: String) : RegistroUiEvent
    data object Submit : RegistroUiEvent
}

/** Efectos de un solo uso que el ViewModel pide a la vista. */
sealed interface RegistroEffect {
    data object AccountCreated : RegistroEffect
    data class ShowError(@StringRes val message: Int) : RegistroEffect
}
