package com.example.gardenhelper.ui.calendar.calendar_day

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gardenhelper.R
import com.example.gardenhelper.databinding.FragmentCalendarBinding
import com.example.gardenhelper.databinding.FragmentCalendarDayBinding
import com.example.gardenhelper.ui.calendar.CalendarViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalendarDayFragment : Fragment() {

    private var _binding: FragmentCalendarDayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel by viewModel<CalendarDayViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarDayBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}