package sdmed.extra.cso.views.hospitalMap

import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.databinding.ListItemHospitalTempBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.adapter.FRecyclerAdapter
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel

class HospitalTempAdapter(relayCommand: ICommand): FRecyclerAdapter<ListItemHospitalTempBinding, HospitalTempModel>(relayCommand) {
    override var layoutId = R.layout.list_item_hospital_temp

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerHospitalTemp")
        fun setRecyclerHospitalTemp(recyclerView: RecyclerView, listItems: StateFlow<MutableList<HospitalTempModel>>?) {
            val adapter = recyclerView.adapter as? HospitalTempAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItems?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}