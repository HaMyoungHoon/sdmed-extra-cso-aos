package sdmed.extra.cso.bases

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.multidex.MultiDexApplication

abstract class FBaseDialog<T1: ViewDataBinding, T2: FBaseViewModel>(private val motherContext: Context): Dialog(motherContext) {
    protected abstract var layoutId: Int
    protected var binding: T1? = null
    protected abstract val dataContext: T2
    val multiDexApplication by lazy {
        motherContext.applicationContext as MultiDexApplication
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, layoutId, null, false)
        binding?.lifecycleOwner = (motherContext as? LifecycleOwner)
        setContentView(layoutId)
        onCreateAfter()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onAfterAttach()
    }
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding = null
        onAfterDetach()
    }
    override fun dismiss() {
        super.dismiss()
        binding = null
    }
    open fun onCreateAfter() { }
    open fun onAfterAttach() { }
    open fun onAfterDetach() { }

    protected fun getResString(@StringRes resId: Int) = motherContext.getString(resId)
    protected fun getResColor(@ColorRes resId: Int) = motherContext.getColor(resId)
}