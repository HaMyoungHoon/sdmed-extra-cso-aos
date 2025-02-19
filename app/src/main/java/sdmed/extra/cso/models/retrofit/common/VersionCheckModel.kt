package sdmed.extra.cso.models.retrofit.common

data class VersionCheckModel(
    var thisPK: String = "",
    var versionCheckType: VersionCheckType = VersionCheckType.AOS,
    var latestVersion: String = "",
    var minorVersion: String = "",
    var able: Boolean = false,
    var regDate: String = ""
) {
}