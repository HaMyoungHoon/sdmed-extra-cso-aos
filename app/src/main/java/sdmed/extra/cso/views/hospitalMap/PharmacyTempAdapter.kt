package sdmed.extra.cso.views.hospitalMap

import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemPharmacyTempBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.retrofit.hospitals.PharmacyTempModel

class PharmacyTempAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemPharmacyTempBinding, PharmacyTempModel>(relayCommand) {
    override var layoutId = R.layout.list_item_pharmacy_temp

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerPharmacyTemp")
        fun setRecyclerPharmacyTemp(recyclerView: RecyclerView, listItems: StateFlow<MutableList<PharmacyTempModel>>?) {
            val adapter = recyclerView.adapter as? PharmacyTempAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItems?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}