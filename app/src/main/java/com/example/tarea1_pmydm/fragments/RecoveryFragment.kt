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
import com.example.tarea1_pmydm.viewmodels.RecoveryEffect
import com.example.tarea1_pmydm.viewmodels.RecoveryUiEvent
import com.example.tarea1_pmydm.viewmodels.RecoveryUiState
import com.example.tarea1_pmydm.viewmodels.RecoveryViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class RecoveryFragment : Fragment(R.layout.fragment_recovery) {

    private val viewModel: RecoveryViewModel by viewModels()

    private lateinit var tilUser: TextInputLayout
    private lateinit var btnRecover: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tilUser = view.findViewById(R.id.tilUserRecovery)
        btnRecover = view.findViewById(R.id.btnRecover)
        val etUser = view.findViewById<TextInputEditText>(R.id.etUserRecovery)

        val saved = viewModel.uiState.value.userOrEmail
        if (etUser.text?.toString() != saved) etUser.setText(saved)

        etUser.doOnTextChanged { text, _, _, _ ->
            viewModel.onEvent(RecoveryUiEvent.UserOrEmailChanged(text?.toString().orEmpty()))
        }
        // Toast con el error al salir de un campo inválido
        etUser.showErrorToastOnFocusLost { viewModel.uiState.value.userOrEmailError }
        etUser.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onEvent(RecoveryUiEvent.Submit)
                true
            } else {
                false
            }
        }

        btnRecover.setOnClickListener { viewModel.onEvent(RecoveryUiEvent.Submit) }
        view.findViewById<TextView>(R.id.tvBackToLogin).setOnClickListener {
            findNavController().popBackStack()
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
                            RecoveryEffect.RecoverySent -> {
                                Toast.makeText(
                                    requireContext(),
                                    R.string.toast_recovery_sent,
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().popBackStack()
                            }

                            is RecoveryEffect.ShowError ->
                                Toast.makeText(requireContext(), effect.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun render(state: RecoveryUiState) {
        val message = state.userOrEmailError?.let { getString(it) }
        if (tilUser.error?.toString() != message) tilUser.error = message
        btnRecover.isEnabled = state.isRecoverEnabled
    }
}
