package uz.udevs.finger_print_pad.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FingerModel(
    @SerializedName("task_id")
    val taskId: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("key")
    val key: String,
    @SerializedName("base_url")
    val baseUrl: String,
) : Serializable