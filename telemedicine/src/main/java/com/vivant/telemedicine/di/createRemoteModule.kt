package com.vivant.telemedicine.network

import com.google.gson.GsonBuilder
import com.vivant.telemedicine.common.Constants
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.network.datasource.AppointmentDetailsDatasource
import com.vivant.telemedicine.network.datasource.ConsultAndScheduleDatasource
import com.vivant.telemedicine.network.datasource.HomeDatasource
import com.vivant.telemedicine.network.interceptor.DecryptInterceptor
import com.vivant.telemedicine.network.interceptor.DecryptionInterceptor
import com.vivant.telemedicine.network.interceptor.EncryptInterceptor
//import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

private const val DEFAULT = "DEFAULT"
private const val ENCRYPTED = "ENCRYPTED"

fun createRemoteModule() = module {

    factory<Interceptor> {

        HttpLoggingInterceptor { message ->
            Utilities.printLog("HttpLogging==>$message")
        }.setLevel(HttpLoggingInterceptor.Level.BODY)

    }

    val logging = HttpLoggingInterceptor { message ->
        Utilities.printLog("HttpLogging==>$message")
    }.setLevel(HttpLoggingInterceptor.Level.BODY)

    factory {
        OkHttpClient.Builder()
            .protocols(Arrays.asList(Protocol.HTTP_1_1))
            .connectTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            //.addInterceptor(get())
            .addInterceptor(logging)
            .addInterceptor(DecryptionInterceptor())
            .build()
    }

    single(qualifier = named(ENCRYPTED)) {
        Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .protocols(Arrays.asList(Protocol.HTTP_1_1))
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .writeTimeout(3, TimeUnit.MINUTES)
                    .readTimeout(3, TimeUnit.MINUTES)
                    .addNetworkInterceptor(EncryptInterceptor())
                    //.addInterceptor(get())
                    .addInterceptor(logging)
                    .addInterceptor(DecryptInterceptor())
                    .build()
            )
            .baseUrl(Constants.strBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            //.addConverterFactory(ScalarsConverterFactory.create())
            .build()
            //.create(ApiService::class.java)
    }

    single(qualifier = named(DEFAULT)) {
        Retrofit.Builder()
            .client(get())
            .baseUrl(Constants.strBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .addConverterFactory(ScalarsConverterFactory.create())
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            //.create(ApiService::class.java)
    }

    //single(qualifier = named(DEFAULT)) { get<Retrofit>(DEFAULT).create(ApiService::class.java) }
    //single(qualifier = named(ENCRYPTED)) { get<Retrofit>(ENCRYPTED).create(ApiService::class.java) }

    single(qualifier = named(DEFAULT)) {
        provideApiService(get(qualifier = named(DEFAULT)))
    }
    single(qualifier = named(ENCRYPTED)) {
        provideApiService(get(qualifier = named(ENCRYPTED)))
    }

    factory { HomeDatasource(get(qualifier = named(DEFAULT)), get(qualifier = named(ENCRYPTED))) }
    factory { ConsultAndScheduleDatasource(get(qualifier = named(DEFAULT)), get(qualifier = named(ENCRYPTED))) }
    factory { AppointmentDetailsDatasource(get(qualifier = named(DEFAULT)), get(qualifier = named(ENCRYPTED))) }

}

fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)