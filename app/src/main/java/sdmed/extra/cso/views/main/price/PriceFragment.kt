package sdmed.extra.cso.views.main.price

import androidx.databinding.BindingAdapter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseFragment
import sdmed.extra.cso.databinding.PriceFragmentBinding
import sdmed.extra.cso.models.common.PageNumberModel
import sdmed.extra.cso.models.common.PaginationModel
import sdmed.extra.cso.models.retrofit.medicines.MedicineModel
import sdmed.extra.cso.utils.FCoroutineUtil
import java.util.ArrayList

class PriceFragment: FBaseFragment<PriceFragmentBinding, PriceFragmentVM>() {
    override var layoutId = R.layout.price_fragment
    override val dataContext: PriceFragmentVM by lazy {
        PriceFragmentVM(multiDexApplication)
    }
    override fun viewInit() {
        binding?.dataContext = dataContext
        super.viewInit()
        setPriceAdapter()
        setPagination()
        getList()
        getSearch()
    }
    override fun setLayoutCommand(data: Any?) {
        setPaginationCommand(data)
        setPageNumberCommand(data)
    }

    private fun setPriceAdapter() = binding?.rvPriceList?.adapter = PriceAdapter(viewLifecycleOwner, dataContext.relayCommand)
    private fun setPagination() = binding?.includePagination?.afterInit(viewLifecycleOwner, dataContext.paginationModel, dataContext.relayCommand)
    @OptIn(FlowPreview::class)
    private fun getSearch() {
        lifecycleScope.launch {
            dataContext.searchBuff.debounce(1000).collectLatest {
                if (dataContext.searchString != it) {
                    dataContext.searchString = it ?: ""
                    getList()
                }
                dataContext.searchLoading.value = false
            }
        }
        lifecycleScope.launch {
            dataContext.searchBuff.collectLatest {
                dataContext.searchBuff.value ?: return@collectLatest
                dataContext.searchLoading.value = true
            }
        }
    }
    private fun getList() {
        loading()
        FCoroutineUtil.coroutineScope({
            val ret = if (dataContext.searchString.isBlank()) {
                dataContext.getList()
            } else {
                dataContext.getLike()
            }
            loading(false)
            if (ret.result != true) {
                toast(ret.msg)
            }
        })
    }
    private fun addList() {
        loading()
        FCoroutineUtil.coroutineScope({
            val ret = if (dataContext.searchString.isBlank()) {
                dataContext.addList()
            } else {
                dataContext.addLike()
            }
            loading(false)
            if (ret.result != true) {
                toast(ret.msg)
            }
        })
    }
    private fun setPaginationCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? PaginationModel.ClickEvent ?: return
        val dataBuff = data[1] as? PaginationModel ?: return
        when (eventName) {
            PaginationModel.ClickEvent.FIRST -> {
                if (dataBuff.first.value == true) {
                    return
                }
                dataContext.page.value = 0
                binding?.includePagination?.firstSelect()
                addList()
            }
            PaginationModel.ClickEvent.PREV -> { }
            PaginationModel.ClickEvent.NEXT -> { }
            PaginationModel.ClickEvent.LAST -> {
                if (dataBuff.last.value == true) {
                    return
                }
                dataContext.page.value = dataBuff.totalPages - 1
                binding?.includePagination?.lastSelect()
                addList()
            }
        }
    }
    private fun setPageNumberCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? PageNumberModel.ClickEvent ?: return
        val dataBuff = data[1] as? PageNumberModel ?: return
        when (eventName) {
            PageNumberModel.ClickEvent.THIS -> {
                if (dataContext.page.value == dataBuff.pageNumber - 1) {
                    return
                }

                dataContext.page.value = dataBuff.pageNumber - 1
                binding?.includePagination?.updateSelect(dataBuff.pageNumber - 1)
                addList()
            }
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerPriceList")
        fun setRecyclerPrice(recyclerView: RecyclerView, listItems: StateFlow<MutableList<MedicineModel>>?) {
            val adapter = recyclerView.adapter as? PriceAdapter ?: return
            adapter.lifecycleOwner.lifecycleScope.launch {
                listItems?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}