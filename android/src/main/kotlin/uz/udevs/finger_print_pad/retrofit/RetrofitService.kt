package uz.udevs.finger_print_pad.retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RetrofitService {
    @Multipart
    @POST("api/v1/task/scanner/scanner-image")
    fun uploadFile(
        @Header("Authorization") token: String,
        @Part("task_id") taskId: RequestBody,
        @Part("key") key: RequestBody,
        @Part body: MultipartBody.Part
    ): Call<ResponseBody>
}