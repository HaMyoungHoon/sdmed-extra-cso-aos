package sdmed.extra.cso.utils.calendar.adapters

import android.content.Context
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import sdmed.extra.cso.R
import sdmed.extra.cso.utils.calendar.CalendarView
import sdmed.extra.cso.utils.calendar.extensions.CalendarGridView
import sdmed.extra.cso.utils.calendar.listeners.DayRowClickListener
import sdmed.extra.cso.utils.calendar.listeners.DayRowLongClickListener
import sdmed.extra.cso.utils.calendar.utils.CalendarProperties
import sdmed.extra.cso.utils.calendar.utils.SelectedDay
import java.util.Calendar
import java.util.Date

/**
 * This class is responsible for loading a calendar page content.
 *
 *
 * Created by Applandeo team
 */
class CalendarPageAdapter(
    private val context: Context,
    private val calendarView: CalendarView,
    private val calendarProperties: CalendarProperties
) : PagerAdapter() {
    private lateinit var calendarGridView: CalendarGridView
    private var pageMonth = 0
    val selectedDays: List<SelectedDay>
        get() = calendarProperties.selectedDays
    var selectedDay: SelectedDay
        get() = calendarProperties.selectedDays.first()
        set(selectedDay) {
            calendarProperties.setSelectedDay(selectedDay)
            informDatePicker()
        }
    init {
        informDatePicker()
    }

    override fun getCount() = CalendarProperties.CALENDAR_SIZE
    override fun getItemPosition(any: Any) = POSITION_NONE
    override fun isViewFromObject(view: View, any: Any) = view === any
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        calendarGridView = inflate(context, R.layout.calendar_view_grid, null) as CalendarGridView
        loadMonth(position)
        calendarGridView.onItemClickListener = DayRowClickListener(calendarView, this, calendarProperties, pageMonth)
        calendarGridView.onItemLongClickListener = DayRowLongClickListener(calendarProperties)
        container.addView(calendarGridView)
        return calendarGridView
    }

    fun addSelectedDay(selectedDay: SelectedDay) {
        if (selectedDay !in calendarProperties.selectedDays) {
            calendarProperties.selectedDays.add(selectedDay)
            informDatePicker()
            return
        }
        calendarProperties.selectedDays.remove(selectedDay)
        informDatePicker()
    }

    /**
     * This method inform DatePicker about ability to return selected days
     */
    private fun informDatePicker() {
        calendarProperties.onSelectionAbilityListener?.invoke(calendarProperties.selectedDays.size > 0)
    }

    /**
     * This method fill calendar GridView with days
     *
     * @param position Position of current page in ViewPager
     */
    private fun loadMonth(position: Int) {
        val days = mutableListOf<Date>()

        // Get Calendar object instance
        val calendar = (calendarProperties.firstPageCalendarDate.clone() as Calendar).apply {
            // Add months to Calendar (a number of months depends on ViewPager position)
            add(Calendar.MONTH, position)

            // Set day of month as 1
            set(Calendar.DAY_OF_MONTH, 1)
        }

        getPageDaysProperties(calendar)

        // Get a number of the first day of the week
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        // Count when month is beginning
        val firstDayOfWeek = calendarProperties.firstDayOfWeek
        val monthBeginningCell =
            (if (dayOfWeek < firstDayOfWeek) 7 else 0) + dayOfWeek - firstDayOfWeek

        // Subtract a number of beginning days, it will let to load a part of a previous month
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell)

        /*
        Get all days of one page (42 is a number of all possible cells in one page
        (a part of previous month, current month and a part of next month))
         */
        while (days.size < 42) {
            days.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        pageMonth = calendar.get(Calendar.MONTH) - 1
        val calendarDayAdapter =
            CalendarDayAdapter(context, this, calendarProperties, days, pageMonth)

        informDatePicker()
        calendarGridView.adapter = calendarDayAdapter
    }

    private fun getPageDaysProperties(calendar: Calendar) {
        val pageCalendarDays = calendarProperties.onPagePrepareListener?.invoke(calendar)
        if (pageCalendarDays != null) {
            val diff = pageCalendarDays.minus(calendarProperties.calendarDayProperties).distinct()
            calendarProperties.calendarDayProperties.addAll(diff)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(any as View)
    }
}