package sdmed.extra.cso.views.main.my

import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseFragment
import sdmed.extra.cso.databinding.MyFragmentBinding

class MyFragment: FBaseFragment<MyFragmentBinding, MyFragmentVM>() {
    override var layoutId = R.layout.my_fragment
    override val dataContext: MyFragmentVM by lazy {
        MyFragmentVM(multiDexApplication)
    }
    override fun viewInit() {
        binding?.dataContext
        super.viewInit()
    }
    override fun setLayoutCommand(data: Any?) {
    }
}