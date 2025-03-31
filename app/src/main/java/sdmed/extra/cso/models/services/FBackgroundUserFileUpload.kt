package sdmed.extra.cso.models.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import org.greenrobot.eventbus.EventBus
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import sdmed.extra.cso.R
import sdmed.extra.cso.interfaces.repository.IAzureBlobRepository
import sdmed.extra.cso.interfaces.repository.ICommonRepository
import sdmed.extra.cso.interfaces.repository.IMyInfoRepository
import sdmed.extra.cso.models.common.NotifyIndex
import sdmed.extra.cso.models.common.QueueLockModel
import sdmed.extra.cso.models.common.UserFileAzureQueueModel
import sdmed.extra.cso.models.common.UserFileResultQueueModel
import sdmed.extra.cso.models.common.UserFileSASKeyQueueModel
import sdmed.extra.cso.models.common.UserTrainingFileAzureQueueModel
import sdmed.extra.cso.models.common.UserTrainingFileResultQueueModel
import sdmed.extra.cso.models.common.UserTrainingFileSASKeyQueueModel
import sdmed.extra.cso.models.eventbus.UserFileUploadEvent
import sdmed.extra.cso.models.retrofit.users.UserFileType
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.FImageUtils
import java.util.UUID
import kotlin.getValue

class FBackgroundUserFileUpload(context: Context): Service(), KodeinAware {
    override val kodein: Kodein by kodein(context)
    val context: Context by kodein.instance()
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    private val mqttService: FMqttService by kodein.instance(FMqttService::class)
    private val notificationService: FNotificationService by kodein.instance(FNotificationService::class)
    private val commonRepository: ICommonRepository by kodein.instance(ICommonRepository::class)
    private val azureBlobRepository: IAzureBlobRepository by kodein.instance(IAzureBlobRepository::class)
    private val myInfoRepository: IMyInfoRepository by kodein.instance(IMyInfoRepository::class)

    private val sasKeyQ = QueueLockModel<UserFileSASKeyQueueModel>("sasQ ${FExtensions.getToday().toString("yyyyMMdd_HHmmss")} ${UUID.randomUUID()}")
    private val azureQ = QueueLockModel<UserFileAzureQueueModel>("sasQ ${FExtensions.getToday().toString("yyyyMMdd_HHmmss")} ${UUID.randomUUID()}")
    private val resultQ = QueueLockModel<UserFileResultQueueModel>("sasQ ${FExtensions.getToday().toString("yyyyMMdd_HHmmss")} ${UUID.randomUUID()}")
    private val sasTrainingQ = QueueLockModel<UserTrainingFileSASKeyQueueModel>("sasQ ${FExtensions.getToday().toString("yyyyMMdd_HHmmss")} ${UUID.randomUUID()}")
    private val azureTrainingQ = QueueLockModel<UserTrainingFileAzureQueueModel>("sasQ ${FExtensions.getToday().toString("yyyyMMdd_HHmmss")} ${UUID.randomUUID()}")
    private val resultTrainingQ = QueueLockModel<UserTrainingFileResultQueueModel>("sasQ ${FExtensions.getToday().toString("yyyyMMdd_HHmmss")} ${UUID.randomUUID()}")

    private var resultQRun = false
    private var resultTrainingQRun = false

