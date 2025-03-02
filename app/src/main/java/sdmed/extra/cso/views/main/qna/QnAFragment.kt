package sdmed.extra.cso.views.main.qna

import android.content.Intent
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseFragment
import sdmed.extra.cso.databinding.QnaFragmentBinding
import sdmed.extra.cso.models.common.PageNumberModel
import sdmed.extra.cso.models.common.PaginationModel
import sdmed.extra.cso.models.eventbus.QnAUploadEvent
import sdmed.extra.cso.models.retrofit.qna.QnAHeaderModel
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.views.main.qna.qnaAdd.QnAAddActivity
import sdmed.extra.cso.views.main.qna.qnaView.QnAViewActivity
import java.util.ArrayList

class QnAFragment: FBaseFragment<QnaFragmentBinding, QnAFragmentVM>() {
    override var layoutId = R.layout.qna_fragment
    override val dataContext: QnAFragmentVM by lazy {
        QnAFragmentVM(multiDexApplication)
    }
    override fun viewInit() {
        super.viewInit()
        setQnaAdapter()
        setPagination()
        getList()
        getSearch()
    }
    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setQnaCommand(data)
        setPaginationCommand(data)
        setPageNumberCommand(data)
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
    private fun setQnaAdapter() = QnAHeaderAdapter(dataContext.relayCommand).also { binding?.rvQnaHeader?.adapter = it }
    private fun setPagination() = binding?.includePagination?.afterInit(dataContext.relayCommand)
    @OptIn(FlowPreview::class)
    private fun getSearch() {
        lifecycleScope.launch {
            dataContext.searchBuff.debounce(1000).collectLatest {
                it ?: return@collectLatest
                if (dataContext.searchString != it) {
                    dataContext.searchString = it
                    getList()
                }
                dataContext.searchLoading.value = false
            }
        }
        lifecycleScope.launch {
            dataContext.searchBuff.collectLatest {
                it ?: return@collectLatest
                dataContext.searchLoading.value = true
            }
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }
    private fun setThisCommand(data: Any?) {
        val eventName = data as? QnAFragmentVM.ClickEvent ?: return
        when (eventName) {
            QnAFragmentVM.ClickEvent.ADD -> {
                startActivity(Intent(contextBuff, QnAAddActivity::class.java))
            }
        }
    }
    private fun setQnaCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? QnAHeaderModel.ClickEvent ?: return
        val dataBuff = data[1] as? QnAHeaderModel ?: return
        when (eventName) {
            QnAHeaderModel.ClickEvent.THIS -> {
                startActivity(Intent(contextBuff, QnAViewActivity::class.java).apply {
                    putExtra("thisPK", dataBuff.thisPK)
                })
            }
        }
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun qnaUploadEvent(qnaUploadEvent: QnAUploadEvent) {
        getList()
    }

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerQnAHeader")
        fun setRecyclerQnaHeader(recyclerView: RecyclerView, listItems: StateFlow<MutableList<QnAHeaderModel>>?) {
            val adapter = recyclerView.adapter as? QnAHeaderAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItems?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}