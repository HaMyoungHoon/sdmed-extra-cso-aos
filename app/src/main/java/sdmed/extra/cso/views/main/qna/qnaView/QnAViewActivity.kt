package sdmed.extra.cso.views.main.qna.qnaView

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.databinding.QnaViewActivityBinding
import sdmed.extra.cso.models.adapter.EllipseListAdapter
import sdmed.extra.cso.models.eventbus.QnAUploadEvent
import sdmed.extra.cso.models.retrofit.qna.QnAContentModel
import sdmed.extra.cso.models.retrofit.qna.QnAReplyModel
import sdmed.extra.cso.models.retrofit.users.UserRole
import sdmed.extra.cso.models.retrofit.users.UserRoles
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.views.main.qna.qnaAdd.QnAAddActivity
import java.util.ArrayList

class QnAViewActivity: FBaseActivity<QnaViewActivityBinding, QnAViewActivityVM>(UserRoles.of(UserRole.Admin, UserRole.CsoAdmin, UserRole.BusinessMan)) {
    override var layoutId = R.layout.qna_view_activity
    override val dataContext: QnAViewActivityVM by lazy {
        QnAViewActivityVM(multiDexApplication)
    }

    private var _qnaAddResult: ActivityResultLauncher<Intent>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerActivityResult()
    }
    override fun viewInit() {
        super.viewInit()
        dataContext.thisPK = intent.getStringExtra("thisPK") ?: ""
        setContentFileAdapter()
        setEllipseAdapter()
        setReplyAdapter()
        getData()
    }
    override fun onDestroy() {
        _qnaAddResult?.unregister()
        _qnaAddResult = null
        super.onDestroy()
    }
    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setReplyCommand(data)
    }
    private fun registerActivityResult() {
        _qnaAddResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != RESULT_OK) {
                return@registerForActivityResult
            }
            finish()
        }
    }
    private fun setThisCommand(data: Any?) {
        val eventName = data as? QnAViewActivityVM.ClickEvent ?: return
        when (eventName) {
            QnAViewActivityVM.ClickEvent.CLOSE -> close()
            QnAViewActivityVM.ClickEvent.COLLAPSE -> dataContext.collapseContent.value = !dataContext.collapseContent.value
            QnAViewActivityVM.ClickEvent.ADD -> {
                _qnaAddResult?.launch(Intent(this, QnAAddActivity::class.java).apply {
                    putExtra("thisPK", dataContext.thisPK)
                    putExtra("title", dataContext.headerModel.value.title)
                })
            }
            QnAViewActivityVM.ClickEvent.COMP -> postData()
        }

    }
    private fun setReplyCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? QnAReplyModel.ClickEvent ?: return
        val dataBuff = data[1] as? QnAReplyModel ?: return
        when (eventName) {
            QnAReplyModel.ClickEvent.OPEN -> dataBuff.open.value = !dataBuff.open.value
        }
    }
    private fun setContentFileAdapter() {
        binding?.vpQnaFileList?.adapter = QnAFileAdapter(dataContext.relayCommand)
        binding?.vpQnaFileList?.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateEllipseList(position)
            }
        })
    }
    private fun setEllipseAdapter() = binding?.rvEllipseList?.adapter = EllipseListAdapter(dataContext.relayCommand)
    private fun setReplyAdapter() = binding?.rvReplyList?.adapter = QnAReplyAdapter(dataContext.relayCommand)
    private fun updateEllipseList(position: Int) {
        val buff = dataContext.ellipseList.value
        if (position < 0 || position >= buff.size) {
            return
        }
        buff.forEach { x -> x.initThis() }
        buff[position].selectThis()
        buff.getOrNull(position - 3)?.tinyThis()
        buff.getOrNull(position - 2)?.visibleThis()
        buff.getOrNull(position - 1)?.visibleThis()
        buff.getOrNull(position + 1)?.visibleThis()
        buff.getOrNull(position + 2)?.visibleThis()
        buff.getOrNull(position + 3)?.tinyThis()
    }
    private fun getData() {
        loading()
        FCoroutineUtil.coroutineScope({
            loading(false)
            val ret = dataContext.getData()
            if (ret.result != true) {
                toast(ret.msg)
            }
        })
    }
    private fun postData() {
        loading()
        FCoroutineUtil.coroutineScope({
            val ret = dataContext.postData()
            loading(false)
            if (ret.result != true) {
                toast(ret.msg)
            }
        })
    }
    private fun close() {
        finish()
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun qnaUploadEvent(qnaUploadEvent: QnAUploadEvent) {
        if (dataContext.thisPK == qnaUploadEvent.qnaPK) {
            getData()
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("viewPagerQnAFileList")
        fun setViewPagerQnAFileList(viewPager2: ViewPager2, item: StateFlow<QnAContentModel>?) {
            val adapter = viewPager2.adapter as? QnAFileAdapter ?: return
            viewPager2.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                item?.collectLatest {
                    adapter.updateItems(it.fileList)
                }
            }
        }
        @JvmStatic
        @BindingAdapter("recyclerQnAReplyList")
        fun setRecyclerQnaReplyList(recyclerView: RecyclerView, item: StateFlow<QnAContentModel>?) {
            val adapter = recyclerView.adapter as? QnAReplyAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                item?.collectLatest {
                    adapter.updateItems(it.replyList)
                }
            }
        }
    }
}