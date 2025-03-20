package sdmed.extra.cso.models.common

import android.content.Context
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FContentsType
import sdmed.extra.cso.utils.FExtensions
import java.util.UUID

data class UserTrainingFileSASKeyQueueModel(
    var media: MediaPickerSourceModel = MediaPickerSourceModel(),
    var trainingDate: String = ""
) {
    fun blobName(context: Context): Pair<String, String> {
        val id = FAmhohwa.getUserID(context)
        if (media.mediaMimeType.isEmpty()) {
            media.mediaMimeType = FContentsType.getExtMimeTypeString(media.mediaName)
        }
        val ext = FContentsType.getExtMimeType(media.mediaMimeType)
        return if (ext == "application/octet-stream") {
            Pair(media.mediaName, "user/${id}/${FExtensions.getToday().toString("yyyyMMdd")}/${UUID.randomUUID()}.${media.mediaName}")
        } else {
            Pair(media.mediaName, "user/${id}/${FExtensions.getToday().toString("yyyyMMdd")}/${UUID.randomUUID()}.${ext}")
        }
    }
}