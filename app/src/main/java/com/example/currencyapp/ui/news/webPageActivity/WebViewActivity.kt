package com.example.currencyapp.ui.news.webPageActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.KeyEvent
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.example.currencyapp.R
import com.example.currencyapp.databinding.ActivityWebViewBinding
import com.example.currencyapp.domain.services.ConnectivityObserver.Status.Available
import com.example.currencyapp.domain.services.ConnectivityObserver.Status.Lost
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding
    private val viewModel: WebViewModel by viewModels()

    private var progress: Progress? = null
    private var isLoaded: Boolean = false
    private var doubleBackToExitPressedOnce = false

    private val args: WebViewActivityArgs by navArgs()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            viewModel.networkStatus.observe(this@WebViewActivity) { connectivityStatus ->
                if (connectivityStatus == Lost) {
                    showToast(getString(R.string.no_internet_connection_error_message))
                    infoTV.text = getString(R.string.no_internet_connection_error_message)
                    showNoNetSnackBar()
                } else if (!isLoaded && connectivityStatus == Available) {
                    loadWebView()
                }
            }
        }
    }

    private fun loadWebView() = with(binding) {
        infoTV.text = ""
        webView.loadUrl(args.url)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                setProgressDialogVisibility(true)
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                isLoaded = true
                setProgressDialogVisibility(false)
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                isLoaded = false
                val errorMessage = "Got Error! $error"
                showToast(errorMessage)
                infoTV.text = errorMessage
                setProgressDialogVisibility(false)
                super.onReceivedError(view, request, error)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean = with(binding) {
        if (event.action == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    showToastToExit()
                }
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showToastToExit() {
        when {
            doubleBackToExitPressedOnce -> {
                onBackPressed()
            }
            else -> {
                doubleBackToExitPressedOnce = true
                showToast(getString(R.string.back_again_to_exit))
                Handler(Looper.myLooper()!!).postDelayed({
                    doubleBackToExitPressedOnce = false
                }, 2000)
            }
        }
    }

    private fun setProgressDialogVisibility(visible: Boolean) {
        if (visible) progress = Progress(this, R.string.please_wait, cancelable = true)
        progress?.apply { if (visible) show() else dismiss() }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showNoNetSnackBar() {
        val snack =
            Snackbar.make(
                binding.root,
                getString(R.string.no_internet_connection_error_message),
                Snackbar.LENGTH_INDEFINITE
            )
        snack.setAction(getString(R.string.settings)) {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }
        snack.show()
    }
}
