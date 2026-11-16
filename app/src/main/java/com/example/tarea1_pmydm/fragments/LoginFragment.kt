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
import com.example.tarea1_pmydm.viewmodels.LoginEffect
import com.example.tarea1_pmydm.viewmodels.LoginUiEvent
import com.example.tarea1_pmydm.viewmodels.LoginUiState
import com.example.tarea1_pmydm.viewmodels.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var tilUser: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var btnLogin: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tilUser = view.findViewById(R.id.tilUserLogin)
        tilPassword = view.findViewById(R.id.tilPasswordLogin)
        btnLogin = view.findViewById(R.id.btnLogin)
        val etUser = view.findViewById<TextInputEditText>(R.id.etUserLogin)
        val etPassword = view.findViewById<TextInputEditText>(R.id.etPasswordLogin)

        // Restaura lo escrito (si lo hay) antes de registrar los listeners
        val state = viewModel.uiState.value
        if (etUser.text?.toString() != state.userOrEmail) etUser.setText(state.userOrEmail)
        if (etPassword.text?.toString() != state.password) etPassword.setText(state.password)

        // Cada letra añadida o eliminada actualiza el estado en el ViewModel
        etUser.doOnTextChanged { text, _, _, _ ->
            viewModel.onEvent(LoginUiEvent.UserOrEmailChanged(text?.toString().orEmpty()))
        }
        etPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.onEvent(LoginUiEvent.PasswordChanged(text?.toString().orEmpty()))
        }
        // Toast con el error al salir de un campo inválido
        etUser.showErrorToastOnFocusLost { viewModel.uiState.value.userOrEmailError }
        etPassword.showErrorToastOnFocusLost { viewModel.uiState.value.passwordError }
        etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onEvent(LoginUiEvent.Submit)
                true
            } else {
                false
            }
        }

        btnLogin.setOnClickListener { viewModel.onEvent(LoginUiEvent.Submit) }
        view.findViewById<TextView>(R.id.tvForgotPassword).setOnClickListener {
            findNavController().navigate(R.id.action_login_to_recovery)
        }
        view.findViewById<TextView>(R.id.tvGoToSignup).setOnClickListener {
            findNavController().navigate(R.id.action_login_to_registro)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.uiState.collect { render(it) } }
                launch {
                    viewModel.effects.collect { effect ->
                        when (effect) {
                            LoginEffect.NavigateToHome ->
                                findNavController().navigate(R.id.action_login_to_home)

                            is LoginEffect.ShowError ->
                                Toast.makeText(requireContext(), effect.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun render(state: LoginUiState) {
        tilUser.setErrorText(state.userOrEmailError)
        tilPassword.setErrorText(state.passwordError)
        btnLogin.isEnabled = state.isLoginEnabled
    }

    private fun TextInputLayout.setErrorText(resId: Int?) {
        val message = resId?.let { getString(it) }
        if (error?.toString() != message) error = message
    }
}
