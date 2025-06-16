package com.example.gardenhelper.ui.settings

import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gardenhelper.R
import com.example.gardenhelper.databinding.FragmentSettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel by viewModel<SettingsViewModel>()

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginLayout.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
        }

        binding.loadDataToCloud.setOnClickListener {
            showCloudUploadConfirmationDialog(onConfirm = { viewModel.uploadAllToFirestore() })
        }

        binding.loadDataFromCloud.setOnClickListener {
            showCloudDownloadConfirmationDialog(onConfirm = { viewModel.downloadAllFromFirestore() })
        }

        binding.geolocation.setOnClickListener {
            showCoordinatesInputDialog()
        }

        viewModel.uploadIsFinished.observe(viewLifecycleOwner) { isFinished ->
            if (isFinished) {
                Toast.makeText(
                    requireContext(),
                    "Данные успешно загружены в облачное хранилище",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.downloadIsFinished.observe(viewLifecycleOwner) { isFinished ->
            if (isFinished) {
                Toast.makeText(
                    requireContext(),
                    "Данные успешно загружены из облачного хранилища",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showCloudUploadConfirmationDialog(
        onConfirm: () -> Unit,
        onCancel: (() -> Unit)? = { }
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Подтверждение загрузки")
            .setMessage("Вы действительно хотите загрузить данные в облако?")
            .setPositiveButton("Да") { dialog, _ ->
                onConfirm()
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                onCancel?.invoke()
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun showCloudDownloadConfirmationDialog(
        onConfirm: () -> Unit,
        onCancel: (() -> Unit)? = { }
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Подтверждение загрузки")
            .setMessage("Вы действительно хотите загрузить данные из облака?")
            .setPositiveButton("Да") { dialog, _ ->
                onConfirm()
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                onCancel?.invoke()
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun showCoordinatesInputDialog() {
        val prefs = requireContext().getSharedPreferences("weather_prefs", MODE_PRIVATE)

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val latInput = EditText(context).apply {
            hint = "Широта (напр. 55.7558)"
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
            setText(prefs.getString("latitude", ""))
        }

        val lonInput = EditText(context).apply {
            hint = "Долгота (напр. 37.6173)"
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
            setText(prefs.getString("longitude", ""))
        }

        layout.addView(latInput)
        layout.addView(lonInput)

        AlertDialog.Builder(context)
            .setTitle("Введите координаты")
            .setView(layout)
            .setPositiveButton("Сохранить") { _, _ ->
                val latitude = latInput.text.toString().trim()
                val longitude = lonInput.text.toString().trim()
                prefs.edit()
                    .putString("latitude", latitude)
                    .putString("longitude", longitude)
                    .apply()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}