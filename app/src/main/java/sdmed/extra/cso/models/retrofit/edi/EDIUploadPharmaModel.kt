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
import okhttp3.Dispatcher
import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.models.common.MediaPickerSourceModel

data class EDIUploadPharmaModel(
    var thisPK: String = "",
    var ediPK: String = "",
    var pharmaPK: String = "",
    var orgName: String = "",
    var year: String = "",
    var month: String = "",
    var day: String = "",
    var isCarriedOver: Boolean = false,
    var ediState: EDIState = EDIState.None,
    var medicineList: MutableList<EDIUploadPharmaMedicineModel> = mutableListOf(),
    var fileList: MutableList<EDIUploadPharmaFileModel> = mutableListOf(),
): FDataModelClass<EDIUploadPharmaModel.ClickEvent>() {
    @Transient
    @JsonIgnore
    var uploadItems: MutableStateFlow<MutableList<MediaPickerSourceModel>> = MutableStateFlow(mutableListOf())
    val isAddable get() = ediState.isEditable()
    val isOpen = MutableStateFlow(false)
    val isSavable: StateFlow<Boolean> = uploadItems.map { it.isNotEmpty() }
        .stateIn(CoroutineScope(Dispatchers.Main + SupervisorJob()), SharingStarted.Lazily, false)
    val currentPosition = MutableStateFlow<Int>(1)
    val positionString: StateFlow<String> = currentPosition.map { "${it}/${fileList.size}" }
        .stateIn(CoroutineScope(Dispatchers.Main + SupervisorJob()), SharingStarted.Lazily, "${currentPosition.value}/${fileList.size}")

    fun getYearMonth() = "${year}-${month}"
    fun getEdiColor() = ediState.parseEDIColor()
    fun getEdiBackColor() = ediState.parseEDIBackColor()
    enum class ClickEvent(var index: Int) {
        OPEN(0),
        ADD(1),
        SAVE(2),
    }
}