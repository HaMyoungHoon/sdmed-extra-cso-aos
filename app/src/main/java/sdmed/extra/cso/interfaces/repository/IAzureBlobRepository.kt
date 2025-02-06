package sdmed.extra.cso.interfaces.repository

import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

interface IAzureBlobRepository {
    suspend fun upload(sasUrl: String, file: File, mimeType: String): Response<ResponseBody>
    suspend fun upload(sasUrl: String, file: File, mimeType: MediaType): Response<ResponseBody>
}