package sdmed.extra.cso.models.retrofit.common

// 버전은 1.1.1 // major / minor / patch 세 개임.
data class VersionModel(
    var major: Int = 0,
    var minor: Int = 0,
    var patch: Int = 0
) {
    fun getVersionString() = "${major}.${minor}.${patch}"
    fun setVersionString(versionString: String): VersionModel {
        val buff = versionString.split(".")
        if (buff.isEmpty()) {
            major = 0
            minor = 0
            patch = 0
            return this
        }
        if (buff.count() <= 1) {
            major = buff[0].toIntOrNull() ?: 0
            minor = 0
            patch = 0
            return this
        }
        if (buff.count() <= 2) {
            major = buff[0].toIntOrNull() ?: 0
            minor = buff[1].toIntOrNull() ?: 0
            patch = 0
            return this
        }
        major = buff[0].toIntOrNull() ?: 0
        minor = buff[1].toIntOrNull() ?: 0
        patch = buff[2].toIntOrNull() ?: 0
        return this
    }

    operator fun compareTo(rhs: VersionModel): Int {
        if (major > rhs.major) return 1
        if (major < rhs.major) return -1
        if (minor > rhs.minor) return 1
        if (minor < rhs.minor) return -1
        if (patch > rhs.patch) return 1
        if (patch < rhs.patch) return -1
        return 0
    }
}