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
import sdmed.extra.cso.models.common.MediaViewParcelModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadFileModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaFileModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaModel
import sdmed.extra.cso.models.services.FBackgroundEDIFileUpload
import kotlin.collections.find

class EDIViewActivityVM(application: MultiDexApplication): FBaseViewModel(application) {
    private val ediListRepository: IEDIListRepository by kodein.instance(IEDIListRepository::class)
    private val backgroundService: FBackgroundEDIFileUpload by kodein.instance(FBackgroundEDIFileUpload::class)

    var thisPK: String = ""
    val item = MutableStateFlow(EDIUploadModel())
    val ellipseList = MutableStateFlow(mutableListOf<EllipseItemModel>())

    suspend fun getData(): RestResultT<EDIUploadModel> {
        val ret = ediListRepository.getData(thisPK)
        if (ret.result == true) {
            item.value = ret.data ?: EDIUploadModel()
            val ellipseBuff = mutableListOf<EllipseItemModel>()
            ret.data?.fileList?.forEach { ellipseBuff.add(EllipseItemModel()) }
            ellipseList.value = ellipseBuff
        }
        return ret
    }
    suspend fun postFile(ediUploadFileModel: List<EDIUploadFileModel>): RestResultT<List<EDIUploadFileModel>> {
        return ediListRepository.postFile(thisPK, ediUploadFileModel)
    }

    fun getMediaViewFiles(): ArrayList<MediaViewParcelModel> {
        val ret = arrayListOf<MediaViewParcelModel>()
        item.value.fileList.forEach { x ->
            ret.add(MediaViewParcelModel().parse(x))
        }
        return ret
    }
    fun getMediaViewPharmaFiles(data: EDIUploadPharmaFileModel): ArrayList<MediaViewParcelModel> {
        val buff = item.value.pharmaList.toMutableList()
        val ret = arrayListOf<MediaViewParcelModel>()
        val findBuff = buff.find { x -> x.fileList.find { y -> y.thisPK == data.thisPK } != null }
        findBuff?.fileList?.forEach { x ->
            ret.add(MediaViewParcelModel().parse(x))
        }
        return ret
    }
    fun addImage(pharmaBuffPK: String, uriString: String?, name: String, fileType: MediaFileType, mimeType: String) {
        uriString ?: return
        val buff = item.value.pharmaList.toMutableList()
        try {
            val findBuff = buff.find { x -> x.thisPK == pharmaBuffPK } ?: return
            val imageBuff = findBuff.uploadItems.value.toMutableList()
            imageBuff.add(MediaPickerSourceModel().apply {
                mediaPath = uriString.toUri()
                mediaName = name
                mediaFileType = fileType
                mediaMimeType = mimeType
            })
            findBuff.uploadItems.value = imageBuff
            item.value.pharmaList = buff
        } catch (_: Exception) {
        }
    }
    fun delImage(imagePK: String) {
        val buff = item.value.pharmaList.toMutableList()
        val findBuff = buff.find { x -> x.uploadItems.value.find { y -> y.thisPK == imagePK } != null } ?: return
        findBuff.uploadItems.value = findBuff.uploadItems.value.filter { it.thisPK != imagePK }.toMutableList()
        item.value.pharmaList = buff
    }
    fun reSetImage(pharmaBuffPK: String, mediaList: ArrayList<MediaPickerSourceModel>?) {
        val buff = item.value.pharmaList.toMutableList()
        val findBuff = buff.find { x -> x.thisPK == pharmaBuffPK } ?: return
        findBuff.uploadItems.value = mediaList?.toMutableList() ?: mutableListOf()
        item.value.pharmaList = buff
    }

    fun startBackgroundService(data: EDIUploadPharmaModel) {
//        return
//        backgroundService.sasKeyEnqueue(EDISASKeyQueueModel().apply {
//            ediPK = thisPK
//            ediUploadModel = item.value
//        })
        data.uploadItems.value = mutableListOf()
        val buff = item.value.pharmaList.toMutableList()
        val findBuff = buff.find { x -> x.thisPK == data.thisPK } ?: return
        findBuff.uploadItems.value = mutableListOf()
        item.value.pharmaList = buff
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0)
    }
}