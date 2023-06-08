package uz.udevs.finger_print_pad

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.senter.function.openapi.unstable.FingerprintC_FBI
import java.io.File

class MainActivity : Activity() {

    private lateinit var fingerprintC: FingerprintC_FBI

    private lateinit var tvInfo: TextView
    private lateinit var imageView: ImageView

    private var mBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)
        tvInfo = findViewById(R.id.tv_info)
        fingerprintC = FingerprintC_FBI.getInstance(this)
        fingerprintC.init()
    }

    override fun onDestroy() {
        super.onDestroy()
        fingerprintC.uninit()
    }

    @Suppress("UNUSED_PARAMETER")
    fun clickOpenDevice(view: View) {
        Thread {
            val isSuccess = fingerprintC.openDevice()
            runOnUiThread {
                Toast.makeText(
                    this@MainActivity,
                    if (isSuccess) "Success" else "Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.start()
    }

    @Suppress("UNUSED_PARAMETER")
    fun clickCloseDevice(view: View) {
        fingerprintC.closeDevice()
    }

    @Suppress("UNUSED_PARAMETER")
    fun clickBack(view: View) {
        finish()
        super.onBackPressed()
    }

    @Suppress("UNUSED_PARAMETER")
    @SuppressLint("SetTextI18n")
    fun clickSaveFinger(view: View) {
        Thread(Runnable {
            runOnUiThread { tvInfo.text = "Input finger" }
            val captureFingerResult = fingerprintC.capture(5 * 1000)

            // an error occurred
            if (captureFingerResult == null) {
                runOnUiThread { tvInfo.text = "Collect failed" }
                return@Runnable
            }

            // save bitmap image locally
            mBitmap = FingerprintC_FBI.GetBitmapFromRaw(
                captureFingerResult.imageData,
                captureFingerResult.width,
                captureFingerResult.height
            )
            runOnUiThread {
                imageView.setImageBitmap(mBitmap)
                //保存至文件中
                val isSuccess = FileOperate.addData(captureFingerResult.featureData)
                if (isSuccess) {
                    tvInfo.text = "Save success"
                } else {
                    tvInfo.text = "Save failed"
                }
            }
        }).start()
    }

    @Suppress("UNUSED_PARAMETER")
    @SuppressLint("SetTextI18n")
    fun clickCompareFinger(view: View) {
        Thread(Runnable {
            runOnUiThread { tvInfo.text = "Input finger" }
            val captureFingerResult = fingerprintC.capture(5 * 1000)

            // an error occurred
            if (captureFingerResult == null) {
                runOnUiThread { tvInfo.text = "Collect failed" }
                return@Runnable
            }

            // save bitmap image locally
            mBitmap = FingerprintC_FBI.GetBitmapFromRaw(
                captureFingerResult.imageData,
                captureFingerResult.width,
                captureFingerResult.height
            )
            runOnUiThread { imageView.setImageBitmap(mBitmap) }
            val dir =
                File(Environment.getExternalStorageDirectory(), "FBI")
            if (!dir.exists()) {
                dir.mkdir()
            }
            val list = dir.listFiles()
            var isFind = false
            if (list != null && list.isNotEmpty()) {
                for (f in list) {
                    val feature = FileOperate.getData(f)
                    val isSuccess =
                        fingerprintC.compare(captureFingerResult.featureData, feature)
                    Log.d("mine", "isSuccess-->$isSuccess")
                    if (isSuccess) {
                        isFind = true
                        runOnUiThread { tvInfo.text = "Compare success" }
                        break
                    }
                }
                if (!isFind) {
                    runOnUiThread {
                        runOnUiThread {
                            tvInfo.text = "Not Found"
                        }
                    }
                }
            } else {
                runOnUiThread { tvInfo.text = "No finger Templete" }
            }
        }).start()
    }
}