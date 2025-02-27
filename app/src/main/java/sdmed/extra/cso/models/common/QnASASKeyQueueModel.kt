package sdmed.extra.cso.models.common

import android.content.Context
import sdmed.extra.cso.models.retrofit.qna.QnAContentModel
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FContentsType
import sdmed.extra.cso.utils.FExtensions
import java.util.UUID

data class QnASASKeyQueueModel(
    var qnaPK: String = "",
    var medias: MutableList<MediaPickerSourceModel> = mutableListOf(),
    var title: String = "",
    var content: String = ""
) {
    fun blobName(context: Context): List<Pair<String, String>> {
        val ret = mutableListOf<Pair<String, String>>()
        val id = FAmhohwa.getUserID(context)
        medias.forEach { x ->
            if (x.mediaMimeType.isEmpty()) {
                x.mediaMimeType = FContentsType.getExtMimeTypeString(x.mediaName)
            }
            val ext = FContentsType.getExtMimeType(x.mediaMimeType)
            if (ext == "application/octet-stream") {
                ret.add(Pair(x.mediaName, "qna/${id}/${FExtensions.getToday().toString("yyyyMMdd")}/${UUID.randomUUID()}.${x.mediaName}"))
            } else {
                ret.add(Pair(x.mediaName, "qna/${id}/${FExtensions.getToday().toString("yyyyMMdd")}/${UUID.randomUUID()}.${ext}"))
            }
        }
        return ret
    }
}