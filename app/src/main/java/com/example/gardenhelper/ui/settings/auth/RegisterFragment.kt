package com.example.gardenhelper.ui.settings.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gardenhelper.R
import com.example.gardenhelper.databinding.FragmentLoginBinding
import com.example.gardenhelper.databinding.FragmentRegisterBinding
import com.example.gardenhelper.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterFragment : Fragment() {
    private val viewModel by viewModel<AuthViewModel>()
    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registrationCompleted.observe(viewLifecycleOwner) { isCompleted ->
            if (isCompleted) {
                Toast.makeText(
                    requireContext(),
                    "Регистрация завершена",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Произошла ошибка, попробуйте снова",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.registerButton.setOnClickListener {
            register()
        }
    }

    fun register() {
        if (binding.emailFieldRegister.text.toString()
                .isEmpty() || binding.passwordFieldRegister.text.toString().isEmpty()
        ) {
            Toast.makeText(requireContext(), "Поля не могут быть пустые", Toast.LENGTH_SHORT).show()
        } else {
            if (binding.passwordFieldRegister.text.toString() != binding.confirmPasswordField.text.toString()) {
                Toast.makeText(requireContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            }

            viewModel.register(
                binding.emailFieldRegister.text.toString(),
                binding.passwordFieldRegister.text.toString(),
                binding.confirmPasswordField.text.toString()
            )
        }
    }
}