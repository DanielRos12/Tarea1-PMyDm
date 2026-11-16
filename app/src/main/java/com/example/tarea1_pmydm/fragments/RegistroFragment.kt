package com.example.tarea1_pmydm.fragments

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.tarea1_pmydm.R
import com.example.tarea1_pmydm.viewmodels.RegistroEffect
import com.example.tarea1_pmydm.viewmodels.RegistroUiEvent
import com.example.tarea1_pmydm.viewmodels.RegistroUiState
import com.example.tarea1_pmydm.viewmodels.RegistroViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

/**
 * Única pantalla con orientación vertical y horizontal:
 * usa res/layout/fragment_registro.xml y res/layout-land/fragment_registro.xml
 * (mismos ids en ambos). El estado vive en RegistroViewModel, que sobrevive al giro.
 */
class RegistroFragment : Fragment(R.layout.fragment_registro) {

    private val viewModel: RegistroViewModel by viewModels()

    private lateinit var tilName: TextInputLayout
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var tilRepeatPassword: TextInputLayout
    private lateinit var btnRegister: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tilName = view.findViewById(R.id.tilName)
        tilEmail = view.findViewById(R.id.tilEmail)
        tilPassword = view.findViewById(R.id.tilPassword)
        tilRepeatPassword = view.findViewById(R.id.tilRepeatPassword)
        btnRegister = view.findViewById(R.id.btnRegister)

        val etName = view.findViewById<TextInputEditText>(R.id.etName)
        val etEmail = view.findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = view.findViewById<TextInputEditText>(R.id.etPassword)
        val etRepeat = view.findViewById<TextInputEditText>(R.id.etRepeatPassword)

        // Tras girar la pantalla se vuelve a pintar lo que había escrito (viene del ViewModel)
        val state = viewModel.uiState.value
        etName.restore(state.name)
        etEmail.restore(state.email)
        etPassword.restore(state.password)
        etRepeat.restore(state.repeatPassword)

        // Cada letra añadida o eliminada actualiza el estado en el ViewModel
        etName.doOnTextChanged { text, _, _, _ ->
            viewModel.onEvent(RegistroUiEvent.NameChanged(text?.toString().orEmpty()))
        }
        etEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.onEvent(RegistroUiEvent.EmailChanged(text?.toString().orEmpty()))
        }
        etPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.onEvent(RegistroUiEvent.PasswordChanged(text?.toString().orEmpty()))
        }
        etRepeat.doOnTextChanged { text, _, _, _ ->
            viewModel.onEvent(RegistroUiEvent.RepeatPasswordChanged(text?.toString().orEmpty()))
        }
        // Toast con el error al salir de un campo inválido
        etName.showErrorToastOnFocusLost { viewModel.uiState.value.nameError }
        etEmail.showErrorToastOnFocusLost { viewModel.uiState.value.emailError }
        etPassword.showErrorToastOnFocusLost { viewModel.uiState.value.passwordError }
        etRepeat.showErrorToastOnFocusLost { viewModel.uiState.value.repeatPasswordError }
        etRepeat.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onEvent(RegistroUiEvent.Submit)
                true
            } else {
                false
            }
        }

        btnRegister.setOnClickListener { viewModel.onEvent(RegistroUiEvent.Submit) }
        view.findViewById<TextView>(R.id.tvGoToLogin).setOnClickListener {
            findNavController().navigate(R.id.action_registro_to_login)
        }

        observeViewModel()
    }

    private fun TextInputEditText.restore(value: String) {
        if (text?.toString() != value) setText(value)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.uiState.collect { render(it) } }
                launch {
                    viewModel.effects.collect { effect ->
                        when (effect) {
                            RegistroEffect.AccountCreated -> {
                                Toast.makeText(
                                    requireContext(),
                                    R.string.toast_account_created,
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(R.id.action_registro_to_login)
                            }

                            is RegistroEffect.ShowError ->
                                Toast.makeText(requireContext(), effect.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun render(state: RegistroUiState) {
        tilName.setErrorText(state.nameError)
        tilEmail.setErrorText(state.emailError)
        tilPassword.setErrorText(state.passwordError)
        tilRepeatPassword.setErrorText(state.repeatPasswordError)
        btnRegister.isEnabled = state.isRegisterEnabled
    }

    private fun TextInputLayout.setErrorText(resId: Int?) {
        val message = resId?.let { getString(it) }
        if (error?.toString() != message) error = message
    }
}
