package sdmed.extra.cso.views.main.qna.qnaAdd

import androidx.core.net.toUri
import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.generic.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.common.MediaFileType
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.QnASASKeyQueueModel
import sdmed.extra.cso.models.services.FBackgroundQnAUpload

class QnAAddActivityVM(application: MultiDexApplication): FBaseViewModel(application) {
    private val backgroundService: FBackgroundQnAUpload by kodein.instance(FBackgroundQnAUpload::class)
    var thisPK = MutableStateFlow<String>("")
    val title = MutableStateFlow<String>("")
    val postTitle = MutableStateFlow<String>("")
    val content = MutableStateFlow<String>("")
    val uploadItems = MutableStateFlow(mutableListOf<MediaPickerSourceModel>())
    val isSavable = MutableStateFlow(false)

    fun getMediaItems() = ArrayList(uploadItems.value.toMutableList())
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
        val data = QnASASKeyQueueModel(title = postTitle.value, content = content.value).apply {
            qnaPK = thisPK.value
            medias = uploadFile
        }
        this.uploadItems.value = mutableListOf()
        backgroundService.sasKeyEnqueue(data)
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0),
        ADD(1),
        SAVE(2)
    }
}