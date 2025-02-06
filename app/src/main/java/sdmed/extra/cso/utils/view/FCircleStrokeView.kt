package sdmed.extra.cso.utils.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.util.AttributeSet
import androidx.databinding.BindingAdapter
import sdmed.extra.cso.utils.FGlideSupport
import kotlin.math.min

class FCircleStrokeView: androidx.appcompat.widget.AppCompatImageView {
    lateinit var imagePath: Path
    lateinit var imageBorderPath: Path
    lateinit var borderPaint: Paint

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }
    private fun init() {
        imagePath = Path()
        imageBorderPath = Path()
        borderPaint = Paint()
        borderPaint.color = Color.WHITE
        borderPaint.strokeCap = Paint.Cap.ROUND
        borderPaint.strokeWidth = 1F
        borderPaint.style = Paint.Style.STROKE
    }
    private fun calculatePath(radius: Float) {
        val centerX = measuredWidth / 2F
        val centerY = measuredHeight / 2F

        imagePath.reset()
        imagePath.addCircle(centerX, centerY, radius, Path.Direction.CW)
        imagePath.close()

        val radiusBorder = if (radius - 5F < 0F) 0F else radius - 5F
        imageBorderPath.reset()
        imageBorderPath.addCircle(centerX, centerY, radius, Path.Direction.CW)
        imageBorderPath.close()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(imageBorderPath, borderPaint)
        canvas.clipPath(imagePath)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
        calculatePath(min(width / 2F, height / 2F) - borderPaint.strokeWidth)
    }

    companion object {
        @JvmStatic
        @BindingAdapter("circleStrokeViewRadius")
        fun setRadius(circleStrokeView: FCircleStrokeView, circleStrokeViewRadius: Float) {
            circleStrokeView.calculatePath(circleStrokeViewRadius)
        }
        @JvmStatic
        @BindingAdapter("circleStrokeViewBorderColor")
        fun setBorderColor(circleStrokeView: FCircleStrokeView, circleStrokeViewBorderColor: Int) {
            circleStrokeView.borderPaint.color = circleStrokeViewBorderColor
            circleStrokeView.invalidate()
        }
        @JvmStatic
        @BindingAdapter("circleStrokeViewBorderWidth")
        fun setBorderWidth(circleStrokeView: FCircleStrokeView, circleStrokeViewBorderWidth: Float) {
            if (circleStrokeViewBorderWidth >= 0F) {
                circleStrokeView.borderPaint.strokeWidth = circleStrokeViewBorderWidth
                circleStrokeView.invalidate()
            }
        }
        @JvmStatic
        @BindingAdapter("circleStrokeViewSource")
        fun setSource(circleStrokeView: FCircleStrokeView, circleStrokeViewSource: String) {
            FGlideSupport.imageLoad(circleStrokeViewSource, circleStrokeView)
        }
    }
}