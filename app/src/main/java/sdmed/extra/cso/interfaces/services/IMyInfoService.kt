package sdmed.extra.cso.interfaces.services

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.common.BlobUploadModel
import sdmed.extra.cso.models.retrofit.users.UserFileType
import sdmed.extra.cso.models.retrofit.users.UserDataModel
import sdmed.extra.cso.models.retrofit.users.UserFileModel
import sdmed.extra.cso.models.retrofit.users.UserTrainingModel

interface IMyInfoService {
    @GET("${FConstants.REST_API_MY_INFO}/data")
    suspend fun getData(@Query("relationView") relationView: Boolean = true, @Query("trainingModelView") trainingModelView: Boolean = true): RestResultT<UserDataModel>
    @PUT("${FConstants.REST_API_MY_INFO}/passwordChange")
    suspend fun putPasswordChange(@Query("currentPW") currentPW: String, @Query("afterPW") afterPW: String, @Query("confirmPW") confirmPW: String): RestResultT<UserDataModel>
    @PUT("${FConstants.REST_API_MY_INFO}/file/{thisPK}")
    suspend fun putUserFileImageUrl(@Path("thisPK") thisPK: String, @Body blobModel: BlobUploadModel, @Query("userFileType") userFileType: UserFileType): RestResultT<UserFileModel>
    @POST("${FConstants.REST_API_MY_INFO}/file/training/{thisPK}")
    suspend fun postUserTrainingData(@Path("thisPK") thisPK: String, @Query("trainingDate") trainingDate: String, @Body blobModel: BlobUploadModel): RestResultT<UserTrainingModel>
}