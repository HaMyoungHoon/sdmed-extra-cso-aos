package sdmed.extra.cso.models.retrofit.common

data class BlobUploadModel(
    var thisPK: String = "",
    var blobUrl: String = "",
    var sasKey: String = "",
    var blobName: String = "",
    var uploaderPK: String = "",
    var originalFilename: String = "",
    var mimeType: String = "",
    var regDate: String = ""
) {
    fun copy(rhs: BlobUploadModel): BlobUploadModel {
        this.thisPK = rhs.thisPK
        this.blobUrl = rhs.blobUrl
        this.sasKey = rhs.sasKey
        this.blobName = rhs.blobName
        this.uploaderPK = rhs.uploaderPK
        this.originalFilename = rhs.originalFilename
        this.mimeType = rhs.mimeType
        this.regDate = rhs.regDate
        return this
    }
    fun blobUrlKey(): String {
        return "${blobUrl}?${sasKey}"
    }
}