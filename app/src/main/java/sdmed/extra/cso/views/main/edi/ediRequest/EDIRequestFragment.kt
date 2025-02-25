package sdmed.extra.cso.views.main.edi.ediRequest

import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseFragment
import sdmed.extra.cso.databinding.EdiRequestFragmentBinding
import sdmed.extra.cso.models.retrofit.users.UserRole
import sdmed.extra.cso.models.retrofit.users.UserRoles

class EDIRequestFragment: FBaseFragment<EdiRequestFragmentBinding, EDIRequestFragmentVM>(UserRoles.of(UserRole.Admin, UserRole.CsoAdmin, UserRole.BusinessMan)) {
    override var layoutId = R.layout.edi_request_fragment
    override val dataContext: EDIRequestFragmentVM by lazy {
        EDIRequestFragmentVM(multiDexApplication)
    }
    override fun viewInit() {
        super.viewInit()
    }
    override fun setLayoutCommand(data: Any?) {
    }
}