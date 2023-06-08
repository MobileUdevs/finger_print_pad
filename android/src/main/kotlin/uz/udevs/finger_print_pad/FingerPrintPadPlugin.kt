package uz.udevs.finger_print_pad


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


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
                print("initinitinit")
                init()
            }

            "openDevice" -> {
                result.success(openDevice())
            }

            "closeDevice" -> {
                closeDevice()
            }

            "saveFinger" -> {
                val byte = saveFinger()
                Toast.makeText(activity, "$byte", Toast.LENGTH_LONG).show()
                resultMethod.success("$byte")
            }

            "compareFinger" -> compareFinger()
            else -> result.notImplemented()
        }

    }


    private fun init() {
        fingerprintC = FingerprintC_FBI.getInstance(activity)
        fingerprintC.init()
    }


    private fun openDevice(): Boolean {
        fingerprintC = FingerprintC_FBI.getInstance(activity)
        val isSuccess = fingerprintC.openDevice()
        Toast.makeText(
            activity,
            if (isSuccess) "Success" else "Failed",
            Toast.LENGTH_SHORT
        ).show()
        return isSuccess
    }

    private fun closeDevice() {
        fingerprintC.closeDevice()
    }

    private fun saveFinger(): ByteArray {
        val captureFingerResult =
            fingerprintC.capture(5 * 1000)
        val mBitmap: Bitmap = FingerprintC_FBI.GetBitmapFromRaw(
            captureFingerResult.imageData,
            captureFingerResult.width,
            captureFingerResult.height
        )
        return bitmapToByteArray(mBitmap)
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun bitmapToFile(bitmap: Bitmap): File {
        val sd = Environment.getExternalStorageDirectory()
        val file: File = File(sd, "image.png")
        val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.close()
        return file
    }

    @SuppressLint("SetTextI18n")
    fun compareFinger() {
        val captureFingerResult = fingerprintC.capture(5 * 1000)

        // an error occurred
//        if (captureFingerResult == null) {
////        runOnUiThread { tvInfo.text = "Collect failed" }
//            return@Runnable
//        }

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
                val isSuccess =
                    fingerprintC.compare(captureFingerResult.featureData, feature)
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
