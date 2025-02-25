package sdmed.extra.cso.views.main.edi.ediView

import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemEdiPharmaListBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaModel

class EDIViewPharmaAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemEdiPharmaListBinding, EDIUploadPharmaModel>(relayCommand) {
    override var layoutId = R.layout.list_item_edi_pharma_list
}