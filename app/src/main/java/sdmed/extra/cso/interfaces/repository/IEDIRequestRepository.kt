package sdmed.extra.cso.interfaces.repository

import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.edi.EDIApplyDateModel
import sdmed.extra.cso.models.retrofit.edi.EDIHosBuffModel
import sdmed.extra.cso.models.retrofit.edi.EDIMedicineBuffModel
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaBuffModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel

interface IEDIRequestRepository {
    suspend fun getApplyDateList(): RestResultT<List<EDIApplyDateModel>>
    suspend fun getHospitalList(applyDate: String): RestResultT<List<EDIHosBuffModel>>
    suspend fun getPharmaList(): RestResultT<List<EDIPharmaBuffModel>>
    suspend fun getPharmaList(hosPK: String, applyDate: String): RestResultT<List<EDIPharmaBuffModel>>
    suspend fun getMedicineList(hosPK: String, pharmaPK: List<String>): RestResultT<List<EDIMedicineBuffModel>>
    suspend fun postData(ediUploadModel: EDIUploadModel): RestResultT<EDIUploadModel>
    suspend fun postNewData(ediUploadModel: EDIUploadModel): RestResultT<EDIUploadModel>
}