package com.example.gardenhelper.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gardenhelper.R
import com.example.gardenhelper.databinding.FragmentCalendarBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendar: CalendarView

    private val viewModel by viewModel<CalendarViewModel>()

    private lateinit var actualDate: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendar = binding.calendar

        binding.btnCalendarDay.setOnClickListener {

            val bundle = Bundle().apply {
                putString(DATE, actualDate)
            }
            findNavController().navigate(
                R.id.action_navigation_calendar_to_calendarDayFragment,
                bundle
            )
        }

        binding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            onCalendarChanged(year, month, dayOfMonth)
        }

        viewModel.weather.observe(viewLifecycleOwner) {
            binding.btnCalendarDay.isVisible = it
        }
    }

    private fun onCalendarChanged(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance().apply {
            set(year, month, dayOfMonth)
        }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        actualDate = formattedDate

        viewModel.hasWeather(formattedDate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val DATE = "date"
    }
}