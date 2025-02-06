package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IEDIRequestRepository
import sdmed.extra.cso.interfaces.services.IEDIRequestService
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.utils.FExtensions

class EDIRequestRepository(private val _service: IEDIRequestService): IEDIRequestRepository {
    override suspend fun getApplyDateList() = FExtensions.restTryT { _service.getApplyDateList() }
    override suspend fun getHospitalList(applyDate: String) = FExtensions.restTryT { _service.getHospitalList(applyDate) }
    override suspend fun getPharmaList(hosPK: String, applyDate: String) = FExtensions.restTryT { _service.getPharmaList(hosPK, applyDate) }
    override suspend fun getMedicineList(hosPK: String, pharmaPK: List<String>) = FExtensions.restTryT { _service.getMedicineList(hosPK, pharmaPK) }
    override suspend fun postData(ediUploadModel: EDIUploadModel) = FExtensions.restTryT { _service.postData(ediUploadModel) }
    override suspend fun postNewData(ediUploadModel: EDIUploadModel) = FExtensions.restTryT { _service.postNewData(ediUploadModel) }
}