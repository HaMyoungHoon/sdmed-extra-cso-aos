package sdmed.extra.cso.interfaces.services

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.RestResult
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.common.BlobStorageInfoModel
import sdmed.extra.cso.models.retrofit.common.VersionCheckModel
import sdmed.extra.cso.models.retrofit.common.VersionCheckType
import sdmed.extra.cso.models.retrofit.users.UserStatus
import java.util.Date

interface ICommonService {
    @GET("${FConstants.REST_API_COMMON}/signIn")
    suspend fun signIn(@Query("id") id: String, @Query("pw") pw: String): RestResultT<String>
    @POST("${FConstants.REST_API_COMMON}/tokenRefresh")
    suspend fun tokenRefresh(): RestResultT<String>

    @GET("${FConstants.REST_API_COMMON}/findIDAuthNumber")
    suspend fun getFindIDAuthNumber(@Query("name") name: String, @Query("phoneNumber") phoneNumber: String): RestResult
    @GET("${FConstants.REST_API_COMMON}/findPWAuthNumber")
    suspend fun getFindPWAuthNumber(@Query("id") id: String, @Query("phoneNumber") phoneNumber: String): RestResult
    @GET("${FConstants.REST_API_COMMON}/checkAuthNumber")
    suspend fun getCheckAuthNumber(@Query("authNumber") authNumber: String, @Query("phoneNumber") phoneNumber: String): RestResultT<String>

    @GET("${FConstants.REST_API_COMMON}/versionCheck")
    suspend fun versionCheck(@Query("versionCheckType") versionCheckType: VersionCheckType = VersionCheckType.AOS): RestResultT<List<VersionCheckModel>>
    @GET("${FConstants.REST_API_COMMON}/serverTime")
    suspend fun serverTime(): RestResultT<Date>
    @POST("${FConstants.REST_API_COMMON}/lang")
    suspend fun setLanguage(@Query("lang") lang: String): RestResult

    @GET("${FConstants.REST_API_COMMON}/myRole")
    suspend fun getMyRole(): RestResultT<Int>
    @GET("${FConstants.REST_API_COMMON}/myState")
    suspend fun getMyState(): RestResultT<UserStatus>
    @GET("${FConstants.REST_API_COMMON}/generate/sas")
    suspend fun getGenerateSas(@Query("blobName") blobName: String): RestResultT<BlobStorageInfoModel>
    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): Response<ResponseBody>
}