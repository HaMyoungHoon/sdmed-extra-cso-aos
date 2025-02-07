package sdmed.extra.cso.views.main.qna

import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseFragment
import sdmed.extra.cso.databinding.QnaFragmentBinding

class QnAFragment: FBaseFragment<QnaFragmentBinding, QnAFragmentVM>() {
    override var layoutId = R.layout.qna_fragment
    override val dataContext: QnAFragmentVM by lazy {
        QnAFragmentVM(multiDexApplication)
    }
    override fun viewInit() {
        binding?.dataContext = dataContext
        super.viewInit()
    }
    override fun setLayoutCommand(data: Any?) {
        super.setLayoutCommand(data)
    }
}