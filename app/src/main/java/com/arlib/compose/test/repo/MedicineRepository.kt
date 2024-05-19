package com.arlib.compose.test.repo

import com.arlib.compose.test.network.ApiService
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class MedicineRepository @Inject constructor(private val api: ApiService) {

    suspend fun execute(): Response<ResponseBody> {
        return api.getDiseases()
    }

}
