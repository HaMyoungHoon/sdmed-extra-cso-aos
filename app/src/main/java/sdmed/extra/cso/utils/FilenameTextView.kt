package sdmed.extra.cso.utils

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.models.common.WriteFontFamily

class FilenameTextView: AppCompatTextView {
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
    }
    private fun updateText(data: String) {
        val spannableString = SpannableString(data)
        spannableString.apply {
            setSpan(BackgroundColorSpan(context.getColor(R.color.disable_back_gray)), 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(AbsoluteSizeSpan(FExtensions.dpToPx(context, 16F)), 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setTypeface(Typeface.create(WriteFontFamily.DEFAULT.fontFileName, Typeface.NORMAL))
        }
        setText(spannableString, BufferType.SPANNABLE)
        setTextColor(context.getColor(R.color.gray))
        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
    }

    companion object {
        @JvmStatic
        @BindingAdapter("filenameTextViewText")
        fun setFilenameTextViewText(textView: FilenameTextView, data: String?) {
            textView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                data?.let {
                    textView.updateText(it)
                }
            }
        }
    }
}