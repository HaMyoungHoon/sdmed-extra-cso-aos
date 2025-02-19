package sdmed.extra.cso.models.common

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import sdmed.extra.cso.bases.FDataModelClass

data class SelectListModel(
    var itemIndex: Int = 0,
    @DrawableRes var iconResId: Int = 0,
    @StringRes var stringResId: Int = 0,
    @ColorRes var colorResId: Int = 0,
    var data: Any? = null
): FDataModelClass<SelectListModel.ClickEvent>() {

    enum class ClickEvent(var index: Int) {
        SELECT(0)
    }
}