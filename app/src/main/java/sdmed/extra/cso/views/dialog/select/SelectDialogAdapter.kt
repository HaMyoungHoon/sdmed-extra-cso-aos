package sdmed.extra.cso.views.dialog.select

import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemSelectDialogListBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.common.SelectListModel

class SelectDialogAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemSelectDialogListBinding, SelectListModel>(relayCommand) {
    override var layoutId = R.layout.list_item_select_dialog_list
}