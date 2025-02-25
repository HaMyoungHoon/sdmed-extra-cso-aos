package sdmed.extra.cso.models.common

import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FDataModelClass

data class PageNumberModel(
    var isVisible: MutableStateFlow<Boolean> = MutableStateFlow(true),
    var isSelect: MutableStateFlow<Boolean> = MutableStateFlow(false),
    var pageNumber: Int = 0,
): FDataModelClass<PageNumberModel.ClickEvent>() {
    val pageNumberString: String
        get() = "$pageNumber"
    fun initThis(): PageNumberModel {
        isVisible.value = false
        isSelect.value = false
        return this
    }
    fun visibleThis(): PageNumberModel {
        isVisible.value = true
        isSelect.value = false
        return this
    }
    fun selectThis(): PageNumberModel {
        isVisible.value = true
        isSelect.value = true
        return this
    }
    fun unSelectThis(): PageNumberModel {
        isVisible.value = true
        isSelect.value = false
        return this
    }
    fun tinyThis(): PageNumberModel {

        return this
    }
    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}