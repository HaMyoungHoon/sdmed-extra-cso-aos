package sdmed.extra.cso.models.adapter

import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemPageNumberBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.common.PageNumberModel

class PageNumberAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemPageNumberBinding, PageNumberModel>(relayCommand) {
    override var layoutId = R.layout.list_item_page_number
}