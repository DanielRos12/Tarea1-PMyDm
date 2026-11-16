package com.example.tarea1_pmydm.viewmodels

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.tarea1_pmydm.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class RecoveryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RecoveryUiState())
    val uiState: StateFlow<RecoveryUiState> = _uiState.asStateFlow()

    private val _effects = Channel<RecoveryEffect>(Channel.BUFFERED)
    val effects: Flow<RecoveryEffect> = _effects.receiveAsFlow()

    fun onEvent(event: RecoveryUiEvent) {
        when (event) {
            is RecoveryUiEvent.UserOrEmailChanged ->
                _uiState.update { validate(it.copy(userOrEmail = event.value)) }

            RecoveryUiEvent.Submit -> submit()
        }
    }

    private fun submit() {
        if (_uiState.value.isRecoverEnabled) {
            _effects.trySend(RecoveryEffect.RecoverySent)
        } else {
            _effects.trySend(RecoveryEffect.ShowError(R.string.toast_error_fill_fields))
        }
    }

    private fun validate(state: RecoveryUiState): RecoveryUiState {
        val error = validateUserOrEmail(state.userOrEmail)
        return state.copy(
            userOrEmailError = if (state.userOrEmail.isEmpty()) null else error,
            isRecoverEnabled = error == null
        )
    }

    /** Devuelve null si es un correo válido o un nombre de usuario válido. */
    private fun validateUserOrEmail(value: String): Int? {
        val text = value.trim()
        return when {
            text.contains("@") ->
                if (Patterns.EMAIL_ADDRESS.matcher(text).matches()) null
                else R.string.error_email_invalid

            USERNAME_REGEX.matches(text) -> null
            else -> R.string.error_user_invalid
        }
    }

    private companion object {
        val USERNAME_REGEX = Regex("^[A-Za-z0-9._-]{3,20}$")
    }
}
