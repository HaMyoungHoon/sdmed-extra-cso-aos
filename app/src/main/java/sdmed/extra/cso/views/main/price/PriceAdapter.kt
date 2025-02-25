package sdmed.extra.cso.views.main.price

import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemPriceBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.retrofit.medicines.MedicineModel

class PriceAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemPriceBinding, MedicineModel>(relayCommand) {
    override var layoutId = R.layout.list_item_price
}