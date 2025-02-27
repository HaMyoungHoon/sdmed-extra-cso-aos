package sdmed.extra.cso.views.main.qna

import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemQnaHeaderBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.retrofit.qna.QnAHeaderModel

class QnAHeaderAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemQnaHeaderBinding, QnAHeaderModel>(relayCommand) {
    override var layoutId = R.layout.list_item_qna_header
}