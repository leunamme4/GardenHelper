package com.example.gardenhelper.ui.garden.garden_scheme

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gardenhelper.R
import com.example.gardenhelper.databinding.FragmentGardenSchemeBinding
import com.example.gardenhelper.domain.models.garden.GardenObject
import com.example.gardenhelper.domain.models.garden.ObjectSizes
import com.example.gardenhelper.ui.MainActivity
import com.example.gardenhelper.ui.objects.ObjectsAdapter
import com.example.gardenhelper.ui.objects.ObjectsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.max
import kotlin.math.min

class GardenSchemeFragment : Fragment() {

    private val viewModel by viewModel<GardenSchemeViewModel>()

    private var _binding: FragmentGardenSchemeBinding? = null

    private val binding get() = _binding!!

    private var selectedView: ImageView? = null
    private var selectedObjectId = 0
    private val iconModels = mutableListOf<ObjectSizes>()

    private val sizeStep = 20  // количество пикселей при увеличении/уменьшении

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGardenSchemeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Кнопки управления размерами
        binding.increaseWidth.setOnClickListener { changeSize(widthDelta = sizeStep) }
        binding.decreaseWidth.setOnClickListener { changeSize(widthDelta = -sizeStep) }
        binding.increaseHeight.setOnClickListener { changeSize(heightDelta = sizeStep) }
        binding.decreaseHeight.setOnClickListener { changeSize(heightDelta = -sizeStep) }

        binding.increaseFieldWidth.setOnClickListener { resizeField(widthDelta = 50) }
        binding.decreaseFieldWidth.setOnClickListener { resizeField(widthDelta = -50) }
        binding.increaseFieldHeight.setOnClickListener { resizeField(heightDelta = 50) }
        binding.decreaseFieldHeight.setOnClickListener { resizeField(heightDelta = -50) }

        viewModel.savedObjects.observe(viewLifecycleOwner) { garden ->
            binding.field.removeAllViews()
            iconModels.clear()
            val params = binding.field.layoutParams
            params.width = garden.width
            params.height = garden.height

            binding.field.layoutParams = params

            if (garden.objects.isNotEmpty()) {
                restoreIcons(garden.objects)
            }
        }

        viewModel.myObjects.observe(viewLifecycleOwner) { gardenObjects ->
            showSelectionDialog(items = gardenObjects)
        }

        viewModel.isFinished.observe(viewLifecycleOwner) { isFinished ->
            if (isFinished) {
                findNavController().popBackStack()
            }
        }

        binding.addIconButton.setOnClickListener {
            viewModel.getObjects()
        }

        binding.saveButton.setOnClickListener {
            viewModel.saveGarden(iconModels, binding.field.width, binding.field.height, true)
        }

        binding.deleteObject.setOnClickListener {
            binding.field.removeView(selectedView)
            selectedView = null
            for (obj in iconModels) {
                if (obj.id == selectedObjectId) {
                    iconModels.remove(obj)
                    break
                }
            }
        }

        binding.selectedIcon.setOnClickListener {
            viewModel.saveGarden(iconModels, binding.field.width, binding.field.height, false)
            val bundle = Bundle().apply {
                putInt(ObjectsFragment.OBJECT_ID, selectedObjectId)
            }
            findNavController().navigate(R.id.action_gardenScheme_to_objectFragment, bundle)
        }

