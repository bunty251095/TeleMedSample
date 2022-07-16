package com.vivant.telemedicine.network.utils

import android.content.Context
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vivant.telemedicine.R
import com.vivant.telemedicine.common.Utilities
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.UnknownHostException
import kotlin.coroutines.coroutineContext

abstract class NetworkDataBoundResource<ResultType, RequestType>(val context: Context) {

    private val localResource = context
    //private val localResource = LocaleHelper.getLocalizedResources(context, Locale(LocaleHelper.getLanguage(context)!!))!!

    private val result = MutableLiveData<Resource<ResultType>>()
    private val supervisorJob = SupervisorJob()

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    suspend fun build(): NetworkDataBoundResource<ResultType, RequestType> {
        withContext(Dispatchers.Main) {
            result.value = Resource.loading(null)
        }
        CoroutineScope(coroutineContext).launch(supervisorJob) {

            try {
                fetchFromNetwork()
            }catch (e: UnknownHostException){
                Utilities.printLogError("An error happened: $e")
                setValue(Resource.error(e,null, errorMessage = localResource.getString(R.string.ERROR_INTERNET_UNAVAILABLE)))
                //setValue(Resource.error(e,null,errorMessage = "Seems like you are offline. Please check your internet connection and try again."))
            } catch (e: Exception) {
                Utilities.printLogError("An error happened: $e")
                setValue(Resource.error(e, null, errorMessage = localResource.getString(R.string.SOMETHING_WENT_WRONG)))
                //setValue(Resource.error(e, null,errorMessage = "Something went wrong."))
            }

        }
        return this
    }

    private suspend fun fetchFromNetwork() {

        val apiResponse = createCallAsync()
        Utilities.printLogError("Data fetched from network")
        setValue(Resource.success(processResponse(apiResponse)))
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) result.postValue(newValue)
    }

    @WorkerThread
    protected abstract fun processResponse(response: RequestType): ResultType

    @MainThread
    protected abstract suspend fun createCallAsync(): RequestType
}