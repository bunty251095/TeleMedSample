package com.vivant.telemedicine.network.interceptor

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.vivant.telemedicine.common.Configuration
import com.vivant.telemedicine.common.EncryptionUtility
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.model.base.BaseHandlerResponse
import com.vivant.telemedicine.model.base.BaseResponse
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset


class DecryptInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.isSuccessful) {
            val newResponse = response.newBuilder()
            val contentType = response.header("Content-Type")
            val source = response.body?.source()
            source?.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source?.buffer()
            val responseBodyString = buffer?.clone()?.readString(Charset.forName("UTF-8"))
            //Utilities.printLog("DecryptInterceptor : encryptedStream --> $responseBodyString")
            var decrypted: String? = responseBodyString
            try {
                decrypted = EncryptionUtility.decrypt(
                    Configuration.SecurityKey,
                    responseBodyString!!,
                    Configuration.SecurityKey)

                val baseResponse = Gson().fromJson(decrypted.toString(), BaseResponse::class.java)
                decrypted = getJsonString(decrypted)

/*                val resultResponse = Gson().fromJson(baseResponse.jSONData.toString(), TermsConditionsModel.TermsConditionsResponse::class.java)
                if (resultResponse.termsConditions == null) {
                    decrypted = getJsonString(decrypted)
                } else {
                    val jsonObject: JSONObject = JSONObject()
                    val headerJsonObject: JSONObject = JSONObject(Gson().toJson(baseResponse.header, BaseResponse.Header::class.java))
                    val jsonObjJsonObject: JSONObject = JSONObject(Gson().toJson(resultResponse, TermsConditionsModel.TermsConditionsResponse::class.java))
                    jsonObject.put("Header", headerJsonObject)
                    jsonObject.put("JSONData", jsonObjJsonObject)
                    jsonObject.put("Data", "")
                    Utilities.printLog("Response===> $jsonObject")
                    decrypted = jsonObject.toString()
                }*/

            } catch (e: Exception) {
                e.printStackTrace()
                //val baseResponse = Gson().fromJson(decrypted.toString(), BaseHandlerResponse::class.java)
                decrypted = getJsonString(decrypted)
                //Utilities.printLog("baseResponse--->$decrypted")
                val baseResponse = Gson().fromJson(decrypted.toString(), BaseHandlerResponse::class.java)
                try {
                    if (baseResponse.jObject == null || baseResponse.jObject?.equals("")!!) {
                        baseResponse.jObject = JsonObject()
                        if ( !baseResponse.stateList.isNullOrEmpty() || baseResponse.success ) {
                            val jsonParser = JsonParser()
                            val gsonObject = jsonParser.parse(decrypted) as JsonObject
                            baseResponse.jsonData = JsonObject()
                            baseResponse.jsonData = gsonObject
                            baseResponse.jObject = gsonObject
                            baseResponse.errorNumber = "0"
                            baseResponse.statusCode = "200"
                        }
                    }
                    decrypted = Gson().toJson(baseResponse)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            newResponse.body(ResponseBody.create(contentType!!.toMediaTypeOrNull(), decrypted!!))
            return newResponse.build()
        }
        return response
    }

    private fun getJsonString(decrypted: String?): String? {
        /* val decryptedResponse: String = decrypted!!
             .replace("\\r\\n", "")
             .replace("\\\"", "\"")
             .replace("\"{", "{")
             .replace("}\"", "}")
         return decryptedResponse*/
        val decryptedResponse: String = decrypted!!
            .replace("\\r\\n", "")
            .replace("\\\\r\\\\n", "")
            .replace("\\\"", "\"")
            .replace("\\\\\"", "\"")
            .replace("\"{", "{")
            .replace("\"[", "[")
            .replace("}\"", "}")
            .replace("]\"", "]")
        return decryptedResponse

    }
}