package com.soe.superbill.data.network


import com.soe.superbill.CoockieInterceptor
import com.soe.superbill.Globals
import okhttp3.Interceptor
import okhttp3.Interceptor.Companion.invoke
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.util.concurrent.TimeUnit


object NetworkService {
    private val loggingInterceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val baseInterceptor: Interceptor = invoke { chain ->
        val newUrl = chain
                .request()
                .url
                .newBuilder()
                .build()

        val request = chain
                .request()
                .newBuilder()
                .url(newUrl)
                .build()

        return@invoke chain.proceed(request)
    }

    private val client: OkHttpClient = OkHttpClient
            .Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)
            .writeTimeout(12, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(baseInterceptor)
            .addInterceptor(CoockieInterceptor())
            .cookieJar(JavaNetCookieJar(CookieManager()))
            .build()



    fun retrofitService(): ApiMain {
        return Retrofit.Builder()
                .baseUrl(Globals.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(ApiMain::class.java)
    }
}