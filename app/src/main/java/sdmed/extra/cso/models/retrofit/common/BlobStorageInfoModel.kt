package sdmed.extra.cso.models.retrofit.common

data class BlobStorageInfoModel(
    var blobUrl: String = "",
    var blobContainerName: String = "",
    var sasKey: String = ""
) {
}