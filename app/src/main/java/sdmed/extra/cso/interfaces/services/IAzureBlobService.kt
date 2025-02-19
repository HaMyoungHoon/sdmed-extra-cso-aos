package sdmed.extra.cso.interfaces.services

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Url

interface IAzureBlobService {
    @Headers("x-ms-blob-type: BlockBlob")
    @PUT
    suspend fun upload(@Url sasKey: String, @Body file: RequestBody): Response<ResponseBody>
}