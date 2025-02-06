package sdmed.extra.cso.bases

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.multidex.MultiDexApplication
import sdmed.extra.cso.R
import sdmed.extra.cso.models.services.FUIStateService

abstract class FBaseDialogFragment<T1: ViewDataBinding, T2: FBaseViewModel>: DialogFragment() {
    protected abstract var layoutId: Int
    protected var binding: T1? = null
    protected abstract val dataContext: T2
    var isAttached = false
        private set
    var contextBuff: Context? = null
        private set
    private val uiStateService: FUIStateService by lazy {
        FUIStateService()
    }
    val multiDexApplication by lazy {
        requireActivity().application as MultiDexApplication
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateAfter()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding?.lifecycleOwner = viewLifecycleOwner
        onBindAfter()
        return binding?.root
    }
    override fun getTheme() = R.style.base_dialog
    override fun onAttach(context: Context) {
        super.onAttach(context)
        contextBuff = context
        isAttached = true
        onAfterAttach()
    }
    override fun onDetach() {
        isAttached = false
        super.onDetach()
        contextBuff = null
        onAfterDetach()
    }
    override fun onDestroy() {
        super.onDestroy()
        loading(false)
        binding = null
    }

    open fun onCreateAfter() { }
    open fun onBindAfter() { }
    open fun onAfterAttach() { }
    open fun onAfterDetach() { }

    protected fun toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) = toast(resources.getString(resId), duration)
    protected fun toast(message: String?, duration: Int = Toast.LENGTH_SHORT) = uiStateService.toast(contextBuff, message, duration)
    protected fun loading(message: String = "", isVisible: Boolean = true) = uiStateService.loading(contextBuff, message, isVisible)
    protected fun loading(isVisible: Boolean = true) = uiStateService.loading(contextBuff, "", isVisible)
    protected fun getResString(@StringRes resId: Int) = contextBuff?.getString(resId) ?: ""
    protected fun getResColor(@ColorRes resId: Int) = contextBuff?.getColor(resId) ?: 0
}