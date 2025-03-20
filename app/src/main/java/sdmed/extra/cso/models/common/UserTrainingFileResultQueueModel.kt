package sdmed.extra.cso.models.common

import sdmed.extra.cso.models.retrofit.common.BlobUploadModel

data class UserTrainingFileResultQueueModel(
    var uuid: String = "",
    var medias: BlobUploadModel = BlobUploadModel(),
    var trainingDate: String = ""
) {
    fun readyToSend() = trainingDate.isNotEmpty()
    fun setThis(data: UserTrainingFileResultQueueModel) {
        medias = data.medias
        trainingDate = data.trainingDate
    }
}