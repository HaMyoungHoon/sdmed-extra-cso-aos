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
import kotlin.math.sqrt

class FHexagonMaskView: androidx.appcompat.widget.AppCompatImageView {
    lateinit var hexagonPath: Path
    lateinit var hexagonBorderPath: Path
    lateinit var borderPaint: Paint
    var cornerLength: Float = 10F

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
        hexagonPath = Path()
        hexagonBorderPath = Path()
        borderPaint = Paint()
        borderPaint.color = Color.WHITE
        borderPaint.strokeCap = Paint.Cap.ROUND
        borderPaint.strokeWidth = 0F
        borderPaint.style = Paint.Style.STROKE
    }
    fun calculatePath(radius: Float) {
        val halfRadius = radius / 2F
        val triangleHeight = (sqrt(3.0) * halfRadius).toFloat()
        val centerX = measuredWidth / 2F
        val centerY = measuredHeight / 2F
        val corner = cornerLength / 1
        val halfCorner = corner / 2

        hexagonPath.reset()

        // ↓
        hexagonPath.moveTo(centerX + corner, centerY + radius - halfCorner)
        hexagonPath.quadTo(centerX, centerY + radius, centerX - corner, centerY + radius - halfCorner)

        // ↙
        hexagonPath.lineTo(centerX - triangleHeight + corner, centerY + halfRadius + halfCorner)
        hexagonPath.quadTo(centerX - triangleHeight, centerY + halfRadius, centerX - triangleHeight, centerY + halfRadius - halfCorner)

        // ↖
        hexagonPath.lineTo(centerX - triangleHeight, centerY - halfRadius + halfCorner)
        hexagonPath.quadTo(centerX - triangleHeight, centerY - halfRadius, centerX - triangleHeight + corner, centerY - halfRadius - halfCorner)

        // ↑
        hexagonPath.lineTo(centerX - corner, centerY - radius + halfCorner)
        hexagonPath.quadTo(centerX, centerY - radius, centerX + corner, centerY - radius + halfCorner)

        // ↗
        hexagonPath.lineTo(centerX + triangleHeight - corner, centerY - halfRadius - halfCorner)
        hexagonPath.quadTo(centerX + triangleHeight, centerY - halfRadius, centerX + triangleHeight, centerY - halfRadius + halfCorner)

        // ↘
        hexagonPath.lineTo(centerX + triangleHeight, centerY + halfRadius - halfCorner)
        hexagonPath.quadTo(centerX + triangleHeight, centerY + halfRadius, centerX + triangleHeight - corner, centerY + halfRadius + halfCorner)

//        hexagonPath.moveTo(centerX, centerY + radius)
//        hexagonPath.lineTo(centerX - triangleHeight, centerY + halfRadius)
//        hexagonPath.lineTo(centerX - triangleHeight, centerY - halfRadius)
//        hexagonPath.lineTo(centerX, centerY - radius)
//        hexagonPath.lineTo(centerX + triangleHeight, centerY - halfRadius)
//        hexagonPath.lineTo(centerX + triangleHeight, centerY + halfRadius)
        hexagonPath.close()

        val radiusBorder = if (radius - 5F < 0F) 0F else radius - 5F
        val halfRadiusBorder = radiusBorder / 2F
        val triangleBorderHeight = (sqrt(3.0) * halfRadiusBorder).toFloat()
        hexagonBorderPath.reset()
        hexagonBorderPath.moveTo(centerX + corner, centerY + radiusBorder - halfCorner)
        hexagonBorderPath.quadTo(centerX, centerY + radiusBorder, centerX - corner, centerY + radiusBorder - halfCorner)
        hexagonBorderPath.lineTo(centerX - triangleBorderHeight + corner, centerY + halfRadiusBorder + halfCorner)
        hexagonBorderPath.quadTo(centerX - triangleBorderHeight, centerY + halfRadiusBorder, centerX - triangleBorderHeight, centerY + halfRadiusBorder - halfCorner)
        hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY - halfRadiusBorder + halfCorner)
        hexagonBorderPath.quadTo(centerX - triangleBorderHeight, centerY - halfRadiusBorder, centerX - triangleBorderHeight + corner, centerY - halfRadiusBorder - halfCorner)
        hexagonBorderPath.lineTo(centerX - corner, centerY - radiusBorder + halfCorner)
        hexagonBorderPath.quadTo(centerX, centerY - radiusBorder, centerX + corner, centerY - radiusBorder + halfCorner)
        hexagonBorderPath.lineTo(centerX + triangleBorderHeight - corner, centerY - halfRadiusBorder - halfCorner)
        hexagonBorderPath.quadTo(centerX + triangleBorderHeight, centerY - halfRadiusBorder, centerX + triangleBorderHeight, centerY - halfRadiusBorder + halfCorner)
        hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY + halfRadiusBorder - halfCorner)
        hexagonBorderPath.quadTo(centerX + triangleBorderHeight, centerY + halfRadiusBorder, centerX + triangleBorderHeight - corner, centerY + halfRadiusBorder + halfCorner)

//        hexagonBorderPath.moveTo(centerX, centerY + radiusBorder)
//        hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY + halfRadiusBorder)
//        hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY - halfRadiusBorder)
//        hexagonBorderPath.lineTo(centerX, centerY - radiusBorder)
//        hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY - halfRadiusBorder)
//        hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY + halfRadiusBorder)
        hexagonBorderPath.close()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(hexagonBorderPath, borderPaint)
        canvas.clipPath(hexagonPath)
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
        @BindingAdapter("hexagonMaskViewRadius")
        fun setRadius(hexagonMaskView: FHexagonMaskView, hexagonMaskViewRadius: Float) {
            hexagonMaskView.calculatePath(hexagonMaskViewRadius)
        }
        @JvmStatic
        @BindingAdapter("hexagonMaskViewBorderColor")
        fun setBorderColor(hexagonMaskView: FHexagonMaskView, hexagonMaskViewBorderColor: Int) {
            hexagonMaskView.borderPaint.color = hexagonMaskViewBorderColor
            hexagonMaskView.invalidate()
        }
        @JvmStatic
        @BindingAdapter("hexagonMaskViewBorderWidth")
        fun setBorderWidth(hexagonMaskView: FHexagonMaskView, hexagonMaskViewBorderWidth: Float) {
            if (hexagonMaskViewBorderWidth >= 0F) {
                hexagonMaskView.borderPaint.strokeWidth = hexagonMaskViewBorderWidth
                hexagonMaskView.invalidate()
            }
        }
        @JvmStatic
        @BindingAdapter("hexagonMaskViewCornerLength")
        fun setCornerLength(hexagonMaskView: FHexagonMaskView, hexagonMaskViewCornerLength: Float) {
            if (hexagonMaskViewCornerLength >= 0F) {
                hexagonMaskView.cornerLength = hexagonMaskViewCornerLength
                hexagonMaskView.invalidate()
            }
        }
        @JvmStatic
        @BindingAdapter("hexagonMaskViewSource")
        fun setSource(hexagonMaskView: FHexagonMaskView, hexagonMaskViewSource: String?) {
            FGlideSupport.imageLoad(hexagonMaskView, hexagonMaskViewSource, hexagonMaskView.width, hexagonMaskView.height)
        }
    }
}