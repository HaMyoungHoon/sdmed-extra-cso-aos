package sdmed.extra.cso.views.main.edi

import androidx.multidex.MultiDexApplication
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseFragment
import sdmed.extra.cso.databinding.EdiFragmentBinding

class EDIFragment: FBaseFragment<EdiFragmentBinding, EDIFragmentVM>() {
    override var layoutId = R.layout.edi_fragment
    override val dataContext: EDIFragmentVM by lazy {
        EDIFragmentVM(contextBuff!!.applicationContext as MultiDexApplication)
    }
    override fun viewInit() {
        binding?.dataContext = dataContext
        super.viewInit()
    }
    override fun setLayoutCommand(data: Any?) {
    }
}