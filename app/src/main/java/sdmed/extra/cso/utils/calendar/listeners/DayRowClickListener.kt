package sdmed.extra.cso.utils.calendar.listeners

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import sdmed.extra.cso.R
import sdmed.extra.cso.utils.calendar.CalendarUtils.getDatesRange
import sdmed.extra.cso.utils.calendar.CalendarView
import sdmed.extra.cso.utils.calendar.EventDay
import sdmed.extra.cso.utils.calendar.adapters.CalendarPageAdapter
import sdmed.extra.cso.utils.calendar.utils.CalendarProperties
import sdmed.extra.cso.utils.calendar.utils.DateUtils.isBetweenMinAndMax
import sdmed.extra.cso.utils.calendar.utils.SelectedDay
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import sdmed.extra.cso.utils.calendar.utils.DayColorsUtils.setCurrentMonthDayColors
import sdmed.extra.cso.utils.calendar.utils.DayColorsUtils.setSelectedDayColors

/**
 * This class is responsible for handle click events
 *
 *
 * Created by Applandeo Team.
 */
class DayRowClickListener(
    private val calendarView: CalendarView,
    private val calendarPageAdapter: CalendarPageAdapter,
    private val calendarProperties: CalendarProperties,
    pageMonth: Int
) : OnItemClickListener {
    private val pageMonth = if (pageMonth < 0) 11 else pageMonth

    override fun onItemClick(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
        val day = GregorianCalendar().apply {
            time = adapterView.getItemAtPosition(position) as Date
        }
        if (calendarProperties.onDayClickListener != null) onClick(day)
        if (calendarProperties.selectionDisabled) return
        when (calendarProperties.calendarType) {
            CalendarView.ONE_DAY_PICKER -> selectOneDay(view, day)
            CalendarView.MANY_DAYS_PICKER -> selectManyDays(view, day)
            CalendarView.RANGE_PICKER -> selectRange(view, day)
            CalendarView.CLASSIC -> {
                if(day.isCurrentMonthDay() && day.isActiveDay()) {
                    calendarPageAdapter.selectedDay = SelectedDay(day, view)
                    calendarProperties.onCloseClickListener?.onCloseClick()
                    calendarProperties.onSelectDateListener?.onSelect(calendarView.selectedDates)
                }
            }
        }
    }
    private fun selectOneDay(view: View, day: Calendar) {
        val previousSelectedDay = calendarPageAdapter.selectedDay
        val dayLabel = view.findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.day_label)
        if (isAnotherDaySelected(previousSelectedDay, day)) {
            selectDay(dayLabel, day)
            reverseUnselectedColor(previousSelectedDay)
            calendarPageAdapter.notifyDataSetChanged()
        }
    }
    private fun selectManyDays(view: View, day: Calendar) {
        val dayLabel = view.findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.day_label)
        if (!day.isCurrentMonthDay() || !day.isActiveDay()) return
        val selectedDay = SelectedDay(day, dayLabel)
        if (!calendarPageAdapter.selectedDays.contains(selectedDay)) {
            setSelectedDayColors(dayLabel, day, calendarProperties)
        } else {
            reverseUnselectedColor(selectedDay)
        }
        calendarPageAdapter.addSelectedDay(selectedDay)
    }
    private fun selectRange(view: View, day: Calendar) {
        val dayLabel = view.findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.day_label)

        if ((!day.isCurrentMonthDay() && !calendarProperties.selectionBetweenMonthsEnabled) || !day.isActiveDay()) return

        val selectedDays = calendarPageAdapter.selectedDays

        when {
            selectedDays.size > 1 -> clearAndSelectOne(dayLabel, day)
            selectedDays.size == 1 -> selectOneAndRange(dayLabel, day)
            selectedDays.isEmpty() -> selectDay(dayLabel, day)
        }
    }
    private fun clearAndSelectOne(dayLabel: androidx.appcompat.widget.AppCompatTextView, day: Calendar) {
        calendarPageAdapter.selectedDays.forEach { reverseUnselectedColor(it) }
        selectDay(dayLabel, day)
        calendarPageAdapter.notifyDataSetChanged()
    }
    private fun selectOneAndRange(dayLabel: androidx.appcompat.widget.AppCompatTextView, day: Calendar) {
        val previousSelectedDayCalendar = calendarPageAdapter.selectedDay.calendar
        previousSelectedDayCalendar.getDatesRange(day)
            .filter { it !in calendarProperties.disabledDays }
            .forEach { calendarPageAdapter.addSelectedDay(SelectedDay(it)) }

        if (isOutOfMaxRange(previousSelectedDayCalendar, day)) return
        setSelectedDayColors(dayLabel, day, calendarProperties)
        calendarPageAdapter.addSelectedDay(SelectedDay(day, dayLabel))
        calendarPageAdapter.notifyDataSetChanged()
    }
    private fun selectDay(dayLabel: androidx.appcompat.widget.AppCompatTextView, day: Calendar) {
        setSelectedDayColors(dayLabel, day, calendarProperties)
        calendarPageAdapter.selectedDay = SelectedDay(day, dayLabel)
    }
    private fun reverseUnselectedColor(selectedDay: SelectedDay) {
        setCurrentMonthDayColors(
            selectedDay.calendar,
            selectedDay.view as? androidx.appcompat.widget.AppCompatTextView,
            calendarProperties
        )
    }
    private fun Calendar.isCurrentMonthDay() = this[Calendar.MONTH] == pageMonth && this.isBetweenMinAndMax(calendarProperties)
    private fun Calendar.isActiveDay() = !calendarProperties.disabledDays.contains(this)
    private fun isOutOfMaxRange(firstDay: Calendar, lastDay: Calendar): Boolean {
        // Number of selected days plus one last day
        val numberOfSelectedDays = firstDay.getDatesRange(lastDay).size + 1
        val daysMaxRange: Int = calendarProperties.maximumDaysRange

        return daysMaxRange != 0 && numberOfSelectedDays >= daysMaxRange
    }
    private fun isAnotherDaySelected(selectedDay: SelectedDay, day: Calendar) = day != selectedDay.calendar && day.isCurrentMonthDay() && day.isActiveDay()
    private fun onClick(day: Calendar) {
        if (calendarProperties.eventDays.isEmpty()) {
            callOnClickListener(EventDay(day))
            return
        }
        val eventDay = calendarProperties.eventDays.firstOrNull { it.calendar == day }
        callOnClickListener(eventDay ?: EventDay(day))
    }
    private fun callOnClickListener(eventDay: EventDay) {
        val enabledDay = calendarProperties.disabledDays.contains(eventDay.calendar) || !eventDay.calendar.isBetweenMinAndMax(calendarProperties)
        eventDay.isEnabled = enabledDay
        calendarProperties.onDayClickListener?.onDayClick(eventDay)
    }
}