package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IEDIListRepository
import sdmed.extra.cso.interfaces.services.IEDIListService
import sdmed.extra.cso.models.retrofit.edi.EDIUploadFileModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaFileModel
import sdmed.extra.cso.utils.FExtensions

class EDIListRepository(private val _service: IEDIListService): IEDIListRepository {
    override suspend fun getList(startDate: String, endDate: String) = FExtensions.restTryT { _service.getList(startDate, endDate) }
    override suspend fun getData(thisPK: String) = FExtensions.restTryT { _service.getData(thisPK) }
    override suspend fun postFile(thisPK: String, ediUploadFileModel: List<EDIUploadFileModel>) = FExtensions.restTryT { _service.postFile(thisPK, ediUploadFileModel) }
    override suspend fun postPharmaFile(thisPK: String, ediPharmaPK: String, ediUploadPharmaFileModel: List<EDIUploadPharmaFileModel>) =
        FExtensions.restTryT { _service.postPharmaFile(thisPK, ediPharmaPK, ediUploadPharmaFileModel) }
    override suspend fun deleteEDIFile(thisPK: String) = FExtensions.restTryT { _service.deleteEDIFile(thisPK) }
}