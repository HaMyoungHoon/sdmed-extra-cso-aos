package sdmed.extra.cso.models.retrofit.edi

import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FDataModelClass

data class EDIApplyDateModel(
    var thisPK: String = "",
    var year: String = "",
    var month: String = "",
    var userPK: String = "",
    var applyDateState: EDIApplyDateState = EDIApplyDateState.None
): FDataModelClass<EDIApplyDateModel.ClickEvent>() {
    val isSelect = MutableStateFlow(false)
    val yearMonth: String get() = "${year}-${month}"
    val yearMonthDay: String get() = "${year}-${month}-01"
    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}