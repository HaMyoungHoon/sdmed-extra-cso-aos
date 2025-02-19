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
    var name: String = "",
    var ediState: EDIState = EDIState.None,
    var regDate: String = "",
    var etc: String = "",
    var pharmaList: MutableList<EDIUploadPharmaModel> = arrayListOf(),
    var fileList: MutableList<EDIUploadFileModel> = arrayListOf(),
    var responseList: MutableList<EDIUploadResponseModel> = arrayListOf()
): FDataModelClass<EDIUploadModel.ClickEvent>() {

    fun getYearMonth() = "${year}-$month"
    fun getRegDateString() = FDateTime().setThis(regDate).toString("yyyy-MM")
    fun getEdiColor() = ediState.parseEDIColor()
    enum class ClickEvent(var index: Int) {
        OPEN(0)
    }
}