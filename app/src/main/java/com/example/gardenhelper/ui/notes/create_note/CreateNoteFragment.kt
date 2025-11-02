package com.example.gardenhelper.ui.notes.create_note

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
import com.example.gardenhelper.databinding.FragmentCreateNoteBinding
import com.example.gardenhelper.ui.calendar.calendar_day.CalendarDayFragment
import com.example.gardenhelper.ui.notes.ImagePagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class CreateNoteFragment : Fragment() {

    private val viewModel by viewModel<CreateNoteViewModel>()

    private var _binding: FragmentCreateNoteBinding? = null

    private val binding get() = _binding!!
    private lateinit var adapter: ImagePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNoteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (requireArguments().getString(CalendarDayFragment.ID) != null) {
            viewModel.setDate(requireArguments().getString(CalendarDayFragment.ID) ?: "")
        }
        if (requireArguments().getInt(OBJECT_ID) != 0) {
            viewModel.setObjectId(requireArguments().getInt(OBJECT_ID))
        }

        binding.addPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Инициализация адаптера с URI
        adapter = ImagePagerAdapter(mutableListOf())
        binding.viewPager.adapter = adapter

        binding.createNote.setOnClickListener {
            if (requireArguments().getInt(OBJECT_ID) != 0) {
                viewModel.saveNoteFromObject(
                    binding.etNameText.text.toString(),
                    binding.etDescriptionText.text.toString()
                )
            } else {
                viewModel.saveNoteFromCalendar(
                    binding.etNameText.text.toString(),
                    binding.etDescriptionText.text.toString()
                )
            }
        }

        viewModel.newImage.observe(viewLifecycleOwner) { path ->
            adapter.images.add(path)
            adapter.notifyDataSetChanged()
            binding.indicator.setViewPager(binding.viewPager)
        }

        viewModel.isFinished.observe(viewLifecycleOwner) { isFinished ->
            if (isFinished) findNavController().popBackStack()
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
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

    companion object {
        const val OBJECT_ID = "object_id"
    }
}