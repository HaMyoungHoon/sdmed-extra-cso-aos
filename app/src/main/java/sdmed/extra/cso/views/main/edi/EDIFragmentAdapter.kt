package sdmed.extra.cso.views.main.edi

import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemEdiListBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel

class EDIFragmentAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemEdiListBinding, EDIUploadModel>(relayCommand) {
    override var layoutId = R.layout.list_item_edi_list
}