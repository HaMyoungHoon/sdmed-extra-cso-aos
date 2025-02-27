package sdmed.extra.cso.models.retrofit.edi

import sdmed.extra.cso.R

enum class EDIState(var index: Int, var desc: String) {
    None(0, "미지정"),
    OK(1, "완료"),
    Reject(2, "거부"),
    Pending(3, "보류"),
    Partial(4, "부분");

    fun isEditable(): Boolean {
        return !(this == OK || this == Reject)
    }
    fun parseEDIColor() = when (this) {
        None -> R.color.edi_state_none
        OK -> R.color.edi_state_ok
        Reject -> R.color.edi_state_reject
        Pending -> R.color.edi_state_pending
        Partial -> R.color.edi_state_partial
    }
    fun parseEDIBackColor() = when (this) {
        None -> R.color.edi_back_state_none
        OK -> R.color.edi_back_state_ok
        Reject -> R.color.edi_back_state_reject
        Pending -> R.color.edi_back_state_pending
        Partial -> R.color.edi_back_state_partial
    }
    companion object {
        fun parseEDIState(index: Int?) = when (index) {
            1 -> OK
            2 -> Reject
            3 -> Pending
            4 -> Partial
            else -> None
        }
        fun parseEDIColor(ediState: EDIState?) = when (ediState) {
            None -> R.color.edi_state_none
            OK -> R.color.edi_state_ok
            Reject -> R.color.edi_state_reject
            Pending -> R.color.edi_state_pending
            Partial -> R.color.edi_state_partial
            null -> R.color.edi_state_none
        }
        fun isEditable(ediState: EDIState): Boolean {
            return !(ediState == OK || ediState == Reject)
        }
    }
}