package sdmed.extra.cso.views.dialog.hospitalTemp

import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseDialogFragment
import sdmed.extra.cso.databinding.HospitalTempDialogBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel

class HospitalTempDialog(private val item: HospitalTempModel, private val relayCommand: ICommand): FBaseDialogFragment<HospitalTempDialogBinding, HospitalTempDialogVM>() {
    override var layoutId = R.layout.hospital_temp_dialog
    override val dataContext: HospitalTempDialogVM by lazy {
        HospitalTempDialogVM(multiDexApplication)
    }

    override fun viewInit() {
        super.viewInit()
        dataContext.item.value = item
    }

    override fun setLayoutCommand(data: Any?) {
        relayCommand.execute(arrayListOf(data, dataContext.item.value))
        if (data == HospitalTempDialogVM.ClickEvent.THIS) {
            dismiss()
        }
    }
}