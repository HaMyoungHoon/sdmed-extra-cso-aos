package sdmed.extra.cso.views.dialog.bottomTrainingCertificate

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseBottomDialogFragment
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.databinding.BottomTrainingCertificateAddDialogBinding
import sdmed.extra.cso.fDate.FDateTime
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.MediaViewParcelModel
import sdmed.extra.cso.models.eventbus.UserFileUploadEvent
import sdmed.extra.cso.models.retrofit.users.UserTrainingModel
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.FStorage.getParcelableList
import sdmed.extra.cso.utils.FStorage.putParcelable
import sdmed.extra.cso.utils.calendar.CalendarView
import sdmed.extra.cso.utils.calendar.DatePicker
import sdmed.extra.cso.utils.calendar.builders.DatePickerBuilder
import sdmed.extra.cso.utils.calendar.listeners.IOnSelectDateListener
import sdmed.extra.cso.views.media.picker.MediaPickerActivity
import sdmed.extra.cso.views.media.view.MediaViewActivity
import java.util.ArrayList
import java.util.Calendar
import kotlin.collections.forEach

class BottomTrainingCertificateAddDialog(var listItem: List<UserTrainingModel>): FBaseBottomDialogFragment<BottomTrainingCertificateAddDialogBinding, BottomTrainingCertificateAddDialogVM>() {
    override var layoutId = R.layout.bottom_training_certificate_add_dialog
    override val dataContext: BottomTrainingCertificateAddDialogVM by lazy {
        BottomTrainingCertificateAddDialogVM(multiDexApplication)
    }
    private var _imagePickerResult: ActivityResultLauncher<Intent>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerActivityResult()
    }
    override fun viewInit() {
        super.viewInit()
        setThisItems()
        setBottomTrainingCertificateAddAdapter()
        registerEventBus()
    }
    override fun setLayoutCommand(data: Any?) {
        setThisLayout(data)
        setBottomTrainingCertificateAddAdapterCommand(data)
    }
    private fun registerActivityResult() {
        _imagePickerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != RESULT_OK) {
                return@registerForActivityResult
            }
            val mediaList = it.data?.getParcelableList<MediaPickerSourceModel>(FConstants.MEDIA_LIST) ?: return@registerForActivityResult
            dataContext.setUploadBuff(mediaList)
        }
    }

    private fun setThisItems() {
        dataContext.trainingList.value = listItem.toMutableList()
    }
    private fun setBottomTrainingCertificateAddAdapter() = BottomTrainingCertificateAddAdapter(dataContext.relayCommand).also { binding?.rvTraining?.adapter = it }
    private fun registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }
    private fun setThisLayout(data: Any?) {
        val eventName = data as? BottomTrainingCertificateAddDialogVM.ClickEvent ?: return
        when (eventName) {
            BottomTrainingCertificateAddDialogVM.ClickEvent.CLOSE -> close()
            BottomTrainingCertificateAddDialogVM.ClickEvent.TRAINING_DATE -> trainingDate()
            BottomTrainingCertificateAddDialogVM.ClickEvent.ADD -> add()
            BottomTrainingCertificateAddDialogVM.ClickEvent.SAVE -> save()
        }
    }
    private fun setBottomTrainingCertificateAddAdapterCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? UserTrainingModel.ClickEvent ?: return
        val dataBuff = data[1] as? UserTrainingModel ?: return
        when (eventName) {
            UserTrainingModel.ClickEvent.THIS -> mediaView(dataBuff)
        }
    }

    private fun close() {
        dismiss()
    }
    private fun trainingDate() {
        loading()
        FCoroutineUtil.coroutineScope({
            calendarOpen(dataContext.trainingDate.value, {
                dataContext.trainingDate.value = it
            })?.show()
            loading(false)
        })
    }
    private fun add() {
        _imagePickerResult?.launch(Intent(contextBuff, MediaPickerActivity::class.java).apply {
            putExtra(FConstants.MEDIA_MAX_COUNT, 1)
        })
    }
    private fun save() {
        if (!dataContext.isSavable.value) {
            return
        }
        loading()
        dataContext.startBackground()
    }
    private fun mediaView(data: UserTrainingModel) {
        val context = contextBuff ?: return
        startActivity((Intent(context, MediaViewActivity::class.java)).apply {
            putParcelable(FConstants.MEDIA_ITEM, MediaViewParcelModel().parse(data))
        })
    }
    private fun calendarOpen(dateString: String, ret: (String) -> Unit): DatePicker? {
        val context = contextBuff ?: return null
        return DatePickerBuilder(context, object: IOnSelectDateListener {
            override fun onSelect(calendar: List<Calendar>) {
                calendar.forEach {
                    val dateTime = FDateTime().setThis(it.get(Calendar.YEAR), it.get(Calendar.MONTH) + 1, it.get(Calendar.DATE))
                    ret(dateTime.toString("yyyy-MM-dd"))
                }
            }
        }).minimumDate(FExtensions.getCalendarMinimumDate()).headerColor(R.color.def_background)
            .selectionColor(R.color.teal_200)
            .selectionLabelColor(R.color.primary)
            .pickerType(CalendarView.CLASSIC)
            .date(FExtensions.parseDateStringToCalendar(dateString))
            .selectedDays(arrayListOf(FExtensions.parseDateStringToCalendar(dateString)))
            .build()
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun userFileUploadEvent(data: UserFileUploadEvent) {
        loading(false)
        if (data.thisPK.isNotBlank()) {
            dismiss()
        }
    }
}