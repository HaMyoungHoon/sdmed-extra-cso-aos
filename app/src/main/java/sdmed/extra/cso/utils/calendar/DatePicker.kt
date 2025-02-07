package sdmed.extra.cso.utils.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.DatePickerBinding
import sdmed.extra.cso.interfaces.IOnDismissListener
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.interfaces.command.IEventListener
import sdmed.extra.cso.models.command.RelayCommand
import sdmed.extra.cso.utils.calendar.CalendarView.Companion.CLASSIC
import sdmed.extra.cso.utils.calendar.listeners.IOnCloseClickListener
import sdmed.extra.cso.utils.calendar.utils.CalendarProperties
import sdmed.extra.cso.utils.calendar.utils.DateUtils.isMonthAfter
import sdmed.extra.cso.utils.calendar.utils.DateUtils.isMonthBefore
import sdmed.extra.cso.utils.calendar.utils.DateUtils.midnightCalendar

/**
 * This class is responsible for creating DatePicker dialog.
 *
 * Created by Applandeo Team.
 */
class DatePicker(
    private val context: Context,
    private val calendarProperties: CalendarProperties,
    private val setOnDismissListener: IOnDismissListener = object: IOnDismissListener {
        override fun onDismiss() {
        }
    }
) {
    val relayCommand: ICommand = RelayCommand({})
    lateinit var binding: DatePickerBinding
    fun show(): DatePicker {
        binding = DatePickerBinding.inflate(LayoutInflater.from(context))
        val alertDialog = MaterialAlertDialogBuilder(context, com.google.android.material.R.style.MaterialAlertDialog_Material3).create().apply {
            setView(binding.root)
        }

        if (calendarProperties.pagesColor != 0) {
            binding.root.setBackgroundColor(calendarProperties.pagesColor)
        }

        setTodayButtonVisibility(binding.btnToday)
        setDialogButtonsColors(binding.btnNegative, binding.btnToday)
        setOkButtonState(calendarProperties.calendarType == CalendarView.ONE_DAY_PICKER, binding.btnPositive)
        setDialogButtonsTypeface(binding.root)

        val calendarView = CalendarView(context = context, properties = calendarProperties)
        (relayCommand as? RelayCommand)?.addEventListener(object: IEventListener {
            override fun onEvent(data: Any?) {
                if (data !is String) return
                when (data) {
                    "btn_negative" -> alertDialog.cancel()
                    "btn_positive" -> {
                        alertDialog.cancel()
                        calendarProperties.onSelectDateListener?.onSelect(calendarView.selectedDates)
                    }
                    "btn_today" -> {
                        calendarView.showCurrentMonthPage()
                    }
                }
            }
        })

        calendarProperties.onSelectionAbilityListener = { enabled ->
            setOkButtonState(enabled, binding.btnPositive)
        }
        calendarProperties.calendar?.let {
            runCatching { calendarView.setDate(it) }
        }

        binding.flCalendarContainer.addView(calendarView)
        binding.vwLine.isVisible = calendarProperties.calendarType != CLASSIC
        binding.llButton.isVisible = calendarProperties.calendarType != CLASSIC
        binding.btnNegative.setOnClickListener {
            relayCommand.execute("btn_negative")
        }
        binding.btnPositive.setOnClickListener {
            relayCommand.execute("btn_positive")
        }
        binding.btnToday.setOnClickListener {
            relayCommand.execute("btn_today")
        }
        calendarView.setOnCloseClickListener(object : IOnCloseClickListener {
            override fun onCloseClick() {
                alertDialog.cancel()
            }
        })

        alertDialog.setOnDismissListener {
            this.setOnDismissListener.onDismiss()
        }
        alertDialog.show()

        return this
    }

    private fun setDialogButtonsTypeface(view: View) {
        calendarProperties.typeface?.let { typeface ->
            binding.btnToday.typeface = typeface
            binding.btnNegative.typeface = typeface
            binding.btnPositive.typeface = typeface
        }
    }

    private fun setDialogButtonsColors(btnNegative: androidx.appcompat.widget.AppCompatTextView, btnToday: AppCompatButton) {
        if (calendarProperties.dialogButtonsColor != 0) {
            btnNegative.setTextColor(ContextCompat.getColor(context, calendarProperties.dialogButtonsColor))
            btnToday.setTextColor(ContextCompat.getColor(context, calendarProperties.dialogButtonsColor))
        }
    }

    private fun setOkButtonState(enabled: Boolean, okButton: androidx.appcompat.widget.AppCompatTextView) {
        okButton.isEnabled = enabled
        if (calendarProperties.dialogButtonsColor == 0) return
        val stateResource = if (enabled) {
            calendarProperties.dialogButtonsColor
        } else {
            R.color.color_1F000000
        }

        okButton.setTextColor(ContextCompat.getColor(context, stateResource))
    }
    private fun setTodayButtonVisibility(todayButton: AppCompatButton) {
        calendarProperties.maximumDate?.let {
            if (it.isMonthBefore(midnightCalendar) || it.isMonthAfter(midnightCalendar)) {
                todayButton.visibility = View.GONE
            }
        }
    }
}
