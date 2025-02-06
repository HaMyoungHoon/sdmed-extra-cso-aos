package sdmed.extra.cso.models.retrofit.edi

import java.util.Date

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
    var regDate: Date = Date(),
    var etc: String = "",
    var pharmaList: MutableList<EDIUploadPharmaModel> = arrayListOf(),
    var fileList: MutableList<EDIUploadFileModel> = arrayListOf(),
    var responseList: MutableList<EDIUploadResponseModel> = arrayListOf()
) {
}