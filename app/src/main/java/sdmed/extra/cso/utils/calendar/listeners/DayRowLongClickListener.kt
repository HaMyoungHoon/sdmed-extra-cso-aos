package sdmed.extra.cso.utils.calendar.listeners

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import sdmed.extra.cso.utils.calendar.EventDay
import sdmed.extra.cso.utils.calendar.utils.CalendarProperties
import sdmed.extra.cso.utils.calendar.utils.DateUtils.isBetweenMinAndMax
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

/**
 * This class is responsible for handle click events
 *
 *
 * Created by Applandeo Team.
 */
class DayRowLongClickListener(private val calendarProperties: CalendarProperties) : OnItemLongClickListener {
    override fun onItemLongClick(adapterView: AdapterView<*>, view: View, position: Int, id: Long): Boolean {
        calendarProperties.onDayClickListener?.let {
            onLongClick(GregorianCalendar().apply {
                time = adapterView.getItemAtPosition(position) as Date
            })
        }
        return true
    }
    private fun onLongClick(day: Calendar) = callOnLongClickListener(calendarProperties.eventDays.firstOrNull { it.calendar == day } ?: EventDay(day))
    private fun callOnLongClickListener(eventDay: EventDay) {
        val enabledDay = calendarProperties.disabledDays.contains(eventDay.calendar) || !eventDay.calendar.isBetweenMinAndMax(calendarProperties)
        eventDay.isEnabled = enabledDay
        calendarProperties.onDayLongClickListener?.onDayLongClick(eventDay)
    }
}