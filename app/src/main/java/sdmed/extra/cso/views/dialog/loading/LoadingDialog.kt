package sdmed.extra.cso.views.dialog.loading

import android.content.Context
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseDialog
import sdmed.extra.cso.databinding.LoadingDialogBinding
import androidx.core.graphics.drawable.toDrawable

class LoadingDialog(context: Context, private val msg: String = "", private val alpha: Float = 0F): FBaseDialog<LoadingDialogBinding, LoadingDialogVM>(context) {
    override var layoutId = R.layout.loading_dialog
    override val dataContext: LoadingDialogVM by lazy {
        LoadingDialogVM(multiDexApplication)
    }

    override fun onCreateAfter() {
        setCancelable(false)
        window?.setDimAmount(alpha)
        window?.setBackgroundDrawable(getResColor(R.color.transparent).toDrawable())
        binding?.dataContext = dataContext
        dataContext.msg.value = msg
    }
}