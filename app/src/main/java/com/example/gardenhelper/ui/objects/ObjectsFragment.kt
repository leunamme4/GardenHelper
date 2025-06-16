package com.example.gardenhelper.ui.objects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gardenhelper.R
import com.example.gardenhelper.databinding.FragmentObjectsBinding
import com.example.gardenhelper.di.viewModel
import com.example.gardenhelper.domain.models.garden.GardenObject
import com.example.gardenhelper.ui.calendar.CalendarFragment
import com.example.gardenhelper.ui.calendar.calendar_day.CalendarDayFragment.Companion.ID
import com.example.gardenhelper.ui.objects.create_object.CreateObjectViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ObjectsFragment : Fragment() {

    private var _binding: FragmentObjectsBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: ObjectsAdapter

    private val viewModel by viewModel<ObjectsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentObjectsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Тестовые данные
//        val testObjects = listOf(
//            GardenObject(
//                name = "Помидор",
//                type = "Растение",
//                description = "Красивое цветущее растение с приятным ароматом",
//                icon = R.drawable.ic_tomato // замените на реальную ссылку
//            ),
//            GardenObject(
//                name = "Дом",
//                type = "Строение",
//                description = "Деревянная беседка для отдыха в саду",
//                icon = R.drawable.ic_housewithgarden // замените на реальную ссылку
//            ),
//            GardenObject(
//                name = "Скамейка",
//                type = "Строение",
//                description = "Металлическая скамейка с деревянными сиденьями",
//                icon = R.drawable.ic_bench // замените на реальную ссылку
//            )
//        )

        viewModel.myObjects.observe(viewLifecycleOwner) { gardenObjects ->
            render(gardenObjects)
        }

        binding.rv.layoutManager = LinearLayoutManager(requireContext())

        binding.addObject.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_objects_to_createObject)
        }

        viewModel.getObjects()

        binding.searchEdit.doOnTextChanged { text, _, _, _ ->

                viewModel.searchObjectsByName(text.toString().trim())

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: ObjectsState) {
        when(state) {
            is ObjectsState.Content -> {
                adapter = ObjectsAdapter(state.objects) { gardenObject ->
                    val bundle = Bundle().apply {
                        putInt(OBJECT_ID, gardenObject.id)
                    }
                    findNavController().navigate(
                        R.id.action_navigation_objects_to_objectFragment,
                        bundle
                    )
                }
                binding.rv.adapter = adapter
            }
            ObjectsState.Empty -> {}
        }
    }

    companion object {
        const val OBJECT_ID = "object_id"
    }
}