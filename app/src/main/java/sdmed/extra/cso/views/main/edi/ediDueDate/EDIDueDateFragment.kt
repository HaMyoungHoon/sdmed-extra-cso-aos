package sdmed.extra.cso.views.main.edi.ediDueDate

import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseFragment
import sdmed.extra.cso.databinding.EdiDueDateFragmentBinding
import sdmed.extra.cso.fDate.FDateTime
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaDueDateModel
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.calendar.CalendarView
import sdmed.extra.cso.utils.calendar.DatePicker
import sdmed.extra.cso.utils.calendar.builders.DatePickerBuilder
import sdmed.extra.cso.utils.calendar.listeners.IOnSelectDateListener
import java.util.ArrayList
import java.util.Calendar
import kotlin.collections.forEach

class EDIDueDateFragment: FBaseFragment<EdiDueDateFragmentBinding, EDIDueDateFragmentVM>() {
    override var layoutId = R.layout.edi_due_date_fragment
    override val dataContext: EDIDueDateFragmentVM by lazy {
        EDIDueDateFragmentVM(multiDexApplication)
    }

    override fun viewInit() {
        setRecyclerView()
        super.viewInit()
        getList()
    }
    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setEDIDueDateCommand(data)
    }
    private fun setThisCommand(data: Any?) {
        val eventName = data as? EDIDueDateFragmentVM.ClickEvent ?: return
        when (eventName) {
            EDIDueDateFragmentVM.ClickEvent.START_DATE -> openStartDate()
            EDIDueDateFragmentVM.ClickEvent.END_DATE -> openEndDate()
            EDIDueDateFragmentVM.ClickEvent.SEARCH -> getList()
        }
    }
    private fun setEDIDueDateCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? EDIPharmaDueDateModel.ClickEvent ?: return
        val dataBuff = data[1] as? EDIPharmaDueDateModel ?: return
        when (eventName) {
            EDIPharmaDueDateModel.ClickEvent.THIS -> {

            }
        }
    }
    private fun setRecyclerView() = EDIDueDateAdapter(dataContext.relayCommand).also { binding?.rvEdiDueDate?.adapter = it }
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
    private fun getList() {
        loading()
        FCoroutineUtil.coroutineScope({
            val ret = dataContext.getList()
            loading(false)
            if (ret.result != true) {
                toast(ret.msg)
            }
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
}