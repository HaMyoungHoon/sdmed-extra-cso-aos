package sdmed.extra.cso.models.retrofit.common

import java.util.Date

data class BlobUploadModel(
    var thisPK: String = "",
    var blobUrl: String = "",
    var uploaderPK: String = "",
    var originalFilename: String = "",
    var mimeType: String = "",
    var regDate: Date = Date()
) {
}