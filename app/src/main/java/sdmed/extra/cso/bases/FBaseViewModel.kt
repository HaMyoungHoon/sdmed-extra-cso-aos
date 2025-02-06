package sdmed.extra.cso.bases

import androidx.lifecycle.AndroidViewModel
import androidx.multidex.MultiDexApplication
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import sdmed.extra.cso.interfaces.command.IAsyncEventListener
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.interfaces.repository.ICommonRepository
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.command.AsyncRelayCommand
import sdmed.extra.cso.models.retrofit.users.UserStatus
import sdmed.extra.cso.utils.FCoroutineUtil

abstract class FBaseViewModel(application: MultiDexApplication): AndroidViewModel(application), KodeinAware {
    final override val kodein: Kodein by kodein(application)
    protected val commonRepository: ICommonRepository by kodein.instance(ICommonRepository::class)

    fun tokenRefresh(ret: (RestResultT<String>) -> Unit) {
        FCoroutineUtil.coroutineScope({
            commonRepository.tokenRefresh()
        }, { ret(it) })
    }
    fun getMyState(ret: (RestResultT<UserStatus>) -> Unit) {
        FCoroutineUtil.coroutineScope({
            commonRepository.getMyState()
        }, { ret(it) })
    }
    fun getMyRole(ret: (RestResultT<Int>) -> Unit) {
        FCoroutineUtil.coroutineScope({
            commonRepository.getMyRole()
        }, { ret(it) })
    }

    val relayCommand: ICommand = AsyncRelayCommand({})
    fun addEventListener(listener: IAsyncEventListener) {
        (relayCommand as AsyncRelayCommand).addEventListener(listener)
    }
}