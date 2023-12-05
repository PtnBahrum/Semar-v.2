package com.example.semarv2.data.source.remote.network.retrofit

import com.example.semarv2.data.source.remote.model.PredictWayang
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("https://wayang-prediction-mrlpwmp4cq-et.a.run.app/predict")
    suspend fun predictWayang(
        @Part file: MultipartBody.Part,
    ): PredictWayang
}