        viewModel.getGardenObjects(arguments?.getInt(GARDEN_ID)!!)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).hideNavigationBar()
    }

    private fun createIconFromModel(model: ObjectSizes) {
        val icon = ImageView(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(model.width, model.height)
            x = model.x
            y = model.y
            //setImageResource(model.drawableRes)
            scaleType = ImageView.ScaleType.CENTER_INSIDE
            id = model.id
            setBackgroundColor(ContextCompat.getColor(context, R.color.light_green_50))
        }

        Glide.with(icon)
            .load(model.icon)
            .placeholder(R.drawable.icon_plant)
            .into(icon)

        if (!iconModels.contains(model)) {
            iconModels.add(model)
        }
        setupTouchHandling(icon, model)
        icon.setOnClickListener {
            selectIcon(icon)
            selectedObjectId = model.id
        }
        binding.field.addView(icon)
    }


    private fun selectIcon(icon: ImageView) {
        // Убрать выделение у предыдущего
        selectedView?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.light_green_50
            )
        )

        // Добавить выделение
        val border = GradientDrawable().apply {
            setStroke(6, Color.GREEN)
            setColor(ContextCompat.getColor(requireContext(), R.color.light_green_50))
        }
        icon.background = border

        selectedView = icon
        binding.selectedIcon.setImageDrawable(icon.drawable)
        binding.selectedIcon.scaleType = ImageView.ScaleType.CENTER_CROP
        binding.controlPanel.visibility = View.VISIBLE
    }

    private fun changeSize(widthDelta: Int = 0, heightDelta: Int = 0) {
        selectedView?.let { view ->
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.width = max(50, params.width + widthDelta)
            params.height = max(50, params.height + heightDelta)
            view.layoutParams = params
        }

        for (obj in iconModels) {
            if (obj.id == selectedObjectId) {
                obj.width = selectedView!!.width
                obj.height = selectedView!!.height
                return
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchHandling(view: ImageView, model: ObjectSizes) {
        var dX = 0f
        var dY = 0f
        var startX = 0f
        var startY = 0f

        val borderPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            view.resources.displayMetrics
        )

        val scaleGestureDetector = ScaleGestureDetector(
            requireContext(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    val scaleFactor = detector.scaleFactor
                    view.scaleX *= scaleFactor
                    view.scaleY *= scaleFactor
                    return true
                }
            })

        view.setOnTouchListener { v, event ->
            scaleGestureDetector.onTouchEvent(event)

            val parent = v.parent as? ViewGroup ?: return@setOnTouchListener false

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    dX = v.x - event.rawX
                    dY = v.y - event.rawY
                    startX = event.rawX
                    startY = event.rawY
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX + dX
                    val newY = event.rawY + dY

                    val viewWidth = v.width * v.scaleX
                    val viewHeight = v.height * v.scaleY

                    val minX = borderPx
                    val minY = borderPx
                    val maxX = parent.width - viewWidth - borderPx
                    val maxY = parent.height - viewHeight - borderPx

                    val clampedX = newX.coerceIn(minX, maxX)
                    val clampedY = newY.coerceIn(minY, maxY)

                    v.x = clampedX
                    v.y = clampedY

                    model.x = clampedX
                    model.y = clampedY
                }

                MotionEvent.ACTION_UP -> {
                    val dx = event.rawX - startX
                    val dy = event.rawY - startY
                    val moved = dx * dx + dy * dy > 20f
                    if (!moved) v.performClick()
                }
            }
            true
        }
    }


    private fun resizeField(widthDelta: Int = 0, heightDelta: Int = 0) {
        val params = binding.field.layoutParams
        params.width = min(max(100, params.width + widthDelta), binding.fieldContainer.width)
        val maxFieldHeight =
            if (binding.controlPanel.isVisible) binding.fieldContainer.height else binding.fieldContainer.height - binding.controlPanel.height
        params.height = min(max(100, params.height + heightDelta), maxFieldHeight)

        binding.field.layoutParams = params
        binding.field.requestLayout()
    }

//    private fun checkCollision(newModel: IconModel, ignoreId: Int? = null): Boolean {
//        val newRect = Rect(
//            newModel.x.toInt(),
//            newModel.y.toInt(),
//            newModel.x.toInt() + newModel.width,
//            newModel.y.toInt() + newModel.height
//        )
//        return iconModels.any {
//            if (it.id == ignoreId) return@any false
//            val rect = Rect(it.x.toInt(), it.y.toInt(), it.x.toInt() + it.width, it.y.toInt() + it.height)
//            Rect.intersects(rect, newRect)
//        }
//    }

    private fun restoreIcons(savedObjects: List<ObjectSizes>) {
        iconModels.addAll(savedObjects)
        savedObjects.forEach { createIconFromModel(it) }
    }

    private fun showSelectionDialog(
        title: String = "Выберите элемент",
        items: List<GardenObject>,
    ) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_garden_object_selection, null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerView)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
            .create()

        dialogView.findViewById<TextView>(R.id.titleTextView).text = title

        dialog.show()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ObjectsAdapter(items, onItemClick = { gardenObject ->
            val newObject = ObjectSizes(id = gardenObject.id, icon = gardenObject.icon)
            for (obj in iconModels) {
                if (newObject.id == obj.id) {
                    dialog.dismiss()
                    return@ObjectsAdapter
                }
            }
            viewModel.addObject(newObject)
            createIconFromModel(newObject)
            dialog.dismiss()
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showNavigationBar()
    }

    companion object {
        const val GARDEN_ID = "garden_id"
        const val OBJECT_ID = "object_id"
    }
}