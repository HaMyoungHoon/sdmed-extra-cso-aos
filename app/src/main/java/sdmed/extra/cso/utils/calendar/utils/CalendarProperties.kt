package sdmed.extra.cso.utils.calendar.utils

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import sdmed.extra.cso.R
import sdmed.extra.cso.utils.calendar.CalendarDay
import sdmed.extra.cso.utils.calendar.CalendarView
import sdmed.extra.cso.utils.calendar.CalendarView.Companion.CLASSIC
import sdmed.extra.cso.utils.calendar.EventDay
import sdmed.extra.cso.utils.calendar.exceptions.ErrorsMessages
import sdmed.extra.cso.utils.calendar.exceptions.UnsupportedMethodsException
import sdmed.extra.cso.utils.calendar.listeners.IOnCalendarPageChangeListener
import sdmed.extra.cso.utils.calendar.listeners.IOnCloseClickListener
import sdmed.extra.cso.utils.calendar.listeners.IOnDayClickListener
import sdmed.extra.cso.utils.calendar.listeners.IOnDayLongClickListener
import sdmed.extra.cso.utils.calendar.listeners.IOnSelectDateListener
import sdmed.extra.cso.utils.calendar.utils.DateUtils.isEqual
import sdmed.extra.cso.utils.calendar.utils.DateUtils.isFullDatesRange
import sdmed.extra.cso.utils.calendar.utils.DateUtils.midnightCalendar
import sdmed.extra.cso.utils.calendar.utils.DateUtils.setMidnight
import sdmed.extra.cso.utils.calendar.utils.DayColorsUtils.parseColor
import java.util.Calendar

/**
 * This class contains all properties of the calendar
 *
 * Created by Applandeo Team.
 */

typealias IOnPagePrepareListener = (Calendar) -> List<CalendarDay>
typealias IOnSelectionAbilityListener = (Boolean) -> Unit

class CalendarProperties(private val context: Context) {

    var calendarType: Int = CLASSIC

    var headerColor: Int = 0
        get() = if (field <= 0) field else context.parseColor(field)

    var headerLabelColor: Int = 0
        get() = if (field <= 0) field else context.parseColor(field)

    var selectionColor: Int = 0
        get() = if (field == 0) context.parseColor(R.color.secondary) else field

    var todayLabelColor: Int = 0
        get() = if (field == 0) context.parseColor(R.color.primary) else field

    var sunDayLabelColor: Int = 0
        get() = if (field == 0) context.parseColor(R.color.red) else field

    var saturDayLabelColor: Int = 0
        get() = if (field == 0) context.parseColor(R.color.primary) else field

    var sunDayDisabledLabelColor: Int = 0
        get() = if (field == 0) context.parseColor(R.color.red2) else field

    var saturDayDisabledLabelColor: Int = 0
        get() = if (field == 0) context.parseColor(R.color.purple_200) else field

    var todayColor: Int = 0
        get() = if (field <= 0) field else context.parseColor(field)

    var dialogButtonsColor: Int = 0

    var itemLayoutResource: Int = R.layout.calendar_view_day

    var selectionBackground: Int = R.drawable.shape_circle

    var disabledDaysLabelsColor: Int = 0
        get() = if (field == 0) context.parseColor(R.color.gray) else field

    var highlightedDaysLabelsColor: Int = 0
        get() = if (field == 0) context.parseColor(R.color.gray) else field

    var pagesColor: Int = 0

    var abbreviationsBarColor: Int = 0

    var abbreviationsLabelsColor: Int = 0

    var abbreviationsLabelsSize: Float = 0F

    var daysLabelsColor: Int = 0
        get() = if (field == 0) context.parseColor(R.color.def_foreground) else field

    var selectionLabelColor: Int = 0
        get() = if (field == 0) context.parseColor(R.color.def_background) else field

    var anotherMonthsDaysLabelsColor: Int = 0
        get() = if (field == 0) context.parseColor(R.color.gray) else field

    var headerVisibility: Int = View.VISIBLE

    var typeface: Typeface? = null

    var todayTypeface: Typeface? = null

    var abbreviationsBarVisibility: Int = View.VISIBLE

    var navigationVisibility: Int = View.VISIBLE

    var eventsEnabled: Boolean = false

    var swipeEnabled: Boolean = true

    var selectionDisabled: Boolean = false

    var previousButtonSrc: Drawable? = null

    var forwardButtonSrc: Drawable? = null

    var selectionBetweenMonthsEnabled: Boolean = false

    val firstPageCalendarDate: Calendar = midnightCalendar

    var firstDayOfWeek = firstPageCalendarDate.firstDayOfWeek

    var calendar: Calendar? = null

    var minimumDate: Calendar? = null

    var maximumDate: Calendar? = null

    var maximumDaysRange: Int = 0

    var onDayClickListener: IOnDayClickListener? = null

    var onDayLongClickListener: IOnDayLongClickListener? = null

    var onSelectDateListener: IOnSelectDateListener? = null

    var onSelectionAbilityListener: IOnSelectionAbilityListener? = null

    var onPreviousPageChangeListener: IOnCalendarPageChangeListener? = null

    var onForwardPageChangeListener: IOnCalendarPageChangeListener? = null

    var onCloseClickListener: IOnCloseClickListener? = null

    var eventDays: List<EventDay> = mutableListOf()

    var calendarDayProperties: MutableList<CalendarDay> = mutableListOf()

    var disabledDays: List<Calendar> = mutableListOf()
        set(disabledDays) {
            selectedDays = selectedDays.filter {
                !disabledDays.contains(it.calendar)
            }.toMutableList()

            field = disabledDays.map { it.setMidnight() }.toList()
        }

    var highlightedDays: List<Calendar> = mutableListOf()
        set(highlightedDays) {
            field = highlightedDays.map { it.setMidnight() }.toList()
        }

    var selectedDays = mutableListOf<SelectedDay>()
        private set

    fun setSelectedDay(calendar: Calendar) = setSelectedDay(SelectedDay(calendar))

    fun setSelectedDay(selectedDay: SelectedDay) {
        selectedDays.clear()
        selectedDays.add(selectedDay)
    }

    @Throws(UnsupportedMethodsException::class)
    fun setSelectDays(days: List<Calendar>) {
//        if (calendarType == CalendarView.ONE_DAY_PICKER) {
//            throw UnsupportedMethodsException(ErrorsMessages.ONE_DAY_PICKER_MULTIPLE_SELECTION)
//        }

        if (calendarType == CalendarView.RANGE_PICKER && !days.isFullDatesRange()) {
            throw UnsupportedMethodsException(ErrorsMessages.RANGE_PICKER_NOT_RANGE)
        }

        selectedDays = days
            .map { SelectedDay(it.setMidnight()) }
            .filterNot { it.calendar in disabledDays }
            .toMutableList()
    }

    var onPagePrepareListener: IOnPagePrepareListener? = null

    fun findDayProperties(calendar: Calendar): CalendarDay? =
        calendarDayProperties.find { it.calendar.isEqual(calendar) }

    companion object {
        /**
         * A number of months (pages) in the calendar
         * 2401 months means 1200 months (100 years) before and 1200 months after the current month
         */
        const val CALENDAR_SIZE = 2401
        const val FIRST_VISIBLE_PAGE = CALENDAR_SIZE / 2
    }
}
