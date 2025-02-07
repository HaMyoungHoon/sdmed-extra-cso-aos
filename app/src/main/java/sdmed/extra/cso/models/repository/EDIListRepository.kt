package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IEDIListRepository
import sdmed.extra.cso.interfaces.services.IEDIListService
import sdmed.extra.cso.models.retrofit.edi.EDIUploadFileModel
import sdmed.extra.cso.utils.FExtensions

class EDIListRepository(private val _service: IEDIListService): IEDIListRepository {
    override suspend fun getList(startDate: String, endDate: String) = FExtensions.restTryT { _service.getList(startDate, endDate) }
    override suspend fun getData(thisPK: String) = FExtensions.restTryT { _service.getData(thisPK) }
    override suspend fun postFile(thisPK: String, ediUploadFileModel: List<EDIUploadFileModel>) = FExtensions.restTryT { _service.postFile(thisPK, ediUploadFileModel) }
    override suspend fun deleteEDIFile(thisPK: String) = FExtensions.restTryT { _service.deleteEDIFile(thisPK) }
}