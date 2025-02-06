package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IMyInfoRepository
import sdmed.extra.cso.interfaces.services.IMyInfoService
import sdmed.extra.cso.models.retrofit.common.BlobUploadModel
import sdmed.extra.cso.models.retrofit.common.UserFileType
import sdmed.extra.cso.utils.FExtensions

class MyInfoRepository(private val _service: IMyInfoService): IMyInfoRepository {
    override suspend fun getData(relationView: Boolean) = FExtensions.restTryT { _service.getData(relationView) }
    override suspend fun putPasswordChange(currentPW: String, afterPW: String, confirmPW: String) = FExtensions.restTryT { _service.putPasswordChange(currentPW, afterPW, confirmPW) }
    override suspend fun putUserFileImageUrl(blobModel: BlobUploadModel, userFileType: UserFileType, thisPK: String) = FExtensions.restTryT { _service.putUserFileImageUrl(blobModel, userFileType, thisPK) }
}