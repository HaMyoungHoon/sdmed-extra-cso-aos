package sdmed.extra.cso.views.main.edi.ediView

import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemEdiFileViewBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.retrofit.edi.EDIUploadFileModel

class EDIViewFileAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemEdiFileViewBinding, EDIUploadFileModel>(relayCommand) {
    override var layoutId = R.layout.list_item_edi_file_view
}