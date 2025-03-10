package sdmed.extra.cso.models.retrofit.edi

import android.content.Context
import androidx.annotation.StringRes
import sdmed.extra.cso.R

enum class EDIType(var index: Int, @StringRes var descResId: Int) {
    DEFAULT(0, R.string.edi_type_default),
    NEW(1, R.string.edi_type_new),
    TRANSFER(2, R.string.edi_type_transfer);
    fun getDesc(context: Context): String {
        return context.getString(descResId)
    }
    companion object {
        fun allEDITypeList() = mutableListOf<EDIType>(NEW, TRANSFER)
        fun allEDITypeDescList() = mutableListOf<Int>(NEW.descResId, TRANSFER.descResId)
    }
}