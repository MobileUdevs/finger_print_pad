package uz.udevs.finger_print_pad

import com.senter.function.openapi.unstable.FingerprintC_FBI
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.udevs.finger_print_pad.model.FingerModel
import uz.udevs.finger_print_pad.retrofit.Common
import uz.udevs.finger_print_pad.retrofit.RetrofitService
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Date


class MainActivity : Activity() {

    private lateinit var fingerprintC: FingerprintC_FBI

    private lateinit var tvInfo: TextView
    private lateinit var imageView: ImageView

    private lateinit var fingerModel: FingerModel
    private var retrofitService: RetrofitService? = null

    private var mBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionBar?.hide()

        fingerModel = intent.getSerializableExtra(EXTRA_ARGUMENT) as FingerModel

        imageView = findViewById(R.id.imageView)
        tvInfo = findViewById(R.id.tv_info)
        try {
            fingerprintC = FingerprintC_FBI.getInstance(this)
            fingerprintC.init()
        } catch (e: Exception) {
            print(e)
        }

        retrofitService =
            if (fingerModel.baseUrl.isNotEmpty()) Common.retrofitService(fingerModel.baseUrl) else null

    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            fingerprintC.uninit()
        } catch (e: Exception) {
            print(e)
        }
    }

    private fun uploadFile(file: File) {
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("fingerprint", file.name, requestFile)
        retrofitService?.uploadFile(
            fingerModel.token,
            fingerModel.taskId.toRequestBody(MultipartBody.FORM),
            fingerModel.key.toRequestBody(MultipartBody.FORM),
            body,
        )?.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("TAG", "onFailure: ", t)
                Toast.makeText(
                    this@MainActivity, "Rasm yuklanmadi", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.e("TAG", "onResponse: ${response.body()?.string()}")
                Toast.makeText(
                    this@MainActivity,
                    "Rasm yuklash muvaffaqiyatli",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        })

    }

    @Suppress("UNUSED_PARAMETER")
    fun clickOpenDevice(view: View) {
        Thread {
            val isSuccess = fingerprintC.openDevice()
            runOnUiThread {
                Toast.makeText(
                    this@MainActivity, if (isSuccess) "Muvaffaqiyatli" else "Muvaffaqiyatsiz", Toast.LENGTH_SHORT
                ).show()
            }
        }.start()
    }

    @Suppress("UNUSED_PARAMETER")
    fun clickCloseDevice(view: View) {
        fingerprintC.closeDevice()
    }

    @Suppress("UNUSED_PARAMETER")
    fun uploadFile(view: View) {
        try {
            if (mBitmap == null) return
            persistImage(mBitmap!!)
        } catch (e: Exception) {
            Toast.makeText(this, "Xato ${e.printStackTrace()}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onBackPressed(view: View) {
        finish()
        super.onBackPressed()
    }

    private fun persistImage(bitmap: Bitmap) {
        val filesDir: File = applicationContext.filesDir
        val date = Date()
        val imageFile = File(filesDir, "finger${date.time}.jpg")
        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
            if (imageFile.exists()) {
                uploadFile(file = imageFile)
            } else {
                Toast.makeText(this, "Rasm mavjud emas", Toast.LENGTH_SHORT).show()
            }
        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "Xato ${e.printStackTrace()}", Toast.LENGTH_SHORT).show()
            Log.e(javaClass.simpleName, "Error writing bitmap", e)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    @SuppressLint("SetTextI18n")
    fun clickSaveFinger(view: View) {
        Thread(Runnable {
            runOnUiThread { tvInfo.text = "Barmoq izi kiritish" }
            val captureFingerResult = fingerprintC.capture(5 * 1000)

            // an error occurred
            if (captureFingerResult == null) {
                runOnUiThread { tvInfo.text = "Olib bo‘lmadi" }
                return@Runnable
            }

            // save bitmap image locally
            mBitmap = FingerprintC_FBI.GetBitmapFromRaw(
                captureFingerResult.imageData, captureFingerResult.width, captureFingerResult.height
            )
            runOnUiThread {
                imageView.setImageBitmap(mBitmap)
                val isSuccess = FileOperate.addData(captureFingerResult.featureData)
                if (isSuccess) {
                    tvInfo.text = "Mufaqiyatli saqlandi"
                } else {
                    tvInfo.text = "Saqlanmadi"
                }
            }
        }).start()
    }

//    @Suppress("UNUSED_PARAMETER")
//    @SuppressLint("SetTextI18n")
//    fun clickCompareFinger(view: View) {
//        Thread(Runnable {
//            runOnUiThread { tvInfo.text = "Barmoq izini kiriting" }
//            val captureFingerResult = fingerprintC.capture(5 * 1000)
//
//            // an error occurred
//            if (captureFingerResult == null) {
//                runOnUiThread { tvInfo.text = "Collect failed" }
//                return@Runnable
//            }
//
//            // save bitmap image locally
//            mBitmap = FingerprintC_FBI.GetBitmapFromRaw(
//                captureFingerResult.imageData, captureFingerResult.width, captureFingerResult.height
//            )
//            runOnUiThread { imageView.setImageBitmap(mBitmap) }
//            val dir = File(Environment.getExternalStorageDirectory(), "FBI")
//            if (!dir.exists()) {
//                dir.mkdir()
//            }
//            val list = dir.listFiles()
//            var isFind = false
//            if (list != null && list.isNotEmpty()) {
//                for (f in list) {
//                    val feature = FileOperate.getData(f)
//                    val isSuccess = fingerprintC.compare(captureFingerResult.featureData, feature)
//                    Log.d("mine", "isSuccess-->$isSuccess")
//                    if (isSuccess) {
//                        isFind = true
//                        runOnUiThread { tvInfo.text = "Barmoq izi olindi" }
//                        break
//                    }
//                }
//                if (!isFind) {
//                    runOnUiThread {
//                        runOnUiThread {
//                            tvInfo.text = "Topilmadi"
//                        }
//                    }
//                }
//            } else {
//                runOnUiThread { tvInfo.text = "Barmoq izi topilmadi" }
//            }
//        }).start()
//    }
}