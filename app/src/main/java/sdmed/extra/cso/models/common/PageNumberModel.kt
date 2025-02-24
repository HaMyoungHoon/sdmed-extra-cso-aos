package sdmed.extra.cso.models.common

import sdmed.extra.cso.bases.FDataModelClass

data class PageNumberModel(
    var isVisible: Boolean = true,
    var isSelect: Boolean = false,
    var pageNumber: Int = 0,
): FDataModelClass<PageNumberModel.ClickEvent>() {
    val pageNumberString: String
        get() = "$pageNumber"
    fun initThis(): PageNumberModel {
        isVisible = false
        isSelect = false
        return this
    }
    fun visibleThis(): PageNumberModel {
        isVisible = true
        isSelect = false
        return this
    }
    fun selectThis(): PageNumberModel {
        isVisible = true
        isSelect = true
        return this
    }
    fun unSelectThis(): PageNumberModel {
        isVisible = true
        isSelect = false
        return this
    }
    fun tinyThis(): PageNumberModel {

        return this
    }
    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}