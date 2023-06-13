package uz.udevs.finger_print_pad.retrofit

object Common {
    fun retrofitService(baseUrl: String): RetrofitService {
        return RetrofitClient.getRetrofit(baseUrl).create(RetrofitService::class.java)
    }
}