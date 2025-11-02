package com.example.gardenhelper.ui.notes.edit_note

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gardenhelper.databinding.FragmentEditNoteBinding
import com.example.gardenhelper.domain.models.notes.Note
import com.example.gardenhelper.ui.notes.ImagePagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class EditNoteFragment : Fragment() {

    private val viewModel by viewModel<EditNoteViewModel>()

    private var _binding: FragmentEditNoteBinding? = null

    private val binding get() = _binding!!
    private lateinit var adapter: ImagePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Инициализация адаптера с URI
        adapter = ImagePagerAdapter(mutableListOf())
        binding.viewPager.adapter = adapter

        binding.createNote.setOnClickListener {
            viewModel.saveNote(
                binding.etNameText.text.toString(),
                binding.etDescriptionText.text.toString()
            )
        }

        viewModel.newImage.observe(viewLifecycleOwner) { path ->
            adapter.images.add(path)
            adapter.notifyDataSetChanged()
            binding.indicator.setViewPager(binding.viewPager)
        }

        viewModel.isFinished.observe(viewLifecycleOwner) { isFinished ->
            if (isFinished) findNavController().popBackStack()
        }

        viewModel.note.observe(viewLifecycleOwner) { note ->
            render(note)
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getNote(requireArguments().getInt(NOTE_ID))
    }

    //регистрируем событие, которое вызывает photo picker
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            //обрабатываем событие выбора пользователем фотографии
            if (uri != null) {
                saveImageToPrivateStorage(uri)
            }
        }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "note_images"
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "${uri.lastPathSegment}.jpg")
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        viewModel.addImage(file.absolutePath)
    }

    private fun render(note: Note) {
        adapter.images.addAll(note.images)
        adapter.notifyDataSetChanged()
        binding.indicator.setViewPager(binding.viewPager)
        binding.etNameText.setText(note.name)
        binding.etDescriptionText.setText(note.text)
    }

    companion object {
        const val NOTE_ID = "note_id"
    }
}