package hit.sun.scarlet.hitwashingwebview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import android.webkit.WebViewClient
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
            webViewClient = WebViewClient()
        }
    }

    override fun onBackPressed() =
            if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
}
