package sdmed.extra.cso.views.main.qna.qnaView

import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemQnaFileBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.retrofit.qna.QnAFileModel

class QnAFileAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemQnaFileBinding, QnAFileModel>(relayCommand) {
    override var layoutId = R.layout.list_item_qna_file
}