package sdmed.extra.cso.interfaces.services

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.common.BlobUploadModel
import sdmed.extra.cso.models.retrofit.common.UserFileType
import sdmed.extra.cso.models.retrofit.users.UserDataModel
import sdmed.extra.cso.models.retrofit.users.UserFileModel

interface IMyInfoService {
    @GET("${FConstants.REST_API_MY_INFO}/data")
    suspend fun getData(@Query("relationView") relationView: Boolean = true): RestResultT<UserDataModel>
    @PUT("${FConstants.REST_API_MY_INFO}/passwordChange")
    suspend fun putPasswordChange(@Query("currentPW") currentPW: String, @Query("afterPW") afterPW: String, @Query("confirmPW") confirmPW: String): RestResultT<UserDataModel>
    @PUT("${FConstants.REST_API_MY_INFO}/file/{thisPK}")
    suspend fun putUserFileImageUrl(@Path("thisPK") thisPK: String, @Body blobModel: BlobUploadModel, @Query("userFileType") userFileType: UserFileType): RestResultT<UserFileModel>
}