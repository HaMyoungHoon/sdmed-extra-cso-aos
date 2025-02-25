package sdmed.extra.cso.models.adapter

import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemEllipseListBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.common.EllipseItemModel

class EllipseListAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemEllipseListBinding, EllipseItemModel>(relayCommand) {
    override var layoutId = R.layout.list_item_ellipse_list
}