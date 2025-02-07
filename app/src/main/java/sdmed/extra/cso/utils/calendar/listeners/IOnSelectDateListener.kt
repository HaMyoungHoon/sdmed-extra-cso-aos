package sdmed.extra.cso.utils.calendar.listeners

import java.util.Calendar

interface IOnSelectDateListener {
    @JvmSuppressWildcards
    fun onSelect(calendar: List<Calendar>)
}