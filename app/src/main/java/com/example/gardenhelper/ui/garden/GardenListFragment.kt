package com.example.gardenhelper.ui.garden

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.marginStart
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gardenhelper.R
import com.example.gardenhelper.databinding.FragmentGardenListBinding
import com.example.gardenhelper.ui.objects.GardenListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class GardenListFragment : Fragment() {

    private lateinit var adapter: GardenListAdapter

    private var _binding: FragmentGardenListBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModel<GardenListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGardenListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rv.layoutManager = LinearLayoutManager(requireContext())

        binding.addGarden.setOnClickListener {
            showNameInputDialog(onConfirm = {
                viewModel.addGarden(it)
            })
        }

        viewModel.myGardens.observe(viewLifecycleOwner) { gardens ->
            render(gardens)
        }

        viewModel.getGardens()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: GardenListState) {
        if (state is GardenListState.Content) {
            adapter = GardenListAdapter(state.objects, onItemClick = { id ->
                val bundle = Bundle().apply {
                    putInt(GARDEN_ID, id)
                }
                findNavController().navigate(
                    R.id.action_navigation_garden_list_to_gardenScheme,
                    bundle
                )
            }, onLongClick = { id ->
                AlertDialog.Builder(context)
                    .setTitle("Удалить объект")
                    .setMessage("Вы уверены, что хотите удалить схему участка? Это действие нельзя отменить.")
                    .setPositiveButton("Удалить") { dialog, _ ->
                        viewModel.deleteGarden(id)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Отмена") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            })
            binding.rv.adapter = adapter
        }
    }

    private fun showNameInputDialog(
        onConfirm: (String) -> Unit,
        onCancel: () -> Unit = {}
    ) {
        val inputEditText = EditText(requireContext()).apply {
            hint = "Введите название"
        }

        AlertDialog.Builder(context)
            .setTitle("Новое название")
            .setView(inputEditText)
            .setPositiveButton("OK") { _, _ ->
                val name = inputEditText.text.toString().trim()
                if (name.isNotEmpty()) {
                    onConfirm(name)
                }
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                onCancel()
                dialog.dismiss()
            }
            .create()
            .show()
    }


    companion object {
        const val GARDEN_ID = "garden_id"
    }
}