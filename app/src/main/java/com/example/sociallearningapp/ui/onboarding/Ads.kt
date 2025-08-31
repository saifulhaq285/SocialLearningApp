package com.example.sociallearningapp.ui.onboarding

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

@Composable
fun BannerAdView(adUnitId: String = "ca-app-pub-3940256099942544/6300978111") {
    // Google test banner ad unit ID above
    AndroidView(factory = { ctx ->
        AdView(ctx).apply {
            setAdSize(AdSize.BANNER)
            this.adUnitId = adUnitId
            loadAd(AdRequest.Builder().build())
        }
    })
}

// find Activity from a context
private fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

/**
 * Lightweight Interstitial holder that can preload and show an interstitial.
 * Uses Google's test interstitial ad unit by default.
 */
class InterstitialHolder(
    private val context: Context,
    private val adUnitId: String = "ca-app-pub-3940256099942544/1033173712"
) {
    private var interstitialAd: InterstitialAd? = null

    fun load(onLoaded: (() -> Unit)? = null) {
        InterstitialAd.load(
            context,
            adUnitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    onLoaded?.invoke()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    fun showOr(runIfUnavailable: () -> Unit, onDismiss: () -> Unit) {
        val activity = context.findActivity()
        val ad = interstitialAd
        if (activity != null && ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    onDismiss()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    runIfUnavailable()
                }
            }
            ad.show(activity)
        } else {
            runIfUnavailable()
        }
    }
}