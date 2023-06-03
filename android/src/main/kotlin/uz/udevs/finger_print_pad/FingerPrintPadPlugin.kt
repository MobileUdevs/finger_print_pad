package uz.udevs.finger_print_pad


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.senter.function.openapi.unstable.FingerprintC_FBI
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import java.io.ByteArrayOutputStream
import java.io.File

/** FingerPrintPadPlugin */
class FingerPrintPadPlugin : FlutterPlugin, MethodCallHandler, ActivityAware,
    PluginRegistry.NewIntentListener, PluginRegistry.ActivityResultListener {



    private lateinit var channel: MethodChannel
    private lateinit var activity: Activity
    private lateinit var resultMethod: Result

    private lateinit var fingerprintC: FingerprintC_FBI

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "finger_print_pad")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        resultMethod = result
        when (call.method) {
            "init" -> {
                init()
            }

            "openDevice" -> {
                val isSuccess = openDevice()
                result.success(isSuccess)
            }

            "closeDevice" -> {
                closeDevice()
            }

            "saveFinger" -> {
                val bitmap = saveFinger()
                if (bitmap != null) {
                    val byteArray = bitmapToByteArray(bitmap)
                    result.success(byteArray)
                } else {
                    result.success(null)
                }
            }

            "compareFinger" -> compareFinger()
            else -> result.notImplemented()
        }

    }



    fun init() {
        fingerprintC = FingerprintC_FBI.getInstance(activity)
        fingerprintC.init()
    }


    private fun openDevice(): Boolean {
        fingerprintC = FingerprintC_FBI.getInstance(activity)
        var isSuccess: Boolean = false
        Thread {
            isSuccess = fingerprintC.openDevice()

//      runOnUiThread {
        Toast.makeText(
            activity,
          if (isSuccess) "Success" else "Failed",
          Toast.LENGTH_SHORT
        ).show()
//      }
        }.start()
        return isSuccess
    }

    private fun closeDevice() {
        fingerprintC.closeDevice()
    }

    private fun saveFinger(): Bitmap? {
        var mBitmap: Bitmap? = null
        Thread(Runnable {
//      runOnUiThread { tvInfo.text = "Input finger" }
            val captureFingerResult = fingerprintC.capture(5 * 1000)

            // an error occurred
            if (captureFingerResult == null) {
//        runOnUiThread { tvInfo.text = "Collect failed" }
                return@Runnable
            }

            // save bitmap image locally
            mBitmap = FingerprintC_FBI.GetBitmapFromRaw(
                captureFingerResult.imageData,
                captureFingerResult.width,
                captureFingerResult.height
            )
//      runOnUiThread {
//        imageView.setImageBitmap(mBitmap)
//        //保存至文件中
//        val isSuccess = FileOperate.addData(captureFingerResult.featureData)
//        if (isSuccess) {
//          tvInfo.text = "Save success"
//        } else {
//          tvInfo.text = "Save failed"
//        }
//      }
        }).start()
        return mBitmap
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    @SuppressLint("SetTextI18n")
    fun compareFinger() {
        Thread(Runnable {
//      runOnUiThread { tvInfo.text = "Input finger" }
            val captureFingerResult = fingerprintC.capture(5 * 1000)

            // an error occurred
            if (captureFingerResult == null) {
//        runOnUiThread { tvInfo.text = "Collect failed" }
                return@Runnable
            }

            // save bitmap image locally
//      mBitmap = FingerprintC_FBI.GetBitmapFromRaw(
//        captureFingerResult.imageData,
//        captureFingerResult.width,
//        captureFingerResult.height
//      )
//      runOnUiThread { imageView.setImageBitmap(mBitmap) }
            val dir = File(Environment.getExternalStorageDirectory(), "FBI")
            if (!dir.exists()) {
                dir.mkdir()
            }
            val list = dir.listFiles()
            var isFind = false
            if (list != null && list.isNotEmpty()) {
                for (f in list) {
                    val feature = FileOperate.getData(f)
                    val isSuccess = fingerprintC.compare(captureFingerResult.featureData, feature)
                    Log.d("mine", "isSuccess-->$isSuccess")
                    if (isSuccess) {
                        isFind = true
//            runOnUiThread { tvInfo.text = "Compare success" }
                        break
                    }
                }
                if (!isFind) {
//          runOnUiThread {
//            runOnUiThread {
//              tvInfo.text = "Not Found"
//            }
//          }
                }
            } else {
//        runOnUiThread { tvInfo.text = "No finger Templete" }
            }
        }).start()
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity as FlutterActivity
        binding.addOnNewIntentListener(this)
        binding.addActivityResultListener(this)
    }

    override fun onDetachedFromActivityForConfigChanges() {
//        activity.closeContextMenu()
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivity() {
//        activity = null
    }

    override fun onNewIntent(intent: Intent): Boolean {
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        TODO("Not yet implemented")
    }
}
