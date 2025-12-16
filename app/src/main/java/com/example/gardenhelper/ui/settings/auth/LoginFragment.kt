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
import com.example.gardenhelper.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private val viewModel by viewModel<AuthViewModel>()
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideNavigationBar()

        viewModel.loginCompleted.observe(viewLifecycleOwner) { isCompleted ->
            if (isCompleted) {
                Toast.makeText(
                    requireContext(),
                    "Вход выаолнен успешно",
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

        binding.goToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginButton.setOnClickListener {
            login()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showNavigationBar()
    }

    fun login() {
        if (binding.emailFieldLogin.text.toString().isEmpty() || binding.passwordFieldLogin.text.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Логин и пароль не могут быть пустые", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.login(binding.emailFieldLogin.text.toString(),binding.passwordFieldLogin.text.toString())
        }
    }
}