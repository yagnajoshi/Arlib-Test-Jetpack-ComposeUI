package com.arlib.compose.test.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    companion object {
        const val BASE_URL = "https://run.mocky.io"
    }

    @GET("/v3/72ff0b88-de14-432f-8297-9f8f4535beba")
    suspend fun getDiseases(): Response<ResponseBody>

}
