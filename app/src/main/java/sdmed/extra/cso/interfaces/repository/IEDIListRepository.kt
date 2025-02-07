package sdmed.extra.cso.interfaces.repository

import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.edi.EDIUploadFileModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel

interface IEDIListRepository {
    suspend fun getList(startDate: String, endDate: String): RestResultT<List<EDIUploadModel>>
    suspend fun getData(thisPK: String): RestResultT<EDIUploadModel>
    suspend fun postFile(thisPK: String, ediUploadFileModel: List<EDIUploadFileModel>): RestResultT<List<EDIUploadFileModel>>
    suspend fun deleteEDIFile(thisPK: String): RestResultT<EDIUploadFileModel>
}