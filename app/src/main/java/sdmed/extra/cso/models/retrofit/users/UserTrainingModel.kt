package sdmed.extra.cso.models.retrofit.users

import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.fDate.FDateTime
import java.util.Date

data class UserTrainingModel(
    var thisPK: String = "",
    var userPK: String = "",
    var blobUrl: String = "",
    var originalFilename: String = "",
    var mimeType: String = "",
    var trainingDate: Date = Date(),
    var regDate: Date = Date()
): FDataModelClass<UserTrainingModel.ClickEvent>() {

    val trainingDateString get() = FDateTime().setThis(trainingDate.time).toString("yyyy-MM-dd")
    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}