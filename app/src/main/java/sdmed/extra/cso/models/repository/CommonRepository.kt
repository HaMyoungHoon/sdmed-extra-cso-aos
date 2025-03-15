package sdmed.extra.cso.models.repository

import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.interfaces.repository.ICommonRepository
import sdmed.extra.cso.interfaces.services.ICommonService
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.common.BlobStorageInfoModel
import sdmed.extra.cso.models.retrofit.common.VersionCheckType

class CommonRepository(private val _service: ICommonService): ICommonRepository {
    override suspend fun signIn(id: String, pw: String) = FExtensions.restTryT { _service.signIn(id, pw) }
    override suspend fun multiSign(token: String) = FExtensions.restTryT { _service.multiSign(token) }
    override suspend fun tokenRefresh() = FExtensions.restTryT { _service.tokenRefresh() }

    override suspend fun getFindIDAuthNumber(name: String, phoneNumber: String) = FExtensions.restTry { _service.getFindIDAuthNumber(name, phoneNumber) }
    override suspend fun getFindPWAuthNumber(id: String, phoneNumber: String) = FExtensions.restTry { _service.getFindPWAuthNumber(id, phoneNumber) }
    override suspend fun getCheckAuthNumber(authNumber: String, phoneNumber: String) = FExtensions.restTryT { _service.getCheckAuthNumber(authNumber, phoneNumber) }

    override suspend fun versionCheck(versionCheckType: VersionCheckType) = FExtensions.restTryT { _service.versionCheck(versionCheckType) }
    override suspend fun serverTime() = FExtensions.restTryT { _service.serverTime() }
    override suspend fun setLanguage(lang: String) = FExtensions.restTry { _service.setLanguage(lang) }

    override suspend fun getMyRole() = FExtensions.restTryT { _service.getMyRole() }
    override suspend fun getMyState() = FExtensions.restTryT { _service.getMyState() }
    override suspend fun getGenerateSas(blobName: String) = FExtensions.restTryT { _service.getGenerateSas(blobName) }
    override suspend fun postGenerateSasList(blobName: List<String>) = FExtensions.restTryT { _service.postGenerateSasList(blobName) }
    override suspend fun downloadFile(url: String) =
        try {
            _service.downloadFile(url)
        } catch (e: Exception) {
            null
        }
}