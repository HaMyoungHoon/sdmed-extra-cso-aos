package sdmed.extra.cso.views.main.edi.ediView

import androidx.core.net.toUri
import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.generic.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IEDIListRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.common.EllipseItemModel
import sdmed.extra.cso.models.common.MediaFileType
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.EDISASKeyQueueModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadFileModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.models.services.FBackgroundEDIFileUpload

class EDIViewActivityVM(application: MultiDexApplication): FBaseViewModel(application) {
    private val ediListRepository: IEDIListRepository by kodein.instance(IEDIListRepository::class)
    private val backgroundService: FBackgroundEDIFileUpload by kodein.instance(FBackgroundEDIFileUpload::class)

    var thisPK: String = ""
    val item = MutableStateFlow(EDIUploadModel())
    val ellipseList = MutableStateFlow(mutableListOf<EllipseItemModel>())
    val uploadItems = MutableStateFlow(mutableListOf<MediaPickerSourceModel>())
    val isAddable = MutableStateFlow(false)
    val isSavable = MutableStateFlow(false)

    suspend fun getData(): RestResultT<EDIUploadModel> {
        val ret = ediListRepository.getData(thisPK)
        if (ret.result == true) {
            item.value = ret.data ?: EDIUploadModel()
            val ellipseBuff = mutableListOf<EllipseItemModel>()
            ret.data?.fileList?.forEach { ellipseBuff.add(EllipseItemModel()) }
            ellipseList.value = ellipseBuff
            uploadItems.value = mutableListOf()
            isAddable.value = item.value.ediState.isEditable()
            isSavable.value = uploadItems.value.isNotEmpty()
        }
        return ret
    }
    suspend fun postFile(ediUploadFileModel: List<EDIUploadFileModel>): RestResultT<List<EDIUploadFileModel>> {
        return ediListRepository.postFile(thisPK, ediUploadFileModel)
    }

    fun getMediaItems() = ArrayList(uploadItems.value.toMutableList())
    fun getMediaPathList() = ArrayList(uploadItems.value.map { it.mediaPath.toString() })
    fun getMediaNameList() = ArrayList(uploadItems.value.map { it.mediaName.toString() })
    fun getMediaFileTypeList() = ArrayList(uploadItems.value.map { it.mediaFileType.index })
    fun removeImage(data: MediaPickerSourceModel?) {
        uploadItems.value = uploadItems.value.filter { it != data }.toMutableList()
    }
    fun addImage(uriString: String?, name: String, fileType: MediaFileType, mimeType: String) {
        uriString ?: return
        try {
            val imageBuff = uploadItems.value.toMutableList()
            imageBuff.add(MediaPickerSourceModel().apply {
                mediaPath = uriString.toUri()
                mediaName = name
                mediaFileType = fileType
                mediaMimeType = mimeType
            })
            uploadItems.value = imageBuff
        } catch (_: Exception) {
        }
    }
    fun addImage(mediaPickerSource: MediaPickerSourceModel) {
        try {
            val imageBuff = uploadItems.value.toMutableList()
            imageBuff.add(mediaPickerSource)
            uploadItems.value = imageBuff
        } catch (_: Exception) {
        }
    }
    fun reSetImage(mediaList: ArrayList<MediaPickerSourceModel>?) {
        uploadItems.value = mutableListOf()
        mediaList?.forEach { x ->
            addImage(x)
        }
    }

    fun startBackgroundService() {
        val uploadFile = this.uploadItems.value.toMutableList()
        val data = EDISASKeyQueueModel().apply {
            ediPK = thisPK
            medias = uploadFile
            ediUploadModel = item.value
        }
        this.uploadItems.value = mutableListOf()
        backgroundService.sasKeyEnqueue(data)
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0),
        ADD_IMAGE(1),
        SAVE(2),
    }
}