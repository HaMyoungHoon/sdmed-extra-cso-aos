package sdmed.extra.cso.views.dialog.bottomLogin

import org.greenrobot.eventbus.EventBus
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseBottomDialogFragment
import sdmed.extra.cso.databinding.BottomLoginDialogBinding
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.models.eventbus.MultiSignEvent
import sdmed.extra.cso.models.retrofit.FRetrofitVariable
import sdmed.extra.cso.models.retrofit.users.UserMultiLoginModel
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FStorage

class BottomLoginDialog(var isAddVisible: Boolean = true, var relayCommand: ICommand): FBaseBottomDialogFragment<BottomLoginDialogBinding, BottomLoginDialogVM>() {
    override var layoutId = R.layout.bottom_login_dialog
    override val dataContext: BottomLoginDialogVM by lazy {
        BottomLoginDialogVM(multiDexApplication)
    }

    override fun viewInit() {
        super.viewInit()
        dataContext.isAddVisible.value = isAddVisible
        setThisItems()
        setBottomLoginAdapter()
    }
    override fun setLayoutCommand(data: Any?) {
        setThisLayout(data)
        setBottomLoginAdapterCommand(data)
    }

    private fun multiSign(item: UserMultiLoginModel) {
        val context = contextBuff ?: return
        loading()
        FCoroutineUtil.coroutineScope({
            val ret = dataContext.multiSign(item.token)
            loading(false)
            if (ret.result == true) {
                FRetrofitVariable.token = ret.data
                FStorage.setAuthToken(context, ret.data)
                addLoginData()
                EventBus.getDefault().post(MultiSignEvent())
                dismiss()
                return@coroutineScope
            }
            if (ret.code == -10002) {
                FStorage.delMultiLoginData(context, item)
                FStorage.getMultiLoginData(context)?.let {
                    dataContext.items.value = it.toMutableList()
                }
            }
            toast(ret.msg)
        })
    }

    private fun setThisLayout(data: Any?) {
        val eventName = data as? BottomLoginDialogVM.ClickEvent ?: return
        when (eventName) {
            BottomLoginDialogVM.ClickEvent.CLOSE -> dismiss()
            BottomLoginDialogVM.ClickEvent.ADD -> add(data)
        }
    }
    private fun setBottomLoginAdapterCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? UserMultiLoginModel.ClickEvent ?: return
        val dataBuff = data[1] as? UserMultiLoginModel ?: return
        when (eventName) {
            UserMultiLoginModel.ClickEvent.THIS -> multiSign(dataBuff)
        }
    }
    private fun setThisItems() {
        val context = contextBuff ?: return
        FStorage.getMultiLoginData(context)?.let {
            dataContext.items.value = it.toMutableList()
        }
    }
    private fun setBottomLoginAdapter() = BottomLoginAdapter(dataContext.relayCommand).also { binding?.rvMultiLogin?.adapter = it }
    private fun add(data: Any?) {
        relayCommand.execute(data)
        dismiss()
    }
    private fun addLoginData() {
        val context = contextBuff ?: return
        FStorage.addMultiLoginData(context, UserMultiLoginModel().apply {
            thisPK = FAmhohwa.getThisPK(context)
            id = FAmhohwa.getTokenID(context)
            name = FAmhohwa.getTokenName(context)
            token = FStorage.getAuthToken(context) ?: ""
            isLogin = true
        })
    }
}