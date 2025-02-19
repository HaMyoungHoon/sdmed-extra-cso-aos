package sdmed.extra.cso.views.dialog.message

import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseDialogFragment
import sdmed.extra.cso.databinding.MessageDialogBinding
import sdmed.extra.cso.interfaces.command.IAsyncEventListener

class MessageDialog(
    val eventListener: IAsyncEventListener,
    val title: String,
    val leftBtnText: String,
    val rightBtnText: String,
    val isCancel: Boolean = true,
    val leftBtnStyle: List<Pair<String, Any>>? = null,
    val rightBtnStyle: List<Pair<String, Any>>? = null,
): FBaseDialogFragment<MessageDialogBinding, MessageDialogVM>() {
    override var layoutId = R.layout.message_dialog
    override val dataContext: MessageDialogVM by lazy {
        MessageDialogVM(multiDexApplication)
    }

    override fun onCreateAfter() {
        super.onCreateAfter()
        setStyle(STYLE_NO_TITLE, R.style.TransparentDialog)
    }

    override fun viewInit() {
        binding?.dataContext = dataContext
        super.viewInit()
        dataContext.title.value = title
        dataContext.leftBtnText.value = leftBtnText
        dataContext.rightBtnText.value = rightBtnText
        dataContext.leftBtnVisible.value = leftBtnText.isNotBlank()
        dataContext.rightBtnVisible.value = rightBtnText.isNotBlank()
        if (!isCancel) isCancelable = isCancel
        buttonStyleSet()
    }
    override fun setEventListener() {
        dataContext.addEventListener(object: IAsyncEventListener {
            override suspend fun onEvent(data: Any?) {
                eventListener.onEvent(data)
                dismiss()
            }
        })
    }
    private fun buttonStyleSet() {
        dataContext.leftBtnTextColor.value = getResColor(R.color.def_foreground)
        dataContext.rightBtnTextColor.value = getResColor(R.color.def_foreground)
        if (!leftBtnStyle.isNullOrEmpty()) {
            val textColor = leftBtnStyle.find { it.first == "textColor" }
            if (textColor != null && textColor.second is Int) {
                dataContext.leftBtnTextColor.value = getResColor(textColor.second as Int)
            }
        }
        if (!rightBtnStyle.isNullOrEmpty()) {
            val textColor = rightBtnStyle.find { it.first == "textColor" }
            if (textColor != null && textColor.second is Int) {
                dataContext.rightBtnTextColor.value = getResColor(textColor.second as Int)
            }
        }
    }
}