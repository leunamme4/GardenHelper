package com.example.gardenhelper.ui.objects.`object`

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.gardenhelper.R
import com.example.gardenhelper.databinding.FragmentObjectBinding
import com.example.gardenhelper.domain.models.garden.GardenObject
import com.example.gardenhelper.domain.models.notes.Note
import com.example.gardenhelper.domain.models.notes.Notification
import com.example.gardenhelper.ui.MainActivity
import com.example.gardenhelper.ui.notes.NotesAdapter
import com.example.gardenhelper.ui.notifications.NotificationsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ObjectFragment : Fragment() {

    private var _binding: FragmentObjectBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModel<ObjectViewModel>()

    private lateinit var notesAdapter: NotesAdapter
    private lateinit var notificationsAdapter: NotificationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentObjectBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).hideNavigationBar()

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addNote.setOnClickListener {
            val bundle = Bundle().apply {
                putInt(OBJECT_ID, requireArguments().getInt(OBJECT_ID))
            }
            findNavController().navigate(
                R.id.action_objectFragment_to_createNote,
                bundle
            )
        }

        binding.addNotification.setOnClickListener {
            val bundle = Bundle().apply {
                putInt(OBJECT_ID, requireArguments().getInt(OBJECT_ID))
            }
            findNavController().navigate(
                R.id.action_objectFragment_to_createNotification,
                bundle
            )
        }

        binding.editObject.setOnClickListener {
            val bundle = Bundle().apply {
                putInt(OBJECT_ID, requireArguments().getInt(OBJECT_ID))
            }
            findNavController().navigate(
                R.id.action_objectFragment_to_editObjectFragment,
                bundle
            )
        }

        viewModel.gardenObject.observe(viewLifecycleOwner) { gardenObject ->
            render(gardenObject)
        }

        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            renderNotes(notes)
        }

        viewModel.notifications.observe(viewLifecycleOwner) { notifications ->
            renderNotifications(notifications)
        }

        viewModel.getObject(requireArguments().getInt(OBJECT_ID))
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showNavigationBar()
    }

    private fun render(gardenObject: GardenObject) {
        binding.objectName.text = gardenObject.name
        binding.objectDescription.text = gardenObject.description
        binding.type.text = gardenObject.type
        Glide.with(binding.icon)
            .load(gardenObject.icon)
            .transform(RoundedCorners(32))
            .into(binding.icon)
    }

    private fun renderNotes(notes: List<Note>) {
        binding.rvNotes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotes.adapter = NotesAdapter(notes,
            onDelete = { id ->
            viewModel.deleteNoteById(id)
        }, onEdit = { id ->
            val bundle = Bundle().apply {
                putInt(NOTE_ID, id)
            }
            findNavController().navigate(
                R.id.action_objectFragment_to_editNoteFragment,
                bundle
            )
        })
        binding.rvNotes.adapter?.notifyDataSetChanged()
    }

    private fun renderNotifications(notifications: List<Notification>) {
        binding.rvNotifications.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotifications.adapter = NotificationsAdapter(
            notifications,
            onDelete = { id ->
                viewModel.deleteNotificationById(id)
                viewModel.cancelAlarm(requireContext(), id)
            }, onEdit = { id ->
                val bundle = Bundle().apply {
                    putInt(NOTIFICATION_ID, id)
                }
                findNavController().navigate(
                    R.id.action_objectFragment_to_editNotificationFragment,
                    bundle
                )
            })
        binding.rvNotifications.adapter?.notifyDataSetChanged()
    }


    companion object {
        const val OBJECT_ID = "object_id"
        const val NOTIFICATION_ID = "notification_id"
        const val NOTE_ID = "note_id"
    }
}