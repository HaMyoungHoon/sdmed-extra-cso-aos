package sdmed.extra.cso.models.retrofit.edi

import sdmed.extra.cso.R

enum class EDIState(var index: Int, var desc: String) {
    None(0, "미지정"),
    OK(1, "완료"),
    Reject(2, "거부"),
    Pending(3, "보류"),
    Partial(4, "부분");
    companion object {
        fun parseEDIColor(ediState: EDIState?) = when (ediState) {
            None -> R.color.edi_state_none
            OK -> R.color.edi_state_ok
            Reject -> R.color.edi_state_reject
            Pending -> R.color.edi_state_pending
            Partial -> R.color.edi_state_partial
            null -> R.color.edi_state_none
        }
    }
}