package com.firstapplication.recipebook.ui.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.databinding.CookTimePickerViewBinding
import com.firstapplication.recipebook.databinding.TimeElementBinding

class CookTimePickerView(
    context: Context,
    attr: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : ConstraintLayout(context, attr, defStyleAttr, defStyleRes), OnCookTimePickerItemClickListener {

    var selectedHour: String = "0"
        private set

    var selectedMinute: String = "0"
        private set

    private val binding: CookTimePickerViewBinding

    private lateinit var hoursAdapter: CookTimePickerAdapter
    private lateinit var minutesAdapter: CookTimePickerAdapter

    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attr,
        defStyleAttr,
        0
    )

    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context) : this(context, null)

    init {
        LayoutInflater.from(context).inflate(R.layout.cook_time_picker_view, this, true)
        binding = CookTimePickerViewBinding.bind(this)
        initAttributes(attr, defStyleAttr, defStyleRes)
        initListeners()
    }

    private fun initListeners() {
        binding.btnConfirm.setOnClickListener {
            val etText = binding.etTime.text
            if (etText.isNullOrEmpty()) {
                clearEditTextFocus()
                return@setOnClickListener
            } else if (etText.isDigitsOnly() && etText.length < 9) {
                val time = etText.toString().toInt()
                if (time < 60) {
                    selectedMinute = time.toString()
                    binding.etTime.setText(resources.getString(R.string.minutes_format, etText))
                } else {
                    val hours = time / 60
                    val minutes = time % 60

                    selectedHour = hours.toString()
                    selectedMinute = minutes.toString()
                    binding.etTime.setText(resources.getString(R.string.time_format, hours.toString(), minutes.toString()))
                }
            } else if (etText.length >= 9) {
                binding.etTime.error = resources.getString(R.string.too_long)
            } else {
                Toast.makeText(context, resources.getString(R.string.incorrect_text), Toast.LENGTH_SHORT).show()
            }

            binding.btnConfirm.isVisible = false
            clearEditTextFocus()
        }

        binding.etTime.setOnFocusChangeListener { _, isFocus ->
            if (isFocus) binding.etTime.setText("")
            binding.btnConfirm.isVisible = isFocus
        }
    }

    private fun clearEditTextFocus() {
        binding.etTime.clearFocus()
        val manager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(binding.etTime.windowToken, 0)
    }

    private fun initAttributes(attr: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attr == null) return
        val typedArray = context.obtainStyledAttributes(
            attr,
            R.styleable.CookTimePickerView,
            defStyleAttr,
            defStyleRes
        )

        val hoursReference = typedArray.getResourceId(R.styleable.CookTimePickerView_hoursData, 0)
        if (hoursReference != 0) setHoursData(hoursReference) else initHours()

        val minutesReference =
            typedArray.getResourceId(R.styleable.CookTimePickerView_minutesData, 0)
        if (minutesReference != 0) setMinutesData(minutesReference) else initMinutes()

        typedArray.recycle()
    }

    private fun initMinutes() {
        val minutes = mutableListOf<String>()
        for (i in 5..60 step 5) minutes.add(i.toString())
        minutesAdapter = CookTimePickerAdapter(this, minutes, TimePickerAdapterType.Minutes)
        binding.rwMinutes.adapter = minutesAdapter
    }

    private fun initHours() {
        val hours = mutableListOf<String>()
        for (i in 1..24) hours.add(i.toString())
        hoursAdapter = CookTimePickerAdapter(this, hours, TimePickerAdapterType.Hours)
        binding.rwHours.adapter = hoursAdapter
    }

    @SuppressLint("ResourceType")
    private fun setHoursData(@IdRes hoursReference: Int) {
        hoursAdapter = CookTimePickerAdapter(
            this,
            resources.getStringArray(hoursReference).toList(),
            TimePickerAdapterType.Hours
        )
        binding.rwHours.adapter = hoursAdapter
    }

    @SuppressLint("ResourceType")
    private fun setMinutesData(@IdRes minutesReference: Int) {
        minutesAdapter = CookTimePickerAdapter(
            this,
            resources.getStringArray(minutesReference).toList(),
            TimePickerAdapterType.Minutes
        )
        binding.rwMinutes.adapter = minutesAdapter
    }

    override fun onMinutesClick(item: String, id: Int) {
        selectedMinute = item
        clearEditTextFocus()

        val oldText = binding.etTime.text?.trim()
        when {
            oldText.isNullOrEmpty() -> {
                binding.etTime.setText(resources.getString(R.string.minutes_format, item))
            }
            oldText.contains(resources.getString(R.string.hours)) -> {
                val hours = oldText.split(" ")[0]
                binding.etTime.setText(resources.getString(R.string.time_format, hours, item))
            }
            else -> {
                binding.etTime.setText(resources.getString(R.string.minutes_format, item))
            }
        }
    }

    override fun onHoursClick(item: String, id: Int) {
        selectedHour = item
        clearEditTextFocus()

        val oldText = binding.etTime.text
        val splitText = oldText?.split(" ")
        if (oldText.isNullOrEmpty()) {
            binding.etTime.setText(resources.getString(R.string.hours_format, item))
        } else if (
            oldText.contains(resources.getString(R.string.hours)) &&
            oldText.contains(resources.getString(R.string.minutes))
        ) {
            val minutes = splitText!![2]
            binding.etTime.setText(resources.getString(R.string.time_format, item, minutes))
        } else if (
            oldText.contains(resources.getString(R.string.minutes)) &&
            !oldText.contains(resources.getString(R.string.hours))
        ) {
            val minutes = splitText!![0]
            binding.etTime.setText(resources.getString(R.string.time_format, item, minutes))
        } else {
            binding.etTime.setText(resources.getString(R.string.hours_format, item))
        }
    }

}

interface OnCookTimePickerItemClickListener {
    fun onMinutesClick(item: String, id: Int)
    fun onHoursClick(item: String, id: Int)
}

class CookTimePickerAdapter(
    private val listener: OnCookTimePickerItemClickListener,
    private val elements: List<String>,
    private val elementsType: TimePickerAdapterType
) : RecyclerView.Adapter<CookTimePickerAdapter.CookTimePickerViewHolder>() {

    class CookTimePickerViewHolder(private val binding: TimeElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(element: String) {
            binding.element.text = element
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookTimePickerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CookTimePickerViewHolder(TimeElementBinding.inflate(inflater))
    }

    override fun onBindViewHolder(
        holder: CookTimePickerViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.bind(elements[position])
        holder.itemView.setOnClickListener {
            if (elementsType is TimePickerAdapterType.Minutes) {
                listener.onMinutesClick(elements[position], position)
            } else {
                listener.onHoursClick(elements[position], position)
            }

            val animation = AnimationUtils.loadAnimation(it.context, R.anim.scale)
            it.startAnimation(animation)
        }
    }

    override fun getItemCount(): Int {
        return elements.size
    }
}

sealed class TimePickerAdapterType {
    object Minutes : TimePickerAdapterType()
    object Hours : TimePickerAdapterType()
}