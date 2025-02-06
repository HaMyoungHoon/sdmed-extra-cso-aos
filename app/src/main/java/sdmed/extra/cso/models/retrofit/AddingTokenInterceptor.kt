package sdmed.extra.cso.models.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import sdmed.extra.cso.bases.FConstants

class AddingTokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newBuilder = chain.request().newBuilder()
        val accessKey = FRetrofitVariable.token

        return if (!accessKey.isNullOrEmpty()) {
            chain.proceed(newBuilder
                .addHeader(FConstants.AUTH_TOKEN, accessKey)
                .build())
        } else {
            chain.proceed(newBuilder.build())
        }
    }
}