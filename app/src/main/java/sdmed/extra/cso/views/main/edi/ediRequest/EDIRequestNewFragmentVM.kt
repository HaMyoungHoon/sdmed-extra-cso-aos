package sdmed.extra.cso.views.main.edi.ediRequest

import androidx.core.net.toUri
import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.generic.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IEDIRequestRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.common.EDISASKeyQueueModel
import sdmed.extra.cso.models.common.MediaFileType
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.retrofit.edi.EDIApplyDateModel
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaBuffModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaModel
import sdmed.extra.cso.models.services.FBackgroundEDIRequestNewUpload
import sdmed.extra.cso.utils.FExtensions

class EDIRequestNewFragmentVM(application: MultiDexApplication): FBaseViewModel(application) {
    private val ediRequestRepository: IEDIRequestRepository by kodein.instance(IEDIRequestRepository::class)
    private val backgroundService: FBackgroundEDIRequestNewUpload by kodein.instance(FBackgroundEDIRequestNewUpload::class)
    val applyDateModel = MutableStateFlow(mutableListOf<EDIApplyDateModel>())
    var selectApplyDate: EDIApplyDateModel? = null
    val etc = MutableStateFlow<String>("")
    val pharmaModel = MutableStateFlow(mutableListOf<EDIPharmaBuffModel>())
    val selectPharma = mutableListOf<EDIPharmaBuffModel>()
    val selectPharmaString = MutableStateFlow<String>("")
    val uploadItems = MutableStateFlow(mutableListOf<MediaPickerSourceModel>())
    val isSavable = MutableStateFlow(false)

    suspend fun getData(): RestResultT<List<EDIApplyDateModel>> {
        val ret = ediRequestRepository.getApplyDateList()
        if (ret.result == true) {
            applyDateModel.value = ret.data?.toMutableList() ?: mutableListOf()
        }
        return ret
    }
    suspend fun getPharmaList(): RestResultT<List<EDIPharmaBuffModel>> {
        val yearMonthDay = selectApplyDate?.yearMonthDay ?: return RestResultT<List<EDIPharmaBuffModel>>().emptyResult()
        val ret = ediRequestRepository.getPharmaList(yearMonthDay)
        if (ret.result == true) {
            pharmaModel.value = ret.data?.toMutableList() ?: mutableListOf()
        }
        return ret
    }
    fun startBackgroundService() {
        val applyDate = selectApplyDate ?: return
        val uploadFile = this.uploadItems.value.toMutableList()
        if (uploadFile.isEmpty()) {
            return
        }
        val ediUploadModel = EDIUploadModel().apply {
            year = applyDate.year
            month = applyDate.month
            this.etc = this@EDIRequestNewFragmentVM.etc.value
            regDate = FExtensions.getTodayString()
        }
        selectPharma.forEach { x ->
            ediUploadModel.pharmaList.add(EDIUploadPharmaModel().apply {
                this.pharmaPK = x.thisPK
            })
        }
        val data = EDISASKeyQueueModel().apply {
            medias = uploadFile
            this.ediUploadModel = ediUploadModel
        }
        this.uploadItems.value = mutableListOf()
        backgroundService.sasKeyEnqueue(data)
    }
    fun applyDateSelect(data: EDIApplyDateModel) {
        if (selectApplyDate == data) {
            selectApplyDate?.isSelect?.value = false
            selectApplyDate = null
            savableCheck()
            return
        }
        selectApplyDate?.isSelect?.value = false
        selectApplyDate = data
        selectApplyDate?.isSelect?.value = true
        savableCheck()
    }
    fun pharmaSelect(data: List<EDIPharmaBuffModel>) {
        selectPharma.clear()
        selectPharma.addAll(data)
        pharmaModel.value.forEach { x -> x.isSelect.value = false }
        pharmaModel.value.filter { x -> x.thisPK in data.map { it.thisPK } }.forEach { x -> x.isSelect.value = true }

        if (selectPharma.isEmpty()) {
            selectPharmaString.value = ""
        } else {
            selectPharmaString.value = "${selectPharma.getOrNull(0)?.orgName}, (${selectPharma.size})"
        }
        savableCheck()
    }
    fun pharmaSelect(data: EDIPharmaBuffModel?) {
        if (data == null) {
            pharmaModel.value = mutableListOf()
            savableCheck()
            return
        }
        val alreadyData = selectPharma.find { x -> x.thisPK == data.thisPK }
        if (alreadyData == null) {
            data.isSelect.value = true
            selectPharma.add(data)
        } else {
            data.isSelect.value = false
            selectPharma.remove(data)
        }
        savableCheck()
    }
    fun savableCheck() {
        if (selectApplyDate == null) {
            isSavable.value = false
            return
        }
        if (etc.value.isBlank()) {
            isSavable.value = false
            return
        }
        if (selectPharma.isEmpty()) {
            isSavable.value = false
            return
        }
        isSavable.value = uploadItems.value.isNotEmpty()
    }
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

    enum class ClickEvent(var index: Int) {
        ADD(0),
        SAVE(1),
        PHARMA_SELECT(2)
    }
}