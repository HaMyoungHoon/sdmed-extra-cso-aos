package sdmed.extra.cso.models.retrofit.common

data class BlobUploadModel(
    var thisPK: String = "",
    var blobUrl: String = "",
    var uploaderPK: String = "",
    var originalFilename: String = "",
    var mimeType: String = "",
    var regDate: String = ""
) {
}