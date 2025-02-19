package sdmed.extra.cso.interfaces.repository

import okhttp3.ResponseBody
import retrofit2.Response
import sdmed.extra.cso.models.RestResult
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.common.BlobStorageInfoModel
import sdmed.extra.cso.models.retrofit.common.VersionCheckModel
import sdmed.extra.cso.models.retrofit.common.VersionCheckType
import sdmed.extra.cso.models.retrofit.users.UserStatus
import java.util.Date

interface ICommonRepository {
    suspend fun signIn(id: String, pw: String): RestResultT<String>
    suspend fun tokenRefresh(): RestResultT<String>

    suspend fun getFindIDAuthNumber(name: String, phoneNumber: String): RestResult
    suspend fun getFindPWAuthNumber(id: String, phoneNumber: String): RestResult
    suspend fun getCheckAuthNumber(authNumber: String, phoneNumber: String): RestResultT<String>

    suspend fun versionCheck(versionCheckType: VersionCheckType = VersionCheckType.AOS): RestResultT<List<VersionCheckModel>>
    suspend fun serverTime(): RestResultT<Date>
    suspend fun setLanguage(lang: String): RestResult

    suspend fun getMyRole(): RestResultT<Int>
    suspend fun getMyState(): RestResultT<UserStatus>
    suspend fun getGenerateSas(blobName: String): RestResultT<BlobStorageInfoModel>
    suspend fun postGenerateSasList(blobName: List<String>): RestResultT<List<BlobStorageInfoModel>>
    suspend fun downloadFile(url: String): Response<ResponseBody>?
}