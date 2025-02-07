package sdmed.extra.cso.utils.calendar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toDrawable

object CalendarUtils {
    /**
     * Created by Applandeo Team.
     */
    /**
     *Utils method to create drawable containing text
     */
    fun Context.getDrawableText(text: String, typeface: Typeface?, color: Int, size: Int): Drawable {
        val bitmap = createBitmap(48, 48)

        val canvas = Canvas(bitmap)
        val scale = this.resources.displayMetrics.density

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.typeface = typeface ?: Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            this.color = ContextCompat.getColor(this@getDrawableText, color)
            this.textSize = (size * scale).toInt().toFloat()
        }

        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val x = (bitmap.width - bounds.width()) / 2
        val y = (bitmap.height + bounds.height()) / 2
        canvas.drawText(text, x.toFloat(), y.toFloat(), paint)

        return bitmap.toDrawable(this.resources)
    }
    /**
     * This method returns a list of calendar objects between two dates
     * @param this representing a first selected date
     * @param toCalendar Calendar representing a last selected date
     * @return List of selected dates between two dates
     */
    fun Calendar.getDatesRange(toCalendar: Calendar): List<Calendar> =
        if (toCalendar.before(this)) {
            getCalendarsBetweenDates(toCalendar.time, this.time)
        } else {
            getCalendarsBetweenDates(this.time, toCalendar.time)
        }
    private fun getCalendarsBetweenDates(dateFrom: Date, dateTo: Date): List<Calendar> {
        val calendars = mutableListOf<Calendar>()
        val calendarFrom = Calendar.getInstance().apply { time = dateFrom }
        val calendarTo = Calendar.getInstance().apply { time = dateTo }
        val daysBetweenDates = TimeUnit.MILLISECONDS.toDays(calendarTo.timeInMillis - calendarFrom.timeInMillis).toInt()
        for (i in 0 until daysBetweenDates) {
            val calendar = calendarFrom.clone() as Calendar
            calendar.add(Calendar.DATE, i)
            calendars.add(calendar)
        }
        return calendars
    }
}