    fun sasKeyEnqueue(data: UserFileSASKeyQueueModel) = sasKeyQ.enqueue(data, true, { sasKeyThreadStart() })
    fun sasKeyEnqueue(data: UserTrainingFileSASKeyQueueModel) = sasTrainingQ.enqueue(data, true, { sasKeyTrainingThreadStart() })
    private fun azureEnqueue(data: UserFileAzureQueueModel) = azureQ.enqueue(data, true, { azureThreadStart() })
    private fun azureEnqueue(data: UserTrainingFileAzureQueueModel) = azureTrainingQ.enqueue(data, true, { azureTrainingThreadStart() })
    private fun resultEnqueue(data: UserFileResultQueueModel) {
        resultQ.locking()
        val findBuff = resultQ.findQ(false, { it.uuid == data.uuid })
        if (findBuff == null) {
            resultQ.enqueue(data, false)
        } else {
            findBuff.appendItemPath(data.currentMedia, data.itemIndex)
        }
        resultThreadStart(resultQRun)
        resultQRun = true
        resultQ.unlocking()
    }
    private fun resultEnqueue(data: UserTrainingFileResultQueueModel) {
        resultTrainingQ.locking()
        val findBuff = resultTrainingQ.findQ(false, { it.uuid == data.uuid })
        if (findBuff == null) {
            resultTrainingQ.enqueue(data, false)
        } else {
            findBuff.setThis(data)
        }
        resultTrainingThreadStart(resultTrainingQRun)
        resultTrainingQRun = true
        resultTrainingQ.unlocking()
    }
    private fun resultDequeue(): UserFileResultQueueModel {
        resultQ.locking()
        val ret: UserFileResultQueueModel
        val retBuff = resultQ.findQ(false, { it.readyToSend() })
        if (retBuff == null) {
            ret = UserFileResultQueueModel("-1")
        } else {
            ret = retBuff
            resultQ.removeQ(ret, false)
        }
        resultQ.unlocking()
        return ret
    }
    private fun resultTrainingDequeue(): UserTrainingFileResultQueueModel {
        resultTrainingQ.locking()
        val ret: UserTrainingFileResultQueueModel
        val retBuff = resultTrainingQ.findQ(false, { it.readyToSend() })
        if (retBuff == null) {
            ret = UserTrainingFileResultQueueModel("-1")
        } else {
            ret = retBuff
            resultTrainingQ.removeQ(ret, false)
        }
        resultTrainingQ.unlocking()
        return ret
    }
    private fun sasKeyThreadStart() = sasKeyQ.threadStart {
        checkSASKeyQ(sasKeyQ.dequeue())
        Thread.sleep(100)
    }
    private fun azureThreadStart() = azureQ.threadStart {
        checkAzureQ(azureQ.dequeue())
        Thread.sleep(100)
    }
    private fun resultThreadStart(resultQRun: Boolean) = resultQ.threadStart({
        while (resultQ.isNotEmpty()) {
            postResultData(resultDequeue())
            Thread.sleep(100)
        }
        this.resultQRun = false
    }, resultQRun)
    private fun sasKeyTrainingThreadStart() = sasTrainingQ.threadStart {
        checkSASKeyQ(sasTrainingQ.dequeue())
        Thread.sleep(100)
    }
    private fun azureTrainingThreadStart() = azureTrainingQ.threadStart {
        checkAzureQ(azureTrainingQ.dequeue())
        Thread.sleep(100)
    }
    private fun resultTrainingThreadStart(resultQRun: Boolean) = resultTrainingQ.threadStart({
        while (resultTrainingQ.isNotEmpty()) {
            postResultData(resultTrainingDequeue())
            Thread.sleep(100)
        }
        this.resultQRun = false
    }, resultQRun)

