package sdmed.extra.cso.models.retrofit.qna

import sdmed.extra.cso.R

enum class QnAState(var index: Int, var desc: String) {
    None(0, "미지정"),
    OK(1, "완료"),
    Recep(2, "접수"),
    Reply(3, "답변");

    fun isEditable(): Boolean {
        return this == Reply
    }
    fun parseQnAColor() = when (this) {
        None -> R.color.qna_state_none
        OK -> R.color.qna_state_ok
        Recep -> R.color.qna_state_recep
        Reply -> R.color.qna_state_reply
    }
    fun parseQnABackColor() = when (this) {
        None -> R.color.qna_back_state_none
        OK -> R.color.qna_back_state_ok
        Recep -> R.color.qna_back_state_recep
        Reply -> R.color.qna_back_state_reply
    }
}