package uz.udevs.finger_print_pad

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.google.gson.Gson
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import uz.udevs.finger_print_pad.model.FingerModel


const val EXTRA_ARGUMENT = "uz.udevs.finger_print_pad.ARGUMENT"
const val FINGER_ACTIVITY = 111
const val FINGER_ACTIVITY_FINISH = 222

/** FingerPrintPadPlugin */
class FingerPrintPadPlugin : FlutterPlugin, MethodCallHandler, ActivityAware,
    PluginRegistry.NewIntentListener, PluginRegistry.ActivityResultListener {

    private lateinit var channel: MethodChannel
    private lateinit var activity: Activity
    private lateinit var resultMethod: Result


    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "finger_print_pad")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        resultMethod = result
        when (call.method) {
            "scanFinger" -> {
                print("scanFinger")
                print(call.arguments)
                if (!call.hasArgument("model")) {
                    Toast.makeText(activity, "Model is null", Toast.LENGTH_LONG).show();
                    result.error("Error", "Model is null", null)
                    return
                } else if (call.hasArgument("model")) {
                    val fingerModel = call.argument("model") as String?
                    val gson = Gson()
                    val config =
                        gson.fromJson(fingerModel, FingerModel::class.java)
                    val intent = Intent(activity, MainActivity::class.java)
                    intent.putExtra(EXTRA_ARGUMENT, config)
                    activity.startActivityForResult(intent, FINGER_ACTIVITY)
                }
            }

            "init" -> {
//                init()
            }

            "openDevice" -> {
//                result.success(openDevice())
            }

            "closeDevice" -> {
//                closeDevice()
            }

            "saveFinger" -> {
//                val byte = saveFinger()
//                Toast.makeText(activity, "$byte", Toast.LENGTH_LONG).show()
//                resultMethod.success("$byte")
            }

            "compareFinger" -> compareFinger()
            else -> result.notImplemented()
        }

    }


    private fun init() {
//        fingerprintC = FingerprintC_FBI.getInstance(activity)
//        fingerprintC.init()
    }


//    private fun openDevice(): Boolean {
//        fingerprintC = FingerprintC_FBI.getInstance(activity)
//        val isSuccess = fingerprintC.openDevice()
//        Toast.makeText(
//            activity, if (isSuccess) "Success" else "Failed", Toast.LENGTH_SHORT
//        ).show()
//        return isSuccess
//    }

    private fun closeDevice() {
//        fingerprintC.closeDevice()
    }

//    private fun saveFinger(): ByteArray {
//        val captureFingerResult = fingerprintC.capture(5 * 1000)
//        val mBitmap: Bitmap = FingerprintC_FBI.GetBitmapFromRaw(
//            captureFingerResult.imageData, captureFingerResult.width, captureFingerResult.height
//        )
//        return bitmapToByteArray(mBitmap)
//    }

//    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
//        val stream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//        return stream.toByteArray()
//    }

    @SuppressLint("SetTextI18n")
    fun compareFinger() {
//        val captureFingerResult = fingerprintC.capture(5 * 1000)
//
//        val dir = File(Environment.getExternalStorageDirectory(), "FBI")
//        if (!dir.exists()) {
//            dir.mkdir()
//        }
//        val list = dir.listFiles()
//        var isFind = false
//        if (list != null && list.isNotEmpty()) {
//            for (f in list) {
//                val feature = FileOperate.getData(f)
//                val isSuccess = fingerprintC.compare(captureFingerResult.featureData, feature)
//                Log.d("mine", "isSuccess-->$isSuccess")
//                if (isSuccess) {
//                    isFind = true
//                    break
//                }
//            }
//        } else {
//            Toast.makeText(activity, "No finger", Toast.LENGTH_SHORT).show()
//        }
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
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivity() {
    }

    override fun onNewIntent(intent: Intent): Boolean {
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return true
    }
}
