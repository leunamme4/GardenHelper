package com.example.gardenhelper.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import com.example.gardenhelper.databinding.FragmentCalendarBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendar: CalendarView

    private val viewModel by viewModel<CalendarViewModel>()

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

        calendar.setOnDateChangeListener { view, year, month, day ->
            ///
        }

        binding.btnCalendarDay.setOnClickListener {
            ///
        }

        viewModel.weather.observe(viewLifecycleOwner) {
            when (it) {
                is WeatherState.Content -> {}
                WeatherState.Empty -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}