package sdmed.extra.cso.views.main.edi.ediRequest

import androidx.core.net.toUri
import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import org.kodein.di.generic.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IEDIRequestRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.common.EDISASKeyQueueModel
import sdmed.extra.cso.models.common.MediaFileType
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.retrofit.edi.EDIApplyDateModel
import sdmed.extra.cso.models.retrofit.edi.EDIHosBuffModel
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaBuffModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaMedicineModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaModel
import sdmed.extra.cso.models.services.FBackgroundEDIRequestUpload
import sdmed.extra.cso.utils.FExtensions
import kotlin.collections.forEach
import kotlin.getValue

class EDIRequestFragmentVM(application: MultiDexApplication): FBaseViewModel(application) {
    private val ediRequestRepository: IEDIRequestRepository by kodein.instance(IEDIRequestRepository::class)
    private val backgroundService: FBackgroundEDIRequestUpload by kodein.instance(FBackgroundEDIRequestUpload::class)
    val applyDateModel = MutableStateFlow(mutableListOf<EDIApplyDateModel>())
    var selectApplyDate: EDIApplyDateModel? = null
    val hospitalModel = MutableStateFlow(mutableListOf<EDIHosBuffModel>())
    var selectHospital: EDIHosBuffModel? = null
    val pharmaModel = MutableStateFlow(mutableListOf<EDIPharmaBuffModel>())
    val selectPharma = mutableListOf<EDIPharmaBuffModel>()
    val uploadItems = MutableStateFlow(mutableListOf<MediaPickerSourceModel>())
    val isSavable = MutableStateFlow(false)
    val selectPharmaString = MutableStateFlow<String>("")

    suspend fun getApplyData(): RestResultT<List<EDIApplyDateModel>> {
        val ret = ediRequestRepository.getApplyDateList()
        if (ret.result == true) {
            applyDateModel.value = ret.data?.toMutableList() ?: mutableListOf()
            if (applyDateModel.value.size == 1) {
                applyDateSelect(applyDateModel.value[0])
            } else {
                applyDateSelect(null)
            }
        } else {
            applyDateSelect(null)
        }
        return ret
    }
    suspend fun getHospitalData(): RestResultT<List<EDIHosBuffModel>> {
        val yearMonthDay = selectApplyDate?.yearMonthDay ?: return RestResultT<List<EDIHosBuffModel>>().emptyResult()
        val ret = ediRequestRepository.getHospitalList(yearMonthDay)
        if (ret.result == true) {
            hospitalModel.value = ret.data?.toMutableList() ?: mutableListOf()
            if (this.hospitalModel.value.isNotEmpty()) {
                hospitalSelect(this.hospitalModel.value[0])
            } else {
                hospitalSelect(null)
            }
        } else {
            hospitalSelect(null)
        }
        return ret
    }
    fun getPharmaData() {
        pharmaModel.value = selectHospital?.pharmaList ?: mutableListOf()
        this.selectPharma.clear()
    }
    fun startBackgroundService() {
        val applyDate = selectApplyDate ?: return
        val hospitalData = selectHospital ?: return
        if (selectPharma.isEmpty()) {
            return
        }
        val uploadFile = this.uploadItems.value.toMutableList()
        if (uploadFile.isEmpty()) {
            return
        }

        val ediUploadModel = EDIUploadModel().apply {
            year = applyDate.year
            month = applyDate.month
            regDate = FExtensions.getTodayString()
            hospitalPK = hospitalData.thisPK
            orgName = hospitalData.orgName
        }
        selectPharma.forEach { x ->
            ediUploadModel.pharmaList.add(EDIUploadPharmaModel().apply {
                this.pharmaPK = x.thisPK
                x.medicineList.forEach { y ->
                    this@apply.medicineList.add(EDIUploadPharmaMedicineModel().apply {
                        this.pharmaPK = x.thisPK
                        this.medicinePK = y.thisPK
                    })
                }
            })
        }
        val data = EDISASKeyQueueModel().apply {
            medias = uploadFile
            this.ediUploadModel = ediUploadModel
        }
        this.uploadItems.value = mutableListOf()
        backgroundService.sasKeyEnqueue(data)
    }
    suspend fun applyDateSelect(data: EDIApplyDateModel?) {
        selectApplyDate?.isSelect?.value = false
        hospitalModel.value = mutableListOf()
        if (data == null) {
            selectApplyDate = null
            hospitalSelect(null)
            return
        }
        if (selectApplyDate?.thisPK == data.thisPK) {
            selectApplyDate = null
            hospitalSelect(null)
            return
        }
        selectApplyDate = data
        selectApplyDate?.isSelect?.value = true
        getHospitalData()
        savableCheck()
    }
    fun hospitalSelect(data: EDIHosBuffModel?) {
        selectPharma.clear()
        selectHospital?.isSelect?.value = false
        if (data == null) {
            selectHospital = null
            pharmaSelect(null)
            return
        }
        if (selectHospital?.thisPK == data.thisPK) {
            selectHospital = null
            pharmaSelect(null)
            return
        }
        selectHospital = data
        selectHospital?.isSelect?.value = true
        getPharmaData()
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
    }
    fun savableCheck() {
        if (selectPharma.isEmpty()) {
            selectPharmaString.value = ""
        } else {
            selectPharmaString.value = "${selectPharma.getOrNull(0)?.orgName}, (${selectPharma.size})"
        }
        if (selectApplyDate == null) {
            isSavable.value = false
            return
        }
        if (selectHospital == null) {
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
        PHARMA_SELECT(2),
    }
}