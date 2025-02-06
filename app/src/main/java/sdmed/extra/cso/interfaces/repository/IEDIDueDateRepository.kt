package sdmed.extra.cso.interfaces.repository

import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaDueDateModel
import java.util.Date

interface IEDIDueDateRepository {
    suspend fun getList(date: Date, isYear: Boolean = false): RestResultT<List<EDIPharmaDueDateModel>>
    suspend fun getListRange(startDate: Date, endDate: Date): RestResultT<List<EDIPharmaDueDateModel>>
}