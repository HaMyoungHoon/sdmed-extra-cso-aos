package sdmed.extra.cso.models.common

import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FDataModelClass

data class EllipseItemModel(
    var showItem: MutableStateFlow<Boolean> = MutableStateFlow(true),
    var isSelect: MutableStateFlow<Boolean> = MutableStateFlow(false),
    var isTiny: MutableStateFlow<Boolean> = MutableStateFlow(false),
): FDataModelClass<EllipseItemModel.ClickEvent>() {
    fun selectThis() {
        showItem.value = true
        isSelect.value = true
        isTiny.value = false
    }
    fun visibleThis() {
        showItem.value = true
        isSelect.value = false
        isTiny.value = false
    }
    fun tinyThis() {
        showItem.value = true
        isSelect.value = false
        isTiny.value = true
    }
    fun initThis() {
        showItem.value = false
        isSelect.value = false
        isTiny.value = true
    }

    enum class ClickEvent(var index: Int) {

    }
}