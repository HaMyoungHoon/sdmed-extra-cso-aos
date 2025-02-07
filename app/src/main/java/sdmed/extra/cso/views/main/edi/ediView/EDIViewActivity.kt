package sdmed.extra.cso.views.main.edi.ediView

import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.databinding.EdiViewActivityBinding
import sdmed.extra.cso.models.retrofit.users.UserRole
import sdmed.extra.cso.models.retrofit.users.UserRoles

class EDIViewActivity: FBaseActivity<EdiViewActivityBinding, EDIViewActivityVM>(UserRoles.of(UserRole.Admin, UserRole.CsoAdmin, UserRole.BusinessMan)) {
    override var layoutId = R.layout.edi_view_activity
    override val dataContext: EDIViewActivityVM by lazy {
        EDIViewActivityVM(multiDexApplication)
    }
    override fun viewInit() {
        binding?.dataContext
        super.viewInit()
    }
    override fun setLayoutCommand(data: Any?) {
    }
}