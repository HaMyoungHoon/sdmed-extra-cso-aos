package sdmed.extra.cso.utils.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import sdmed.extra.cso.R
import androidx.core.content.withStyledAttributes

class OutLineTextView @JvmOverloads constructor (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {
    var strokeColor: Int = Color.WHITE
    var strokeWidthVal: Float = 2F
    init {
        context.withStyledAttributes(attrs, R.styleable.OutLineTextView) {
            strokeColor = getColor(R.styleable.OutLineTextView_textStrokeColor, Color.WHITE)
            strokeWidthVal = getFloat(R.styleable.OutLineTextView_textStrokeWidth, 2F)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //draw stroke
        val states: ColorStateList = textColors
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidthVal
        setTextColor(strokeColor)
        super.onDraw(canvas)

        //draw fill
        paint.style = Paint.Style.FILL
        setTextColor(states)
        super.onDraw(canvas)
    }
}