    private fun checkSASKeyQ(data: UserFileSASKeyQueueModel) {
        FCoroutineUtil.coroutineScope({
            val blobName = data.blobName(context)
            val ret = commonRepository.postGenerateSasList(blobName.map { it.second })
            if (ret.result != true || ret.data == null) {
                notificationService.sendNotify(context, NotifyIndex.QNA_UPLOAD, context.getString(R.string.user_file_upload_fail), ret.msg ?: "")
                return@coroutineScope
            }
            val uuid = UUID.randomUUID().toString()
            resultEnqueue(UserFileResultQueueModel(uuid, itemIndex = -1, itemCount = data.medias.size, mediaTypeIndex = data.mediaTypeIndex))
            ret.data?.forEachIndexed { index, x ->
                val queue = UserFileAzureQueueModel(uuid, mediaTypeIndex = index).parse(data, blobName, x) ?: return@forEachIndexed
                azureEnqueue(queue)
            }
            progressNotificationCall(uuid)
        })
    }
    private fun checkAzureQ(data: UserFileAzureQueueModel) {
        FCoroutineUtil.coroutineScope({
            data.media.mediaPath?.let { uri ->
                try {
                    val cachedFile = FImageUtils.uriToFile(context, uri, data.media.mediaName)
                    val ret = azureBlobRepository.upload(data.userFileModel.blobUrlKey(), cachedFile, data.userFileModel.mimeType)
                    FImageUtils.fileDelete(context, cachedFile)
                    if (ret.isSuccessful) {
                        resultEnqueue(UserFileResultQueueModel(data.uuid, data.userFileModel, data.mediaTypeIndex, mediaTypeIndex = data.mediaTypeIndex))
                    } else {
                        progressNotificationCall(data.uuid, true)
                        notificationCall(context.getString(R.string.user_file_upload_fail))
                    }
                } catch (_: Exception) {
                    progressNotificationCall(data.uuid, true)
                    notificationCall(context.getString(R.string.user_file_upload_fail))
                }
            }
        })
    }
    private fun postResultData(data: UserFileResultQueueModel) {
        if (data.uuid == "-1") {
            return
        }
        FCoroutineUtil.coroutineScope({
            val thisPK = FAmhohwa.getThisPK(context)
            val ret = myInfoRepository.putUserFileImageUrl(thisPK, data.parseBlobUploadModel(), data.userFileType())
            if (ret.result == true) {
                notificationCall(context.getString(R.string.user_file_upload_comp), thisPK = thisPK)
                mqttService.mqttUserFileAdd(thisPK, getUserFileContent(data.userFileType()))
            } else {
                notificationCall(context.getString(R.string.user_file_upload_fail), ret.msg)
            }
            progressNotificationCall(data.uuid, true)
        })
    }
    private fun getUserFileContent(userFileType: UserFileType) = when (userFileType) {
        UserFileType.Taxpayer -> context.getString(R.string.mqtt_title_taxpaayer_add)
        UserFileType.BankAccount -> context.getString(R.string.mqtt_title_taxpaayer_add)
        UserFileType.CsoReport -> context.getString(R.string.mqtt_title_taxpaayer_add)
        UserFileType.MarketingContract -> context.getString(R.string.mqtt_title_taxpaayer_add)
    }
    private fun checkSASKeyQ(data: UserTrainingFileSASKeyQueueModel) {
        FCoroutineUtil.coroutineScope({
            val blobName = data.blobName(context)
            val ret = commonRepository.getGenerateSas(blobName.second)
            if (ret.result != true || ret.data == null) {
                notificationService.sendNotify(context, NotifyIndex.QNA_UPLOAD, context.getString(R.string.user_file_upload_fail), ret.msg ?: "")
                return@coroutineScope
            }
            val uuid = UUID.randomUUID().toString()
            resultEnqueue(UserTrainingFileResultQueueModel(uuid))
            ret.data?.let { x ->
                val queue = UserTrainingFileAzureQueueModel(uuid, trainingDate = data.trainingDate).parse(data, x) ?: return@let
                azureEnqueue(queue)
            }
            progressNotificationCall(uuid)
        })
    }
    private fun checkAzureQ(data: UserTrainingFileAzureQueueModel) {
        FCoroutineUtil.coroutineScope({
            data.media.mediaPath?.let { uri ->
                try {
                    val cachedFile = FImageUtils.uriToFile(context, uri, data.media.mediaName)
                    val ret = azureBlobRepository.upload(data.userFileModel.blobUrlKey(), cachedFile, data.userFileModel.mimeType)
                    FImageUtils.fileDelete(context, cachedFile)
                    if (ret.isSuccessful) {
                        resultEnqueue(UserTrainingFileResultQueueModel(data.uuid, data.userFileModel, data.trainingDate))
                    } else {
                        progressNotificationCall(data.uuid, true)
                        notificationCall(context.getString(R.string.user_file_upload_fail))
                    }
                } catch (_: Exception) {
                    progressNotificationCall(data.uuid, true)
                    notificationCall(context.getString(R.string.user_file_upload_fail))
                }
            }
        })
    }
    private fun postResultData(data: UserTrainingFileResultQueueModel) {
        if (data.uuid == "-1") {
            return
        }
        FCoroutineUtil.coroutineScope({
            val thisPK = FAmhohwa.getThisPK(context)
            val ret = myInfoRepository.postUserTrainingData(thisPK,  data.trainingDate, data.medias)
            if (ret.result == true) {
                notificationCall(context.getString(R.string.user_file_upload_comp), thisPK = thisPK)
                mqttService.mqttUserFileAdd(thisPK, context.getString(R.string.mqtt_title_training_certificate_add))
            } else {
                notificationCall(context.getString(R.string.user_file_upload_fail), ret.msg)
            }
            progressNotificationCall(data.uuid, true)
        })
    }

    private fun notificationCall(title: String, message: String? = null, thisPK: String = "") {
        notificationService.sendNotify(context, NotifyIndex.USER_FILE_UPLOAD, title, message, FNotificationService.NotifyType.WITH_VIBRATE, true, thisPK)
        EventBus.getDefault().post(UserFileUploadEvent(thisPK))
    }
    private fun progressNotificationCall(uuid: String, isCancel: Boolean = false) {
        if (isCancel) {
            notificationService.progressUpdate(context, uuid, isCancel = true)
        } else {
            val title = context.getString(R.string.user_file_upload)
            notificationService.makeProgressNotify(context, uuid, title)
        }
    }
}