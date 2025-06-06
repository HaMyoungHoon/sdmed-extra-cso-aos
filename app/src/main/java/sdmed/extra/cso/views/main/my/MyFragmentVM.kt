package sdmed.extra.cso.views.main.my

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.generic.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.interfaces.repository.IMyInfoRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.UserFileSASKeyQueueModel
import sdmed.extra.cso.models.retrofit.hospitals.HospitalModel
import sdmed.extra.cso.models.retrofit.pharmas.PharmaModel
import sdmed.extra.cso.models.retrofit.users.UserDataModel
import sdmed.extra.cso.models.services.FBackgroundUserFileUpload

class MyFragmentVM(application: MultiDexApplication): FBaseViewModel(application) {
    private val backgroundService: FBackgroundUserFileUpload by kodein.instance(FBackgroundUserFileUpload::class)
    private val myInfoRepository: IMyInfoRepository by kodein.instance(IMyInfoRepository::class)
    val thisData = MutableStateFlow(UserDataModel())
    val hosList = MutableStateFlow(mutableListOf<HospitalModel>())
    val selectedHos = MutableStateFlow<HospitalModel>(HospitalModel())
    val pharmaList = MutableStateFlow(mutableListOf<PharmaModel>())

    suspend fun getData(): RestResultT<UserDataModel> {
        val ret = myInfoRepository.getData()
        if (ret.result == true) {
            thisData.value = ret.data ?: UserDataModel()
        }
        return ret
    }
    fun userFileUpload(mediaTypeIndex: Int, mediaList: ArrayList<MediaPickerSourceModel>) {
        if (mediaTypeIndex == -1) return
        if (mediaList.isEmpty()) return

        backgroundService.sasKeyEnqueue(UserFileSASKeyQueueModel().apply {
            medias.add(mediaList.first())
            this.mediaTypeIndex = mediaTypeIndex
        })
    }

    enum class ClickEvent(var index: Int) {
        LOGOUT(0),
        PASSWORD_CHANGE(1),
        MULTI_LOGIN(2),
        IMAGE_TRAINING(3),
        TRAINING_CERTIFICATE_ADD(4),
        IMAGE_TAXPAYER(5),
        IMAGE_BANK_ACCOUNT(6),
        IMAGE_CSO_REPORT(7),
        IMAGE_MARKETING_CONTRACT(8),
    }
}