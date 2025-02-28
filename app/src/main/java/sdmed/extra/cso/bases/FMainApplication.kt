package sdmed.extra.cso.bases

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import sdmed.extra.cso.interfaces.repository.IAzureBlobRepository
import sdmed.extra.cso.interfaces.repository.ICommonRepository
import sdmed.extra.cso.interfaces.repository.IEDIDueDateRepository
import sdmed.extra.cso.interfaces.repository.IEDIListRepository
import sdmed.extra.cso.interfaces.repository.IEDIRequestRepository
import sdmed.extra.cso.interfaces.repository.IMedicinePriceListRepository
import sdmed.extra.cso.interfaces.repository.IMqttRepository
import sdmed.extra.cso.interfaces.repository.IMyInfoRepository
import sdmed.extra.cso.interfaces.repository.IQnAListRepository
import sdmed.extra.cso.interfaces.services.IAzureBlobService
import sdmed.extra.cso.interfaces.services.ICommonService
import sdmed.extra.cso.interfaces.services.IEDIDueDateService
import sdmed.extra.cso.interfaces.services.IEDIListService
import sdmed.extra.cso.interfaces.services.IEDIRequestService
import sdmed.extra.cso.interfaces.services.IMedicinePriceListService
import sdmed.extra.cso.interfaces.services.IMqttService
import sdmed.extra.cso.interfaces.services.IMyInfoService
import sdmed.extra.cso.interfaces.services.IQnAListService
import sdmed.extra.cso.models.repository.AzureBlobRepository
import sdmed.extra.cso.models.repository.CommonRepository
import sdmed.extra.cso.models.repository.EDIDueDateRepository
import sdmed.extra.cso.models.repository.EDIListRepository
import sdmed.extra.cso.models.repository.EDIRequestRepository
import sdmed.extra.cso.models.repository.MedicinePriceListRepository
import sdmed.extra.cso.models.repository.MqttRepository
import sdmed.extra.cso.models.repository.MyInfoRepository
import sdmed.extra.cso.models.repository.QnAListRepository
import sdmed.extra.cso.models.services.FBackgroundEDIFileUpload
import sdmed.extra.cso.models.services.FBackgroundEDIRequestNewUpload
import sdmed.extra.cso.models.services.FBackgroundEDIRequestUpload
import sdmed.extra.cso.models.services.FBackgroundQnAUpload
import sdmed.extra.cso.models.services.FMqttService
import sdmed.extra.cso.models.services.FNotificationService
import sdmed.extra.cso.models.services.ForcedTerminationService
import sdmed.extra.cso.models.services.RetrofitService
import sdmed.extra.cso.utils.FThemeUtil

class FMainApplication: MultiDexApplication(), LifecycleEventObserver, KodeinAware {
    companion object {
        var isForeground = false
        private var _ins: FMainApplication? = null
        val ins: FMainApplication get() {
            if (_ins == null) {
                _ins = FMainApplication()
            }
            return _ins!!
        }
    }
    override val kodein = Kodein.direct {
        import(androidXModule(this@FMainApplication))

        bind<FNotificationService>(FNotificationService::class) with singleton { FNotificationService(applicationContext) }
        bind<FBackgroundEDIRequestUpload>(FBackgroundEDIRequestUpload::class) with singleton { FBackgroundEDIRequestUpload(applicationContext) }
        bind<FBackgroundEDIRequestNewUpload>(FBackgroundEDIRequestNewUpload::class) with singleton { FBackgroundEDIRequestNewUpload(applicationContext) }
        bind<FBackgroundEDIFileUpload>(FBackgroundEDIFileUpload::class) with singleton { FBackgroundEDIFileUpload(applicationContext) }
        bind<FBackgroundQnAUpload>(FBackgroundQnAUpload::class) with singleton { FBackgroundQnAUpload(applicationContext) }
        bind<FMqttService>(FMqttService::class) with singleton { FMqttService(applicationContext) }

        bind<IAzureBlobRepository>(IAzureBlobRepository::class) with singleton { AzureBlobRepository(RetrofitService.create(IAzureBlobService::class.java)) }
        bind<ICommonRepository>(ICommonRepository::class) with singleton { CommonRepository(RetrofitService.create(ICommonService::class.java)) }
        bind<IMqttRepository>(IMqttRepository::class) with singleton { MqttRepository(RetrofitService.create(IMqttService::class.java)) }
        bind<IEDIListRepository>(IEDIListRepository::class) with singleton { EDIListRepository(RetrofitService.create(IEDIListService::class.java)) }
        bind<IEDIDueDateRepository>(IEDIDueDateRepository::class) with singleton { EDIDueDateRepository(RetrofitService.create(IEDIDueDateService::class.java)) }
        bind<IEDIRequestRepository>(IEDIRequestRepository::class) with singleton { EDIRequestRepository(RetrofitService.create(IEDIRequestService::class.java)) }
        bind<IMedicinePriceListRepository>(IMedicinePriceListRepository::class) with singleton { MedicinePriceListRepository(RetrofitService.create(IMedicinePriceListService::class.java)) }
        bind<IQnAListRepository>(IQnAListRepository::class) with singleton { QnAListRepository(RetrofitService.create(IQnAListService::class.java)) }
        bind<IMyInfoRepository>(IMyInfoRepository::class) with singleton { MyInfoRepository(RetrofitService.create(IMyInfoService::class.java)) }
    }.kodein

    fun isDebug() = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    fun getVersionCode(flags: Int = 0): Long {
        val pInfo = getPackageInfo(flags)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pInfo?.longVersionCode ?: 0
        } else {
            @Suppress("DEPRECATION") (pInfo?.versionCode ?: 0).toLong()
        }
    }
    fun getVersionName(flags: Int = 0): Long {
        val versionName = getVersionNameString(flags)
        return if (versionName.isEmpty()) {
            0L
        } else {
            versionName.replace(".","").toLong()
        }
    }
    fun getVersionNameString(flags: Int = 0): String {
        val pInfo = getPackageInfo(flags)
        return pInfo?.versionName ?: ""
    }
    fun getPackageInfo(flags: Int = 0): PackageInfo? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
            } else {
                packageManager.getPackageInfo(packageName, flags)
            }
        } catch (e: Exception) {
            return null
        }
    }
    fun getApplicationID() = applicationInfo.processName.toString()
    fun getSignatureArray(): Array<Signature>? {
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            PackageManager.GET_SIGNING_CERTIFICATES
        } else {
            @Suppress("DEPRECATION") PackageManager.GET_SIGNATURES
        }
        val pInfo = getPackageInfo(flag) ?: return null
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pInfo.signingInfo?.apkContentsSigners
            } else {
                @Suppress("DEPRECATION") pInfo.signatures
            }
        } catch (_: Exception) {
            return null
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    override fun onCreate() {
        super.onCreate()
        _ins = this
        FThemeUtil.applyTheme()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        try {
            startService(Intent(this, ForcedTerminationService::class.java))
        } catch (_: Exception) { }
    }
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_START) {
            isForeground = true
        } else if (event == Lifecycle.Event.ON_STOP) {
            isForeground = false
        }
    }
}