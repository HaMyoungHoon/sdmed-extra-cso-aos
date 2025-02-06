package sdmed.extra.cso.models.adapter

import android.text.InputFilter
import android.widget.EditText
import androidx.databinding.BindingAdapter

class EditTextAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter(value = ["inputFilterPattern", "inputFilterMaxLength"], requireAll = false)
        fun editTextInputFilterAdapter(editText: EditText, inputFilterPattern: String?, inputFilterMaxLength: Int?) {
            if (inputFilterPattern.isNullOrEmpty() && inputFilterMaxLength == null) return

            val filters: MutableList<InputFilter> = mutableListOf()
            if (!inputFilterPattern.isNullOrEmpty()) {
                filters.add(InputFilter { source, _, _, _, _, _ ->
                    if (source.matches(Regex(inputFilterPattern))) source else ""
                })
            }
            if (inputFilterMaxLength != null) {
                filters.add(InputFilter.LengthFilter(inputFilterMaxLength))
            }
            editText.filters = filters.toTypedArray()
        }
    }
}
