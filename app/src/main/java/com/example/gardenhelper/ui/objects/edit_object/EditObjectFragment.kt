package com.example.gardenhelper.ui.objects.edit_object

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gardenhelper.R
import com.example.gardenhelper.databinding.FragmentEditObjectBinding
import com.example.gardenhelper.domain.models.garden.GardenObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditObjectFragment : Fragment() {

    private val viewModel by viewModel<EditObjectViewModel>()

    private var _binding: FragmentEditObjectBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditObjectBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerInit()

        viewModel.isFinished.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        binding.createObject.setOnClickListener {
            viewModel.saveObject(
                name = binding.etNameText.text.toString(),
                type = binding.mySpinner.selectedItem as String,
                description = binding.etDescriptionText.text.toString()
            )
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.myObject.observe(viewLifecycleOwner) { obj ->
            render(obj)
        }

        viewModel.getObject(requireArguments().getInt(OBJECT_ID))
    }


    private fun spinnerInit() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_options,
            R.layout.spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.mySpinner.adapter = adapter
        }

        // Set spinner item selected listener
        binding.mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle selection
                val selectedItem = parent?.getItemAtPosition(position).toString()
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected
            }
        }
    }

    private fun render(obj: GardenObject) {
        binding.etNameText.setText(obj.name)
        binding.etDescriptionText.setText(obj.description)

        val spinnerAdapter = binding.mySpinner.adapter
        for (i in 0 until spinnerAdapter.count) {
            val item = spinnerAdapter.getItem(i).toString() // Получаем элемент на позиции i
            if (item == obj.type) {
                binding.mySpinner.setSelection(i) // Устанавливаем выбранную позицию
                break
            }
        }
    }

    companion object {
        const val OBJECT_ID = "object_id"
    }
}