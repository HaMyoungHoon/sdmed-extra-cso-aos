package sdmed.extra.cso.models.retrofit.qna

import sdmed.extra.cso.bases.FDataModelClass

data class QnAFileModel(
    var thisPK: String = "",
    var headerPK: String = "",
    var blobUrl: String = "",
    var originalFilename: String = "",
    var mimeType: String = "",
    var regDate: String = ""
): FDataModelClass<QnAFileModel.ClickEvent>() {
    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}