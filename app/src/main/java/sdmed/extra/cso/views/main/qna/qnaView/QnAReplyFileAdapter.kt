package sdmed.extra.cso.views.main.qna.qnaView

import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemQnaReplyFileViewBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.retrofit.qna.QnAReplyFileModel

class QnAReplyFileAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemQnaReplyFileViewBinding, QnAReplyFileModel>(relayCommand) {
    override var layoutId = R.layout.list_item_qna_reply_file_view
}