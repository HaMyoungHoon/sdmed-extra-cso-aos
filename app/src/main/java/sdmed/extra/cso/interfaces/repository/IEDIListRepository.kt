package sdmed.extra.cso.interfaces.repository

import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.edi.EDIUploadFileModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import java.util.Date

interface IEDIListRepository {
    suspend fun getList(startDate: Date, endDate: Date): RestResultT<List<EDIUploadModel>>
    suspend fun getData(thisPK: String): RestResultT<EDIUploadModel>
    suspend fun postFile(thisPK: String, ediUploadFileModel: List<EDIUploadFileModel>): RestResultT<List<EDIUploadFileModel>>
    suspend fun deleteEDIFile(thisPK: String): RestResultT<EDIUploadFileModel>
}