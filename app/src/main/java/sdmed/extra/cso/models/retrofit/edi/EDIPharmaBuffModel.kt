package sdmed.extra.cso.models.retrofit.edi

import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FDataModelClass

data class EDIPharmaBuffModel(
    var thisPK: String = "",
    var hosPK: String = "",
    var code: String = "",
    var orgName: String = "",
    var innerName: String = "",
): FDataModelClass<EDIPharmaBuffModel.ClickEvent>() {
    var medicineList: MutableList<EDIMedicineBuffModel> = mutableListOf()
    val isSelect = MutableStateFlow(false)
    fun lhsFromRhs(rhs: EDIPharmaBuffModel): EDIPharmaBuffModel {
        this.thisPK = rhs.thisPK
        this.hosPK = rhs.hosPK
        this.code = rhs.code
        this.orgName = rhs.orgName
        this.innerName = rhs.innerName
        this.medicineList = rhs.medicineList
        this.isSelect.value = rhs.isSelect.value
        return this
    }

    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}