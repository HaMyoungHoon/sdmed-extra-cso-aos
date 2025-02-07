package sdmed.extra.cso.interfaces.repository

import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaDueDateModel

interface IEDIDueDateRepository {
    suspend fun getList(date: String, isYear: Boolean = false): RestResultT<List<EDIPharmaDueDateModel>>
    suspend fun getListRange(startDate: String, endDate: String): RestResultT<List<EDIPharmaDueDateModel>>
}