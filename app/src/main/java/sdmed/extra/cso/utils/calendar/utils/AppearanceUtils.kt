package sdmed.extra.cso.utils.calendar.utils

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.CalendarViewBinding

object AppearanceUtils {
    internal fun CalendarViewBinding.setAbbreviationsLabels(color: Int, firstDayOfWeek: Int) {
        val labels = getAbbreviationsTextViews()
        val abbreviations = this.root.context.resources.getStringArray(R.array.week_desc_array)
        labels.forEachIndexed { index, label ->
            label.text = abbreviations[(index + firstDayOfWeek - 1) % 7]

            if (color != 0) {
                label.setTextColor(color)
            }
        }
    }
    internal fun CalendarViewBinding.setAbbreviationsLabelsSize(size: Float) {
        val labels = getAbbreviationsTextViews()
        val maxTextSize = this.root.resources.getDimensionPixelSize(R.dimen.calendar_month_textsize)
        labels.forEachIndexed { _, label ->
            if (size > 0.0 && size <= maxTextSize) {
                label.textSize = size
            }
        }
    }
    private fun CalendarViewBinding.getAbbreviationsTextViews() = listOf(
        mondayLabel,
        tuesdayLabel,
        wednesdayLabel,
        thursdayLabel,
        fridayLabel,
        saturdayLabel,
        sundayLabel)
    internal fun CalendarViewBinding.setTypeface(typeface: Typeface?) {
        if (typeface == null) return
        getAbbreviationsTextViews().forEach { label ->
            label.typeface = typeface
        }
    }
    internal fun CalendarViewBinding.setHeaderColor(color: Int) {
        if (color == 0) return
        this.clCalendarHeader.setBackgroundColor(color)
    }
    internal fun CalendarViewBinding.setHeaderLabelColor(color: Int) {
        if (color == 0) return
        this.tvCurrentDate.setTextColor(color)
    }
    internal fun CalendarViewBinding.setHeaderTypeface(typeface: Typeface?) {
        if (typeface == null) return
        this.tvCurrentDate.typeface = typeface
    }
    internal fun CalendarViewBinding.setAbbreviationsBarColor(color: Int) {
        if (color == 0) return
        this.llAbbreviationBar.setBackgroundColor(color)
    }
    internal fun CalendarViewBinding.setPagesColor(color: Int) {
        if (color == 0) return
        this.calendarViewPager.setBackgroundColor(color)
    }
    internal fun CalendarViewBinding.setPreviousButtonImage(drawable: Drawable?) {
        if (drawable == null) return
        this.ivPrevious.setImageDrawable(drawable)
    }
    internal fun CalendarViewBinding.setForwardButtonImage(drawable: Drawable?) {
        if (drawable == null) return
        this.ivSubsequent.setImageDrawable(drawable)
    }
    internal fun CalendarViewBinding.setHeaderVisibility(visibility: Int) {
        this.clCalendarHeader.visibility = visibility
    }
    internal fun CalendarViewBinding.setNavigationVisibility(visibility: Int) {
        this.ivPrevious.visibility = visibility
        this.ivSubsequent.visibility = visibility
    }
    internal fun CalendarViewBinding.setAbbreviationsBarVisibility(visibility: Int) {
        this.llAbbreviationBar.visibility = visibility
    }
}