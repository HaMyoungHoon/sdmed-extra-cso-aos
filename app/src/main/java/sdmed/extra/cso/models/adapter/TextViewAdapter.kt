package sdmed.extra.cso.models.adapter

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter

class TextViewAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter("textColorResId")
        fun textViewTextColorResId(textView: AppCompatTextView, @ColorRes textColorResId: Int?) {
            textColorResId ?: return
            if (textColorResId == 0) {
                return
            }
            val color = try {
                textView.context.getColor(textColorResId)
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
    }
}