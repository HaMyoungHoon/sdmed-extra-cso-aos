package sdmed.extra.cso.models.retrofit.edi

import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FDataModelClass

data class EDIHosBuffModel(
    var thisPK: String = "",
    var orgName: String = "",
    var pharmaList: MutableList<EDIPharmaBuffModel> = mutableListOf()
): FDataModelClass<EDIHosBuffModel.ClickEvent>() {
    val isSelect = MutableStateFlow(false)

    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}