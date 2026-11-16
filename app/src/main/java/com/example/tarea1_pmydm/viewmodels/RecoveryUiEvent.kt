package com.example.tarea1_pmydm.viewmodels

import androidx.annotation.StringRes

sealed interface RecoveryUiEvent {
    data class UserOrEmailChanged(val value: String) : RecoveryUiEvent
    data object Submit : RecoveryUiEvent
}

sealed interface RecoveryEffect {
    data object RecoverySent : RecoveryEffect
    data class ShowError(@StringRes val message: Int) : RecoveryEffect
}
