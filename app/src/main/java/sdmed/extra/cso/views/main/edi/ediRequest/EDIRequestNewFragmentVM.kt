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
import sdmed.extra.cso.models.retrofit.edi.EDIType
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaModel
import sdmed.extra.cso.models.services.FBackgroundEDIRequestNewUpload
import sdmed.extra.cso.utils.FExtensions

class EDIRequestNewFragmentVM(application: MultiDexApplication): FBaseViewModel(application) {
    private val ediRequestRepository: IEDIRequestRepository by kodein.instance(IEDIRequestRepository::class)
    private val backgroundService: FBackgroundEDIRequestNewUpload by kodein.instance(FBackgroundEDIRequestNewUpload::class)
    val ediTypeModel = MutableStateFlow(mutableListOf<EDIType>())
    val selectEDITypePosition = MutableStateFlow(0)
    val applyDateModel = MutableStateFlow(mutableListOf<EDIApplyDateModel>())
    var selectApplyDate: EDIApplyDateModel? = null
    val tempOrgName = MutableStateFlow<String>("")
    val searchString = MutableStateFlow<String>("")
    val pharmaModel = MutableStateFlow(mutableListOf<EDIPharmaBuffModel>())
    val pharmaViewModel = MutableStateFlow(mutableListOf<EDIPharmaBuffModel>())
    val isSavable = MutableStateFlow(false)

    suspend fun getData(): RestResultT<List<EDIApplyDateModel>> {
        val ret = ediRequestRepository.getApplyDateList()
        if (ret.result == true) {
            applyDateModel.value = ret.data?.toMutableList() ?: mutableListOf()
        }
        return ret
    }
    suspend fun getPharmaList(): RestResultT<List<EDIPharmaBuffModel>> {
        val ret = ediRequestRepository.getPharmaList()
        if (ret.result == true) {
            pharmaModel.value = ret.data?.toMutableList() ?: mutableListOf()
            pharmaViewModel.value = pharmaModel.value
        }
        return ret
    }
    fun startBackgroundService() {
        return
        val applyDate = selectApplyDate ?: return
        val ediUploadModel = EDIUploadModel().apply {
            year = applyDate.year
            month = applyDate.month
            ediType = ediTypeModel.value[selectEDITypePosition.value]
            this.tempOrgName = this@EDIRequestNewFragmentVM.tempOrgName.value
            regDate = FExtensions.getTodayString()
        }
        pharmaModel.value.forEach { x ->
            if (x.uploadItems.value.isNotEmpty()) {
                ediUploadModel.pharmaList.add(EDIUploadPharmaModel().apply {
                    this.pharmaPK = x.thisPK
                    this.uploadItems.value = x.uploadItems.value
                })
            }
        }
        val data = EDISASKeyQueueModel().apply {
            this.ediUploadModel = ediUploadModel
        }
        backgroundService.sasKeyEnqueue(data)
        this.pharmaModel.value.forEach { x -> x.uploadItems.value = mutableListOf() }
        this.searchString.value = ""
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
    fun filterItem() {
        val searchBuff = searchString.value
        if (searchBuff.isEmpty()) {
            pharmaViewModel.value = pharmaModel.value.toMutableList()
            return
        }

        pharmaViewModel.value = pharmaModel.value.filter { x -> x.orgName.contains(searchBuff, true) }.toMutableList()
    }
    fun savableCheck() {
        if (selectApplyDate == null) {
            isSavable.value = false
            return
        }
        if (tempOrgName.value.isBlank()) {
            isSavable.value = false
            return
        }
        if (pharmaModel.value.none { x -> x.uploadItems.value.isNotEmpty() }) {
            isSavable.value = false
            return
        }
        isSavable.value = true
    }

    fun addImage(pharmaBuffPK: String, uriString: String?, name: String, fileType: MediaFileType, mimeType: String) {
        uriString ?: return
        val buff = this.pharmaModel.value.toMutableList()

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
            findBuff.isOpen.value = true
            this.pharmaModel.value = buff
        } catch (_: Exception) {
        }
        savableCheck()
    }
    fun delImage(imagePK: String) {
        val buff = this.pharmaModel.value.toMutableList()
        val findBuff = buff.find { x -> x.uploadItems.value.find { y -> y.thisPK == imagePK } != null } ?: return
        findBuff.uploadItems.value = findBuff.uploadItems.value.filter { it.thisPK != imagePK }.toMutableList()
        this.pharmaModel.value = buff
    }
    fun reSetImage(pharmaBuffPK: String, mediaList: ArrayList<MediaPickerSourceModel>?) {
        val buff = this.pharmaModel.value.toMutableList()
        val findPharma = buff.find { x -> x.thisPK == pharmaBuffPK }
        findPharma?.uploadItems?.value = mediaList?.toMutableList() ?: mutableListOf()
        findPharma?.isOpen?.value = true
        this.pharmaModel.value = buff
        savableCheck()
    }

    enum class ClickEvent(var index: Int) {
        SAVE(0)
    }
}