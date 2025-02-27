package sdmed.extra.cso.models.retrofit.qna

import sdmed.extra.cso.bases.FDataModelClass

data class QnAFileModel(
    var thisPK: String = "",
    var headerPK: String = "",
    var blobUrl: String = "",
    var sasKey: String = "",
    var blobName: String = "",
    var originalFilename: String = "",
    var mimeType: String = "",
    var regDate: String = ""
): FDataModelClass<QnAFileModel.ClickEvent>() {
    fun copy(rhs: QnAFileModel): QnAFileModel {
        this.thisPK = rhs.thisPK
        this.headerPK = rhs.headerPK
        this.blobUrl = rhs.blobUrl
        this.sasKey = rhs.sasKey
        this.blobName = rhs.blobName
        this.originalFilename = rhs.originalFilename
        this.mimeType = rhs.mimeType
        this.regDate = rhs.regDate
        return this
    }
    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
    fun blobUrlKey(): String {
        return "${blobUrl}?${sasKey}"
    }
}