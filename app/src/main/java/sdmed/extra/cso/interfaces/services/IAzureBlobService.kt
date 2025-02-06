package sdmed.extra.cso.interfaces.services

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Url

interface IAzureBlobService {
    @PUT
    suspend fun upload(@Url sasKey: String, @Body file: MultipartBody.Part, @Header("x-ms-blob-type") blobType: String = "BlockBlob"): Response<ResponseBody>
}