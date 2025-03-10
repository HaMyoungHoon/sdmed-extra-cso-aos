package sdmed.extra.cso.interfaces.services

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.edi.EDIUploadFileModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaFileModel

interface IEDIListService {
    @GET("${FConstants.REST_API_EDI_LIST}/list")
    suspend fun getList(@Query("startDate") startDate: String, @Query("endDate") endDate: String): RestResultT<List<EDIUploadModel>>
    @GET("${FConstants.REST_API_EDI_LIST}/data/{thisPK}")
    suspend fun getData(@Path("thisPK") thisPK: String): RestResultT<EDIUploadModel>
    @POST("${FConstants.REST_API_EDI_LIST}/file/{thisPK}")
    suspend fun postFile(@Path("thisPK") thisPK: String, @Body ediUploadFileModel: List<EDIUploadFileModel>): RestResultT<List<EDIUploadFileModel>>
    @POST("${FConstants.REST_API_EDI_LIST}/file/{thisPK}/pharma/{ediPharmaPK}")
    suspend fun postPharmaFile(@Path("thisPK") thisPK: String, @Path("ediPharmaPK") ediPharmaPK: String, @Body ediUploadPharmaFileModel: List<EDIUploadPharmaFileModel>): RestResultT<List<EDIUploadPharmaFileModel>>
    @DELETE("${FConstants.REST_API_EDI_LIST}/data/file/{thisPK}")
    suspend fun deleteEDIFile(@Path("thisPK") thisPK: String): RestResultT<EDIUploadFileModel>
}