package sdmed.extra.cso.models.retrofit.edi

import java.util.Date

data class EDIUploadFileModel(
    var thisPK: String = "",
    var ediPK: String = "",
    var blobUrl: String = "",
    var originalFilename: String = "",
    var mimeType: String = "",
    var regDate: Date = Date(),
    var inVisible: Boolean = false
) {
}