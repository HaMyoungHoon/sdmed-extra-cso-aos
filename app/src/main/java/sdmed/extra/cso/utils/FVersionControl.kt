package sdmed.extra.cso.utils

import sdmed.extra.cso.models.retrofit.common.VersionCheckModel
import sdmed.extra.cso.models.retrofit.common.VersionModel

object FVersionControl {
    enum class VersionResultType(var index: Int) {
        LATEST(0),
        UPDATABLE(1),
        NEED_UPDATE(2)
    }
    fun checkVersion(versionModel: VersionCheckModel, currentVersion: String): VersionResultType {
        if (versionModel.latestVersion == currentVersion) {
            return VersionResultType.LATEST
        }

        val current = VersionModel().setVersionString(currentVersion)
        val minor = VersionModel().setVersionString(versionModel.minorVersion)
        if (current >= minor) {
            return VersionResultType.UPDATABLE
        }

        return VersionResultType.NEED_UPDATE
    }
}