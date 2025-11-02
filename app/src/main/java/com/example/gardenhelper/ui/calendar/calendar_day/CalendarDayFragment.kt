package com.example.gardenhelper.ui.calendar.calendar_day

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gardenhelper.R
import com.example.gardenhelper.databinding.FragmentCalendarDayBinding
import com.example.gardenhelper.domain.models.calendar.CalendarDay
import com.example.gardenhelper.domain.models.notes.Note
import com.example.gardenhelper.ui.MainActivity
import com.example.gardenhelper.ui.calendar.CalendarFragment
import com.example.gardenhelper.ui.notes.NotesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalendarDayFragment : Fragment() {

    private var _binding: FragmentCalendarDayBinding? = null

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

        (activity as MainActivity).hideNavigationBar()

        viewModel.day.observe(viewLifecycleOwner) { day ->
            render(day)
        }

        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            renderNotes(notes)
        }

        if (arguments != null) {
            viewModel.getDay(requireArguments().getString(CalendarFragment.DATE)!!)
        }

        binding.addNote.setOnClickListener {
            val bundle = Bundle().apply {
                putString(ID, requireArguments().getString(CalendarFragment.DATE)!!)
            }
            findNavController().navigate(
                R.id.action_calendarDayFragment_to_createNote,
                bundle
            )
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun render(day: CalendarDay) {
        binding.actionBar.text = viewModel.formatDateToRussian(day.date)

        Glide.with(binding.iconWeather)
            .load("https:${day.weather.current.condition.icon}")
            .centerCrop()
            .placeholder(R.drawable.weather_placeholder)
            .into(binding.iconWeather)

        val weather = day.weather.current
        val location = day.weather.location
        binding.statsWeather.text = viewModel.getFormattedWeatherText(weather, location)
    }

    private fun renderNotes(notes: List<Note>) {
        binding.rvNotes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotes.adapter = NotesAdapter(notes,
            onDelete = { id ->
                viewModel.deleteNoteById(id)
            },
            onEdit = { id ->
                val bundle = Bundle().apply {
                    putInt(NOTE_ID, id)
                }
                findNavController().navigate(
                    R.id.action_calendarDayFragment_to_editNoteFragment,
                    bundle
                )
            })
        binding.rvNotes.adapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showNavigationBar()
    }

    companion object {
        const val ID = "id"
        const val NOTE_ID = "note_id"
    }
}