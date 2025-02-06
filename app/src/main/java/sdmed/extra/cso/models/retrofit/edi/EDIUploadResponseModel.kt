package sdmed.extra.cso.models.retrofit.edi

import java.util.Date

data class EDIUploadResponseModel(
    var thisPK: String = "",
    var ediPK: String = "",
    var pharmaPK: String = "",
    var pharmaName: String = "",
    var userPK: String = "",
    var userName: String = "",
    var etc: String = "",
    var ediState: EDIState = EDIState.None,
    var regDate: Date = Date(),
) {
}