package sdmed.extra.cso.interfaces.repository

import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.common.BlobUploadModel
import sdmed.extra.cso.models.retrofit.users.UserFileType
import sdmed.extra.cso.models.retrofit.users.UserDataModel
import sdmed.extra.cso.models.retrofit.users.UserFileModel
import sdmed.extra.cso.models.retrofit.users.UserTrainingModel

interface IMyInfoRepository {
    suspend fun getData(relationView: Boolean = true, trainingModelView: Boolean = true): RestResultT<UserDataModel>
    suspend fun putPasswordChange(currentPW: String, afterPW: String, confirmPW: String): RestResultT<UserDataModel>
    suspend fun putUserFileImageUrl(thisPK: String, blobModel: BlobUploadModel, userFileType: UserFileType): RestResultT<UserFileModel>
    suspend fun postUserTrainingData(thisPK: String, trainingDate: String, blobModel: BlobUploadModel): RestResultT<UserTrainingModel>
}