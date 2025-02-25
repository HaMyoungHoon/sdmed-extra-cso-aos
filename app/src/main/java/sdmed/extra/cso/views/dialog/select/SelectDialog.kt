package sdmed.extra.cso.views.dialog.select

import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseDialogFragment
import sdmed.extra.cso.databinding.SelectDialogBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.common.SelectListModel

class SelectDialog(private val items: List<SelectListModel>, private val orientation: Int, private val relayCommand: ICommand): FBaseDialogFragment<SelectDialogBinding, SelectDialogVM>() {
    override var layoutId = R.layout.select_dialog
    override val dataContext: SelectDialogVM by lazy {
        SelectDialogVM(multiDexApplication)
    }
    override fun viewInit() {
        dataContext.items.value = items.toMutableList()
        super.viewInit()
        setSelectAdapter()
    }
    override fun setLayoutCommand(data: Any?) {
        relayCommand.execute(data)
        dismiss()
    }

    private fun setSelectAdapter() {
        binding?.rvSelectList?.adapter = SelectDialogAdapter(dataContext.relayCommand)
        binding?.rvSelectList?.layoutManager = LinearLayoutManager(contextBuff, orientation, false)
    }

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerSelectItem")
        fun setSelectItem(recyclerView: RecyclerView, listItems: StateFlow<MutableList<SelectListModel>>?) {
            val adapter = recyclerView.adapter as? SelectDialogAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItems?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}