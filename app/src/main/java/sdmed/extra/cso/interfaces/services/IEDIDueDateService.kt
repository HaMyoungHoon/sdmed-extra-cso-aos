package sdmed.extra.cso.interfaces.services

import retrofit2.http.GET
import retrofit2.http.Query
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaDueDateModel

interface IEDIDueDateService {
    @GET("${FConstants.REST_API_EDI_DUE_DATE}/list")
    suspend fun getList(@Query("date") date: String, @Query("isYear") isYear: Boolean = false): RestResultT<List<EDIPharmaDueDateModel>>
    @GET("${FConstants.REST_API_EDI_DUE_DATE}/list/range")
    suspend fun getListRange(@Query("startDate") startDate: String, @Query("endDate") endDate: String): RestResultT<List<EDIPharmaDueDateModel>>
}