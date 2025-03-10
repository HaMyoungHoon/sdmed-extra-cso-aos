package sdmed.extra.cso.models.retrofit.edi

import sdmed.extra.cso.bases.FDataModelClass

data class EDIUploadPharmaFileModel(
    var thisPK: String = "",
    var ediPharmaPK: String = "",
    var pharmaPK: String = "",
    var blobUrl: String = "",
    var sasKey: String = "",
    var blobName: String = "",
    var originalFilename: String = "",
    var mimeType: String = "",
    var regDate: String = "",
): FDataModelClass<EDIUploadPharmaFileModel.ClickEvent>() {
    fun copy(rhs: EDIUploadPharmaFileModel): EDIUploadPharmaFileModel {
        this.thisPK = rhs.thisPK
        this.ediPharmaPK = rhs.ediPharmaPK
        this.pharmaPK = rhs.pharmaPK
        this.blobUrl = rhs.blobUrl
        this.sasKey = rhs.sasKey
        this.blobName = rhs.blobName
        this.originalFilename = rhs.originalFilename
        this.mimeType = rhs.mimeType
        this.regDate = rhs.regDate
        return this
    }
    enum class ClickEvent(var index: Int) {
        LONG(0),
        SHORT(1)
    }

    fun blobUrlKey(): String {
        return "${blobUrl}?${sasKey}"
    }
}