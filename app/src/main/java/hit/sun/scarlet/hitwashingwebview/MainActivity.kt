package hit.sun.scarlet.hitwashingwebview

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidBug5497Workaround.assistActivity(this)

        initWebView()

        floatingActionButton.setOnClickListener({ webView.reload() })
    }

    private fun initWebView() {
        webView.apply {
            settings.javaScriptEnabled = true
            requestFocus()
            scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
            settings.allowUniversalAccessFromFileURLs = true
            loadUrl("http://115.159.59.238/")
            webChromeClient = WebChromeClient()

            //在js中调用本地java方法
            addJavascriptInterface(JsInterface(context), "AndroidWebView")
        }
    }

    private inner class JsInterface(private val mContext: Context) {
        @JavascriptInterface
        fun showInfoFromJs(name: String) {
            Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() =
            if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
}
