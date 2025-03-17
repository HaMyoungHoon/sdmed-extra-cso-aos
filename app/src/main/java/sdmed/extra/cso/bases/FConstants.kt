package sdmed.extra.cso.bases

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

object FConstants {
    const val APP_NAME = "SDMedExtraCSO"

    const val SHARED_FILE_NAME = "SDMedExtraCSO"
    const val SHARED_CRYPT_FILE_NAME = "SDMedExtraCSOCrypt"

    const val NOTIFICATION_CHANNEL_ID = "notify_channel_sdmed_extra_cso"
    const val NOTIFICATION_CHANNEL_NAME = "NotifyChannelSDMedExtraCSO"

    const val AUTH_TOKEN = "token"
    const val TOKEN_REFRESHING = "tokenRefreshing"
    const val HOME_MENU_INDEX = "homeMenuIndex"
    const val GOOGLE_MAP_STYLE_INDEX = "googleMapStyleIndex"
    const val MULTI_LOGIN_TOKEN = "multiLoginToken"
    const val MEDIA_ITEM = "mediaItem"
    const val MEDIA_LIST = "mediaList"
    const val MEDIA_TARGET_PK = "mediaTargetPK"
    const val MEDIA_MAX_COUNT = "mediaMaxCount"
    const val HOSPITAL_TEMP = "hospitalTemp"
    const val HOSPITAL_PK = "hospitalPK"

    const val GOOGLE_DOC_PREFIX = "https://docs.google.com/viewer?url="
    const val WEB_VIEW_PREFIX = "https://intra-cso.sdmed.co.kr/externalBlobView?blobUrl="

    const val REST_API_DEBUG_RUL = "https://back-cso.sdmed.co.kr"
    const val REST_API_URL = "https://back-cso.sdmed.co.kr"

    const val REST_API_COMMON = "common"
    const val REST_API_MQTT = "mqtt"

    const val REST_API_DASHBOARD = "extra/dashboard"

    const val REST_API_EDI_LIST = "extra/ediList"
    const val REST_API_EDI_REQUEST = "extra/ediRequest"
    const val REST_API_EDI_DUE_DATE = "extra/ediDueDate"
    const val REST_API_HOSPITAL_TEMP = "extra/hospitalTemp"

    const val REST_API_MY_INFO = "extra/myInfo"

    const val REST_API_QNA_LIST = "extra/qnaList"

    const val REST_API_MEDICINE_PRICE_LIST = "extra/medicinePriceList"

    const val CLAIMS_INDEX = "index"
    const val CLAIMS_NAME = "name"
    const val CLAIMS_EXP = "exp"

    const val REGEX_NUMBER_REPLACE = "[^0-9]"
    // 숫자, 문자(영대소한글)
    const val REGEX_CHECK_PASSWORD_0 = "^(?=.*[A-Za-z가-힣ㄱ-ㅎㅏ-ㅣ!@#\$%^&*()])(?=.*\\d)[A-Za-z가-힣ㄱ-ㅎㅏ-ㅣ\\d@\$!@#\$%^&*()]{8,20}\$"
    // 숫자, 특수문자, 문자(영대소한글)
    const val REGEX_CHECK_PASSWORD_1 = "^(?=.*[A-Za-z가-힣ㄱ-ㅎㅏ-ㅣ!@#\$%^&*()])(?=.*\\d)(?=.*[@\$!@#\$%^&*()])[A-Za-z가-힣ㄱ-ㅎㅏ-ㅣ\\d@\$!@#\$%^&*()]{8,20}\$"
    // 숫자, 대문자, 소문자
    const val REGEX_CHECK_PASSWORD_2 = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}\$"

    val LOCATION_PERMISSION = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    val ALARM_PERMISSION = arrayOf(Manifest.permission.SCHEDULE_EXACT_ALARM)
    val NOTIFICATION_PERMISSION = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    val READ_EXTERNAL_PERMISSION_32 = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    val READ_EXTERNAL_PERMISSION_33 = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
    val READ_EXTERNAL_PERMISSION_33 = arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
//    val READ_EXTERNAL_PERMISSION_34 = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
    val READ_EXTERNAL_PERMISSION_34 = arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
    enum class Permit(val index: Int) {
        LOCATION(1),
        ALARM(2),
        LOCATION_WIFI(3),
        READ_EXTERNAL(4),
        CAMERA(5),
        NOTIFICATION(6),
    }
}