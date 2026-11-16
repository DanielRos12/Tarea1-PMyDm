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

/**
 * El ViewModel sobrevive a los giros de pantalla, por lo que los datos escritos
 * en el formulario no se pierden al cambiar entre vertical y horizontal.
 */
class RegistroViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    private val _effects = Channel<RegistroEffect>(Channel.BUFFERED)
    val effects: Flow<RegistroEffect> = _effects.receiveAsFlow()

    fun onEvent(event: RegistroUiEvent) {
        when (event) {
            is RegistroUiEvent.NameChanged ->
                _uiState.update { validate(it.copy(name = event.value)) }

            is RegistroUiEvent.EmailChanged ->
                _uiState.update { validate(it.copy(email = event.value)) }

            is RegistroUiEvent.PasswordChanged ->
                _uiState.update { validate(it.copy(password = event.value)) }

            is RegistroUiEvent.RepeatPasswordChanged ->
                _uiState.update { validate(it.copy(repeatPassword = event.value)) }

            RegistroUiEvent.Submit -> submit()
        }
    }

    private fun submit() {
        if (_uiState.value.isRegisterEnabled) {
            _effects.trySend(RegistroEffect.AccountCreated)
        } else {
            _effects.trySend(RegistroEffect.ShowError(R.string.toast_error_fill_fields))
        }
    }

    /** Recalcula errores y habilitación del botón a partir de todos los campos. */
    private fun validate(state: RegistroUiState): RegistroUiState {
        val nameError = validateName(state.name)
        val emailError = validateEmail(state.email)
        val passwordError = validatePassword(state.password)
        val repeatError = validateRepeatPassword(state.password, state.repeatPassword)
        return state.copy(
            nameError = if (state.name.isEmpty()) null else nameError,
            emailError = if (state.email.isEmpty()) null else emailError,
            passwordError = if (state.password.isEmpty()) null else passwordError,
            repeatPasswordError = if (state.repeatPassword.isEmpty()) null else repeatError,
            isRegisterEnabled = nameError == null && emailError == null &&
                passwordError == null && repeatError == null
        )
    }

    private fun validateName(value: String): Int? =
        if (value.trim().length >= MIN_NAME_LENGTH) null else R.string.error_name_invalid

    private fun validateEmail(value: String): Int? =
        if (Patterns.EMAIL_ADDRESS.matcher(value.trim()).matches()) null
        else R.string.error_email_invalid

    private fun validatePassword(value: String): Int? =
        if (value.length >= MIN_PASSWORD_LENGTH) null else R.string.error_password_short

    private fun validateRepeatPassword(password: String, repeat: String): Int? =
        if (repeat.isNotEmpty() && repeat == password) null else R.string.error_passwords_mismatch

    private companion object {
        const val MIN_NAME_LENGTH = 2
        const val MIN_PASSWORD_LENGTH = 6
    }
}
