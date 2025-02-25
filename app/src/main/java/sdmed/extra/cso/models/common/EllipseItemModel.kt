package sdmed.extra.cso.models.common

import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FDataModelClass

data class EllipseItemModel(
    var isVisible: Boolean = false,
    var isSelect: Boolean = false,
    var isTiny: Boolean = false,
): FDataModelClass<EllipseItemModel.ClickEvent>() {
    fun selectThis() {
        isVisible = true
        isSelect = true
        isTiny = false
    }
    fun visibleThis() {
        isVisible = true
        isSelect = false
        isTiny = false
    }
    fun tinyThis() {
        isVisible = true
        isSelect = false
        isTiny = true
    }
    fun initThis() {
        isVisible = false
        isSelect = false
        isTiny = false
    }
    fun getWidthSize() = if (isTiny) 3 else 5
    fun getHeightSize() = if (isTiny) 3 else 5
    fun getThisSrc() = if (isSelect) R.drawable.vector_ellipse_primary else R.drawable.vector_ellipse_disable

    enum class ClickEvent(var index: Int) {

    }
}