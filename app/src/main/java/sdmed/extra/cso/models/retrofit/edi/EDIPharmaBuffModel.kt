package sdmed.extra.cso.models.retrofit.edi

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.models.common.MediaPickerSourceModel

data class EDIPharmaBuffModel(
    var thisPK: String = "",
    var hosPK: String = "",
    var code: String = "",
    var orgName: String = "",
    var innerName: String = "",
): FDataModelClass<EDIPharmaBuffModel.ClickEvent>() {
    @Transient
    @JsonIgnore
    var medicineList: MutableList<EDIMedicineBuffModel> = mutableListOf()
    @Transient
    @JsonIgnore
    var uploadItems: MutableStateFlow<MutableList<MediaPickerSourceModel>> = MutableStateFlow(mutableListOf())
    val isSelect = MutableStateFlow(false)
    val isOpen = MutableStateFlow(false)
    val uploadItemCount: StateFlow<String> = uploadItems.map { "(${it.size})" }
        .stateIn(CoroutineScope(Dispatchers.Main + SupervisorJob()), SharingStarted.Lazily, "(${uploadItems.value.size})")
    fun lhsFromRhs(rhs: EDIPharmaBuffModel): EDIPharmaBuffModel {
        this.thisPK = rhs.thisPK
        this.hosPK = rhs.hosPK
        this.code = rhs.code
        this.orgName = rhs.orgName
        this.innerName = rhs.innerName
        this.medicineList = rhs.medicineList
        this.isSelect.value = rhs.isSelect.value
        this.isOpen.value = rhs.isOpen.value
        return this
    }

    enum class ClickEvent(var index: Int) {
        THIS(0),
        OPEN(1),
        ADD(2)
    }
}