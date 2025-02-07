package sdmed.extra.cso.views.main.home

import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseFragment
import sdmed.extra.cso.databinding.HomeFragmentBinding

class HomeFragment: FBaseFragment<HomeFragmentBinding, HomeFragmentVM>() {
    override var layoutId = R.layout.home_fragment
    override val dataContext: HomeFragmentVM by lazy {
        HomeFragmentVM(multiDexApplication)
    }
    override fun viewInit() {
        binding?.dataContext = dataContext
        super.viewInit()
    }
    override fun setLayoutCommand(data: Any?) {
    }
}