package sdmed.extra.cso.views.main.edi.ediView

import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemEdiResponseListBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.retrofit.edi.EDIUploadResponseModel

class EDIViewResponseAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemEdiResponseListBinding, EDIUploadResponseModel>(relayCommand) {
    override var layoutId = R.layout.list_item_edi_response_list
}