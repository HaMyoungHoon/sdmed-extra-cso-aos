package sdmed.extra.cso.models.retrofit.users

import java.util.Date

data class UserFileModel(
    var thisPK: String = "",
    var userPK: String = "",
    var blobUrl: String = "",
    var originalFilename: String = "",
    var mimeType: String = "",
    var regDate: Date = Date(),
    var userFileType: UserFileType = UserFileType.Taxpayer
) {
}