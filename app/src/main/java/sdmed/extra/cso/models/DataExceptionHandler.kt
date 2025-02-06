package sdmed.extra.cso.models

import com.google.gson.Gson
import retrofit2.HttpException
import sdmed.extra.cso.interfaces.IRestResult
import java.io.IOException

class DataExceptionHandler {
    companion object {
        fun <T> handleExceptionT(exception: Exception): IRestResult {
            return when (exception) {
                is IOException -> RestResult().setFail(msg = exception.localizedMessage)
                is HttpException -> {
                    val errString = exception.response()?.errorBody()?.string()
                    if (errString.isNullOrEmpty()) {
                        RestResultT<T>().setFail(exception.code(), exception.message)
                    } else {
                        Gson().fromJson(errString, RestResultT::class.java)
                    }
                }
                else -> RestResultT<T>().setFail(msg = exception.localizedMessage)
            }
        }
        fun handleException(exception: Exception): IRestResult {
            return when (exception) {
                is IOException -> RestResult().setFail(msg = exception.localizedMessage)
                is HttpException -> {
                    val errString = exception.response()?.errorBody()?.string()
                    if (errString.isNullOrEmpty()) {
                        RestResult().setFail(exception.code(), exception.message)
                    } else {
                        Gson().fromJson(errString, RestResult::class.java)
                    }
                }
                else -> RestResult().setFail(msg = exception.localizedMessage)
            }
        }
        fun <T> handleThrowableT(throwable: Throwable?): IRestResult {
            return when (throwable) {
                null -> RestResultT<T>().setFail()
                else -> {
                    RestResultT<T>().setFail(msg = throwable.localizedMessage)
                }
            }
        }
        fun handleThrowable(throwable: Throwable?): IRestResult {
            return when (throwable) {
                null -> RestResult().setFail()
                else -> {
                    RestResult().setFail(msg = throwable.localizedMessage)
                }
            }
        }
    }
}