package hit.sun.scarlet.hitwashingwebview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidBug5497Workaround.assistActivity(this)

        initWebView()

        floatingActionButton.setOnClickListener({
            webView.reload()
//            webView.evaluateJavascript("javascript:printBill()", {
//                Log.d("AKG", it)
//            })
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView.apply {
            settings.javaScriptEnabled = true
            scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
            settings.javaScriptCanOpenWindowsAutomatically = true
            webViewClient = WebViewClient()
            addJavascriptInterface(WebAppInterface(context), "Android")
            loadUrl("http://115.159.59.238/")
        }
    }

    inner class WebAppInterface
    /** Instantiate the interface and set the context  */
    internal constructor(private var mContext: Context) {

        @JavascriptInterface
        fun showToast(toast: String) {
            if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null || !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].connState) {
                btnBluetoothConn()
            } else {
                btnReceiptPrint(toast)
            }

            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
        }
    }

    fun btnBluetoothConn() =
            startActivityForResult(Intent(this, BluetoothDeviceList::class.java), Constant.BLUETOOTH_REQUEST_CODE)

    fun btnReceiptPrint(toast: String) = ThreadPool.getInstantiation().addTask {
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].currentPrinterCommand === PrinterCommand.ESC) Constant.sendReceiptWithResponse(id, toast)
        else Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constant.BLUETOOTH_REQUEST_CODE -> {
                    /*获取蓝牙mac地址*/
                    val macAddress = data.getStringExtra(BluetoothDeviceList.EXTRA_DEVICE_ADDRESS)
                    //初始化话DeviceConnFactoryManager
                    DeviceConnFactoryManager.Build()
                            .setId(0)
                            //设置连接方式
                            .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
                            //设置连接的蓝牙mac地址
                            .setMacAddress(macAddress)
                            .build()
                    //打开端口
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].openPort()
                }
                else -> {
                }
            }
        }
    }


    override fun onBackPressed() =
            if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
}
