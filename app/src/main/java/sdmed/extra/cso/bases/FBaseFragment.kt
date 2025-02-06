package sdmed.extra.cso.bases

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.multidex.MultiDexApplication
import sdmed.extra.cso.models.retrofit.users.UserRole
import sdmed.extra.cso.models.retrofit.users.UserRole.Companion.getFlag
import sdmed.extra.cso.models.retrofit.users.UserRoles
import sdmed.extra.cso.models.retrofit.users.UserStatus
import sdmed.extra.cso.models.services.FUIStateService

abstract class FBaseFragment<T1: ViewDataBinding, T2: FBaseViewModel>(val needRoles: UserRoles = UserRole.None.toS()): Fragment() {
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
    protected var singlePermissionResult: ActivityResultLauncher<String>? = null
        private set
    protected var multiPermissionResult: ActivityResultLauncher<Array<String>>? = null
        private set
    protected var myState: UserStatus = UserStatus.None
        private set
    protected var haveRole: Boolean = false
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding?.lifecycleOwner = viewLifecycleOwner
        initPermissionResult()
        onBindAfter()
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreateAfter()
    }
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
    private fun onViewCreateAfter() {
        loading()
        stateCheck()
        if (myState != UserStatus.Live) {
            loading(false)
            return
        }
        roleCheck()
        if (!haveRole) {
            loading(false)
            return
        }
        loading(false)

        viewInit()
    }

    open fun onBindAfter() { }
    open fun viewInit() { }
    open fun onAfterAttach() { }
    open fun onAfterDetach() { }

    private fun stateCheck() {
        dataContext.getMyState {
            if (it.result == true) {
                myState = it.data ?: UserStatus.None
            } else {
                toast(it.msg)
            }
        }
    }
    private fun roleCheck() {
        if (needRoles.getFlag() == 0) {
            haveRole = true
            return
        }
        dataContext.getMyRole {
            if (it.result == true) {
                haveRole = ((it.data ?: 0) and needRoles.getFlag()) != 0
            } else {
                haveRole = false
                toast(it.msg)
            }
        }
    }
    private fun initPermissionResult() {
        singlePermissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) { x -> onSinglePermission(x) }
        multiPermissionResult = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { x -> onMultiPermission(x) }
    }
    open fun onSinglePermission(data: Boolean) { }
    open fun onMultiPermission(data: Map<String, Boolean>) { }

    protected fun toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) = toast(resources.getString(resId), duration)
    protected fun toast(message: String?, duration: Int = Toast.LENGTH_SHORT) = uiStateService.toast(contextBuff, message, duration)
    protected fun loading(message: String = "", isVisible: Boolean = true) = uiStateService.loading(contextBuff, message, isVisible)
    protected fun loading(isVisible: Boolean = true) = uiStateService.loading(contextBuff, "", isVisible)
    protected fun getResString(@StringRes resId: Int) = contextBuff?.getString(resId) ?: ""
    protected fun getResColor(@ColorRes resId: Int) = contextBuff?.getColor(resId) ?: 0

    protected fun shouldShowRequestPermissionRationale(permissions: Array<String>) = permissions.any { shouldShowRequestPermissionRationale(it) }
    protected fun hasPermissionsGranted(permissions: Array<String>) = permissions.none {
        val context = contextBuff ?: return false
        ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
    }
    protected fun requestSinglePermissions(permission: String) {
        if (!shouldShowRequestPermissionRationale(permission)) {
            singlePermissionResult?.launch(permission)
        }
    }
    protected fun requestMultiPermissions(permissions: Array<String>) {
        if (!shouldShowRequestPermissionRationale(permissions)) {
            multiPermissionResult?.launch(permissions)
        }
    }
}