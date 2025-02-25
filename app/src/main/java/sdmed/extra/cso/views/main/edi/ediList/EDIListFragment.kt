package sdmed.extra.cso.views.main.edi.ediList

import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.BuildConfig
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseFragment
import sdmed.extra.cso.databinding.EdiListFragmentBinding
import sdmed.extra.cso.fDate.FDateTime
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.models.retrofit.users.UserRole
import sdmed.extra.cso.models.retrofit.users.UserRoles
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.calendar.CalendarView
import sdmed.extra.cso.utils.calendar.DatePicker
import sdmed.extra.cso.utils.calendar.builders.DatePickerBuilder
import sdmed.extra.cso.utils.calendar.listeners.IOnSelectDateListener
import sdmed.extra.cso.views.main.edi.ediView.EDIViewActivity
import java.util.ArrayList
import java.util.Calendar

class EDIListFragment: FBaseFragment<EdiListFragmentBinding, EDIListFragmentVM>(UserRoles.of(UserRole.Admin, UserRole.CsoAdmin, UserRole.BusinessMan)) {
    override var layoutId = R.layout.edi_list_fragment
    override val dataContext: EDIListFragmentVM by lazy {
        EDIListFragmentVM(multiDexApplication)
    }
    override fun viewInit() {
        setRecyclerView()
        super.viewInit()
        searchItem()
    }
    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setEDIListCommand(data)
    }

    private fun setThisCommand(data: Any?) {
        val eventName = data as? EDIListFragmentVM.ClickEvent ?: return
        when (eventName) {
            EDIListFragmentVM.ClickEvent.START_DATE -> openStartDate()
            EDIListFragmentVM.ClickEvent.END_DATE -> openEndDate()
            EDIListFragmentVM.ClickEvent.SEARCH -> searchItem()
        }
    }
    private fun setEDIListCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? EDIUploadModel.ClickEvent ?: return
        val dataBuff = data[1] as? EDIUploadModel ?: return
        when (eventName) {
            EDIUploadModel.ClickEvent.OPEN -> openEDIView(dataBuff)
        }
    }
    private fun setRecyclerView() {
        val binding = super.binding ?: return
        binding.rvEdiList.adapter = EDIListItemAdapter(dataContext.relayCommand)
    }

    private fun openStartDate() {
        loading()
        FCoroutineUtil.coroutineScope({
            calendarOpen(dataContext.startDate.value, {
                dataContext.startDate.value = it
            })?.show()
            loading(false)
        })
    }
    private fun openEndDate() {
        loading()
        FCoroutineUtil.coroutineScope({
            calendarOpen(dataContext.endDate.value, {
                dataContext.endDate.value = it
            })?.show()
            loading(false)
        })
    }
    private fun searchItem() {
        loading()
        FCoroutineUtil.coroutineScope({
            val ret = dataContext.getList()
            loading(false)
            if (ret.result != true) {
                toast(ret.msg)
            }
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val uri = "package:${BuildConfig.APPLICATION_ID}".toUri()
                startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))
            }
        }
    }
    private fun openEDIView(data: EDIUploadModel) {
        startActivity(Intent(contextBuff, EDIViewActivity::class.java).apply {
            putExtra("thisPK", data.thisPK)
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

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerEDIListItems")
        fun setEDIListItems(recyclerView: RecyclerView, listItems: StateFlow<MutableList<EDIUploadModel>>?) {
            val adapter = recyclerView.adapter as? EDIListItemAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItems?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}