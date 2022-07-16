package com.vivant.telemedicine.network.utils

import android.content.Context
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vivant.telemedicine.R
import com.google.gson.JsonSyntaxException
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.model.base.ApiResponse
import com.vivant.telemedicine.model.base.BaseResponse
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.UnknownHostException
import kotlin.coroutines.coroutineContext

abstract class NetworkBoundResource<ResultType, RequestType>(val context: Context) {

    //private val localResource = LocaleHelper.getLocalizedResources(context, Locale(LocaleHelper.getLanguage(context)!!))!!
    private val localResource = context
    private val utilities = Utilities

    private val result = MutableLiveData<Resource<ResultType>>()
    private val supervisorJob = SupervisorJob()

    suspend fun build(): NetworkBoundResource<ResultType, RequestType> {
        withContext(Dispatchers.Main) {
            result.value = Resource.loading(null)
        }
        CoroutineScope(coroutineContext).launch(supervisorJob) {
            val dbResult = loadFromDb()
            if (shouldFetch(dbResult)) {
                try {
                    fetchFromNetwork(dbResult)
                } catch (e: UnknownHostException){
                    utilities.printLogError("An error happened: $e")
                    setValue(Resource.error(e,loadFromDb(), errorMessage = localResource.getString(R.string.ERROR_INTERNET_UNAVAILABLE)))
                    //setValue(Resource.error(e,loadFromDb(), errorMessage = "Seems like you are offline. Please check your internet connection and try again."))
                } catch (e: JsonSyntaxException) {
                    utilities.printLogError("An error happened: JsonSyntaxException $e")
                    setValue(Resource.error(e, loadFromDb(), errorMessage = localResource.getString(R.string.SOMETHING_WENT_WRONG), errorNumber =  "111"))
                    //setValue(Resource.error(e, loadFromDb(), errorMessage = "Something went wrong.", errorNumber =  "111"))
                } catch (e: Exception) {
                    utilities.printLogError("An error happened: $e")
                    setValue(Resource.error(e, loadFromDb(), errorMessage = localResource.getString(R.string.SOMETHING_WENT_WRONG)))
                    //setValue(Resource.error(e, loadFromDb(), errorMessage = "Something went wrong."))
                }
            } else {
                utilities.printLogError("Return data from local database")
                setValue(Resource.success(dbResult))
            }
        }
        return this
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    // ---

    private suspend fun fetchFromNetwork(dbResult: ResultType) {
        utilities.printLogError("Fetch data from network")
        if (shouldStoreInDb())
            setValue(Resource.loading(dbResult)) // Dispatch latest value quickly (UX purpose)

        val apiResponse = createCallAsync()
        utilities.printLogError("Data fetched from network")
        if (!hasError(apiResponse)) {
            if (shouldStoreInDb()) {
                saveCallResults(processResponse(apiResponse))
                setValue(Resource.success(loadFromDb()))
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
//        result.postValue(newValue)
        if (result.value != newValue) result.postValue(newValue)
    }

    @WorkerThread
    protected abstract fun processResponse(response: RequestType): ResultType

    @MainThread
    private fun <T> hasError(response: T?): Boolean {

        try {
            val baseResponse = response as BaseResponse<ResultType>
            if (baseResponse.header?.hasErrors!!) {
                if (baseResponse.header != null && baseResponse.header?.errors?.size != 0) {
                    if (baseResponse.header?.errors?.get(0)?.errorNumber == 1100014) {
                        setValue(Resource.error(Throwable(), null, localResource.getString(R.string.MSG_SESSION_EXPIRED), baseResponse.header?.errors?.get(0)?.errorNumber.toString()))
                    } else {
                        //setValue(Resource.error(Throwable(), null, "Something Went wrong..", baseResponse.header?.errors?.get(0)?.errorNumber.toString()))
                        setValue(Resource.error(Throwable(), null, baseResponse.header?.errors?.get(0)?.message.toString(), baseResponse.header?.errors?.get(0)?.errorNumber.toString()))
                    }
                    return true
                } else if (!shouldStoreInDb()) {
                    setValue(Resource.success(baseResponse.jSONData))
                    return true
                }
            } else {
                if (!shouldStoreInDb()) {
                    setValue(Resource.success(baseResponse.jSONData))
                    return true
                }
            }
        } catch (e: ClassCastException) {
            val apiResponse = response as ApiResponse<RequestType>
            if (!apiResponse.statusCode.equals("0")) {
                if (!apiResponse.statusCode.equals("200")) {
                    setValue(Resource.error(Throwable(), null, apiResponse.message))
                    return true
                } else if (!shouldStoreInDb()) {
                    setValue(Resource.success(apiResponse.data as ResultType))
                }
            }
        }

        return false

    }

    @WorkerThread
    protected abstract suspend fun saveCallResults(items: ResultType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun shouldStoreInDb(): Boolean

    @MainThread
    protected abstract suspend fun loadFromDb(): ResultType

    @MainThread
    protected abstract suspend fun createCallAsync(): RequestType
}