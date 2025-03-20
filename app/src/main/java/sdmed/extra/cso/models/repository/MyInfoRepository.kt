package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IMyInfoRepository
import sdmed.extra.cso.interfaces.services.IMyInfoService
import sdmed.extra.cso.models.retrofit.common.BlobUploadModel
import sdmed.extra.cso.models.retrofit.users.UserFileType
import sdmed.extra.cso.utils.FExtensions

class MyInfoRepository(private val _service: IMyInfoService): IMyInfoRepository {
    override suspend fun getData(relationView: Boolean, trainingModelView: Boolean) = FExtensions.restTryT { _service.getData(relationView, trainingModelView) }
    override suspend fun putPasswordChange(currentPW: String, afterPW: String, confirmPW: String) = FExtensions.restTryT { _service.putPasswordChange(currentPW, afterPW, confirmPW) }
    override suspend fun putUserFileImageUrl(thisPK: String, blobModel: BlobUploadModel, userFileType: UserFileType) = FExtensions.restTryT { _service.putUserFileImageUrl(thisPK, blobModel, userFileType) }
    override suspend fun postUserTrainingData(thisPK: String, trainingDate: String, blobModel: BlobUploadModel) = FExtensions.restTryT { _service.postUserTrainingData(thisPK, trainingDate, blobModel) }
}