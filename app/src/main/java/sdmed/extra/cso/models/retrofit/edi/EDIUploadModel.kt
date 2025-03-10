package sdmed.extra.cso.models.retrofit.edi

import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.fDate.FDateTime

data class EDIUploadModel(
    var thisPK: String = "",
    var userPK: String = "",
    var year: String = "",
    var month: String = "",
    var day: String = "",
    var hospitalPK: String = "",
    var orgName: String = "",
    var tempHospitalPK: String = "",
    var tempOrgName: String = "",
    var name: String = "",
    var ediState: EDIState = EDIState.None,
    var ediType: EDIType = EDIType.DEFAULT,
    var regDate: String = "",
    var etc: String = "",
    var pharmaList: MutableList<EDIUploadPharmaModel> = arrayListOf(),
    var fileList: MutableList<EDIUploadFileModel> = arrayListOf(),
    var responseList: MutableList<EDIUploadResponseModel> = arrayListOf()
): FDataModelClass<EDIUploadModel.ClickEvent>() {
    val orgViewName get() = if (ediType == EDIType.DEFAULT) orgName else tempOrgName
    val tempOrgString get() = "(${tempOrgName})"
    val isDefault get() = ediType == EDIType.DEFAULT
    val isNew get() = ediType == EDIType.NEW
    val isTransfer get() = ediType == EDIType.TRANSFER
    fun getYearMonth() = "${year}-$month"
    fun getRegDateString() = FDateTime().setThis(regDate).toString("yyyy-MM")
    fun getEdiColor() = ediState.parseEDIColor()
    fun getEdiBackColor() = ediState.parseEDIBackColor()
    enum class ClickEvent(var index: Int) {
        OPEN(0)
    }
}