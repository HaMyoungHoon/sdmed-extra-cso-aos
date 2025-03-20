package sdmed.extra.cso.views.dialog.bottomTrainingCertificate

import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.generic.instance
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.UserFileSASKeyQueueModel
import sdmed.extra.cso.models.common.UserTrainingFileSASKeyQueueModel
import sdmed.extra.cso.models.retrofit.users.UserTrainingModel
import sdmed.extra.cso.models.services.FBackgroundUserFileUpload
import sdmed.extra.cso.utils.FExtensions

class BottomTrainingCertificateAddDialogVM(application: MultiDexApplication): FBaseViewModel(application) {
    private val backgroundService: FBackgroundUserFileUpload by kodein.instance(FBackgroundUserFileUpload::class)
    val trainingList = MutableStateFlow(mutableListOf<UserTrainingModel>())
    val uploadBuff = MutableStateFlow<MediaPickerSourceModel?>(null)
    val trainingDate = MutableStateFlow(FExtensions.getTodayString())
    val isSavable = MutableStateFlow(false)

    fun setUploadBuff(data: ArrayList<MediaPickerSourceModel>) {
        if (data.isEmpty()) {
            return
        }
        uploadBuff.value = data[0]
        checkSavable()
    }
    fun startBackground() {
        val uploadBuff = uploadBuff.value ?: return
        backgroundService.sasKeyEnqueue(UserTrainingFileSASKeyQueueModel().apply {
            media = uploadBuff
            this.trainingDate = this@BottomTrainingCertificateAddDialogVM.trainingDate.value
        })
    }
    fun checkSavable() {
        isSavable.value = uploadBuff.value != null
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0),
        TRAINING_DATE(1),
        ADD(2),
        SAVE(3),
    }
}