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

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effects = Channel<LoginEffect>(Channel.BUFFERED)
    val effects: Flow<LoginEffect> = _effects.receiveAsFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.UserOrEmailChanged ->
                _uiState.update { validate(it.copy(userOrEmail = event.value)) }

            is LoginUiEvent.PasswordChanged ->
                _uiState.update { validate(it.copy(password = event.value)) }

            LoginUiEvent.Submit -> submit()
        }
    }

    private fun submit() {
        if (_uiState.value.isLoginEnabled) {
            _effects.trySend(LoginEffect.NavigateToHome)
        } else {
            _effects.trySend(LoginEffect.ShowError(R.string.toast_error_fill_fields))
        }
    }

    /** Recalcula errores y habilitación del botón a partir de los valores actuales. */
    private fun validate(state: LoginUiState): LoginUiState {
        val userError = validateUserOrEmail(state.userOrEmail)
        val passwordError = validatePassword(state.password)
        return state.copy(
            // Solo se muestra el error cuando el campo no está vacío
            userOrEmailError = if (state.userOrEmail.isEmpty()) null else userError,
            passwordError = if (state.password.isEmpty()) null else passwordError,
            isLoginEnabled = userError == null && passwordError == null
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

    /** Devuelve null si la contraseña tiene al menos 6 caracteres. */
    private fun validatePassword(value: String): Int? =
        if (value.length >= MIN_PASSWORD_LENGTH) null else R.string.error_password_short

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
        val USERNAME_REGEX = Regex("^[A-Za-z0-9._-]{3,20}$")
    }
}
