package sdmed.extra.cso.models.adapter

import android.content.res.ColorStateList
import sdmed.extra.cso.R
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

class TextViewAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter("textColorResId")
        fun textViewTextColorResId(textView: AppCompatTextView, @ColorRes textColorResId: Int?) {
            textColorResId ?: return
            var colorBuff = textColorResId
            if (colorBuff == 0) {
                colorBuff = R.color.def_button_foreground
            }
            val color = try {
                textView.context.getColor(colorBuff)
            } catch (_: Exception) {
                textView.textColors.defaultColor
            }
            textView.setTextColor(color)
        }
        @JvmStatic
        @BindingAdapter("textResId")
        fun textViewTextResId(textView: AppCompatTextView, @StringRes textResId: Int?) {
            textResId ?: return
            if (textResId == 0) {
                return
            }
            val text = try {
                textView.context.getString(textResId)
            } catch (_: Exception) {
                textView.text
            }
            textView.text = text
        }
        @JvmStatic
        @BindingAdapter(value = ["circleNumberSrc", "circleNumberSolid", "circleNumberStroke"], requireAll = false)
        fun setCircleNumberTextView(textView: AppCompatTextView, @ColorRes circleNumberSrc: Int?, circleNumberSolid: Int?, @ColorRes circleNumberStroke: Int?) {
            val drawable = ContextCompat.getDrawable(textView.context, R.drawable.shape_circle)
            val shape = (drawable as? LayerDrawable)?.getDrawable(0) as? GradientDrawable
            if (circleNumberSrc == null) {
                textView.text = ""
            } else {
                textView.text = circleNumberSrc.toString()
            }
            if (shape == null) {
                return
            }
            val shapeColor = if (circleNumberSolid == null) {
                textView.context.getColor(R.color.color_AA000000)
            } else {
                textView.context.getColor(circleNumberSolid)
            }
            shape.setColor(shapeColor)
            val shapeStroke = if (circleNumberStroke == null) {
                textView.context.getColor(R.color.color_AAFFFFFF)
            } else {
                textView.context.getColor(circleNumberStroke)
            }
            shape.setStroke(1, shapeStroke)
            textView.background = shape
        }
        @JvmStatic
        @BindingAdapter("backgroundTintResId")
        fun textViewBackgroundTintResId(textView: AppCompatTextView, @ColorRes backgroundTintResId: Int?) {
            backgroundTintResId ?: return
            var colorBuff = backgroundTintResId
            if (colorBuff == 0) {
                colorBuff = R.color.def_foreground
            }
            val color = try {
                textView.context.getColor(colorBuff)
            } catch (_: Exception) {
                textView.textColors.defaultColor
            }
            textView.backgroundTintList = ColorStateList.valueOf(color)
        }
    }
}