package sdmed.extra.cso.models.retrofit.edi

import sdmed.extra.cso.bases.FDataModelClass

data class EDIUploadFileModel(
    var thisPK: String = "",
    var ediPK: String = "",
    var blobUrl: String = "",
    var sasKey: String = "",
    var blobName: String = "",
    var originalFilename: String = "",
    var mimeType: String = "",
    var regDate: String = "",
    var inVisible: Boolean = false
): FDataModelClass<EDIUploadFileModel.ClickEvent>() {
    fun copy(rhs: EDIUploadFileModel): EDIUploadFileModel {
        this.thisPK = rhs.thisPK
        this.ediPK = rhs.ediPK
        this.blobUrl = rhs.blobUrl
        this.blobName = rhs.blobName
        this.originalFilename = rhs.originalFilename
        this.mimeType = rhs.mimeType
        this.regDate = rhs.regDate
        this.inVisible = rhs.inVisible
        return this
    }
    enum class ClickEvent(var index: Int) {
        THIS(0)
    }

    fun blobUrlKey(): String {
        return "${blobUrl}?${sasKey}"
    }
}