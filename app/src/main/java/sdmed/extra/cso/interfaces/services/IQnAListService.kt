package sdmed.extra.cso.interfaces.services

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.qna.QnAContentModel
import sdmed.extra.cso.models.retrofit.qna.QnAHeaderModel
import sdmed.extra.cso.models.retrofit.qna.QnAReplyModel

interface IQnAListService {
    @GET("${FConstants.REST_API_QNA_LIST}/list")
    suspend fun getList(): RestResultT<List<QnAHeaderModel>>
    @GET("${FConstants.REST_API_QNA_LIST}/data/header/{thisPK}")
    suspend fun getHeaderData(@Path("thisPK") thisPK: String): RestResultT<QnAHeaderModel>
    @GET("${FConstants.REST_API_QNA_LIST}/data/content/{thisPK}")
    suspend fun getContentData(@Path("thisPK") thisPK: String): RestResultT<QnAContentModel>
    @POST("${FConstants.REST_API_QNA_LIST}/data")
    suspend fun postData(@Query("title") title: String, @Body qnaContentModel: QnAContentModel): RestResultT<QnAHeaderModel>
    @POST("${FConstants.REST_API_QNA_LIST}/data/{thisPK}")
    suspend fun postReply(@Path("thisPK") thisPK: String, @Body qnaReplyModel: QnAReplyModel): RestResultT<QnAReplyModel>
    @PUT("${FConstants.REST_API_QNA_LIST}/data/{thisPK}")
    suspend fun putData(@Path("thisPK") thisPK: String): RestResultT<QnAHeaderModel>
}