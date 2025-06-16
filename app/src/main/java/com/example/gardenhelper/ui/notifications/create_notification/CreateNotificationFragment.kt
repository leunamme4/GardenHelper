package com.example.gardenhelper.ui.notifications.create_notification

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gardenhelper.databinding.FragmentCreateNotificationBinding
import com.example.gardenhelper.ui.objects.ObjectsFragment.Companion.OBJECT_ID
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class CreateNotificationFragment : Fragment() {

    private val viewModel by viewModel<CreateNotificationViewModel>()

    private var _binding: FragmentCreateNotificationBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNotificationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.isSaved.observe(viewLifecycleOwner) { isSaved ->
            if (isSaved) {
                findNavController().popBackStack()
            }
        }

        binding.addNotification.setOnClickListener {
            viewModel.setParameters(
                context = requireContext(),
                title = binding.etNameText.text.toString(),
                message = binding.etDescriptionText.text.toString(),
                time = "${binding.etDate.text} ${binding.etTime.text}",
                isActive = binding.activeSwitch.isChecked
            )

            viewModel.saveNotification(requireArguments().getInt(OBJECT_ID))
        }

        binding.etDate.setOnClickListener {
            showDatePicker()
        }

        binding.etTime.setOnClickListener {
            showTimePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Месяц возвращается от 0 до 11, поэтому добавляем 1
                val formattedDate = "$selectedDay.${selectedMonth + 1}.$selectedYear"
                binding.etDate.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.etTime.setText(formattedTime)
            },
            hour, minute, true // true - 24-часовой формат
        )
        timePickerDialog.show()
    }
}