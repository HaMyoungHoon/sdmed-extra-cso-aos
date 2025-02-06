package sdmed.extra.cso.interfaces.services

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.edi.EDIApplyDateModel
import sdmed.extra.cso.models.retrofit.edi.EDIHosBuffModel
import sdmed.extra.cso.models.retrofit.edi.EDIMedicineBuffModel
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaBuffModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel

interface IEDIRequestService {
    @GET("${FConstants.REST_API_EDI_REQUEST}/list/applyDate")
    suspend fun getApplyDateList(): RestResultT<List<EDIApplyDateModel>>
    @GET("${FConstants.REST_API_EDI_REQUEST}/list/hospital")
    suspend fun getHospitalList(@Query("applyDate") applyDate: String): RestResultT<List<EDIHosBuffModel>>
    @GET("${FConstants.REST_API_EDI_REQUEST}/list/pharma/{hosPK}")
    suspend fun getPharmaList(@Path("hosPK") hosPK: String, @Query("applyDate") applyDate: String): RestResultT<List<EDIPharmaBuffModel>>
    @GET("${FConstants.REST_API_EDI_REQUEST}/list/medicine/{hosPK}")
    suspend fun getMedicineList(@Path("hosPK") hosPK: String, @Query("pharmaPK") pharmaPK: List<String>): RestResultT<List<EDIMedicineBuffModel>>
    @POST("${FConstants.REST_API_EDI_REQUEST}/data")
    suspend fun postData(@Body ediUploadModel: EDIUploadModel): RestResultT<EDIUploadModel>
    @POST("${FConstants.REST_API_EDI_REQUEST}/data/new")
    suspend fun postNewData(@Body ediUploadModel: EDIUploadModel): RestResultT<EDIUploadModel>
}