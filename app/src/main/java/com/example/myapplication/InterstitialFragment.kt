package com.example.myapplication

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vungle.ads.AdConfig
import com.vungle.ads.BaseAd
import com.vungle.ads.BaseAdListener

import com.vungle.ads.InterstitialAd
import com.vungle.ads.VungleError

class InterstitialFragment : Fragment(R.layout.fragment_interstitial), BaseAdListener {

    companion object {
        fun newInstance() = InterstitialFragment()
    }

    private lateinit var viewModel: FullScreenViewModel

    private var interstitialAd: InterstitialAd? = null

    private var placementId = "DEFAULT-4695800"

    private lateinit var appContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_interstitial, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[FullScreenViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context
    }

    fun loadInterstitial() {
        interstitialAd = InterstitialAd(appContext, placementId, AdConfig().apply {
        }).apply {
            adListener = this@InterstitialFragment
            load()
        }
    }

    fun playInterstitial() {
        Log.d(MainActivity.TAG, "In playInterstitial")
        if (interstitialAd?.canPlayAd() == true) {
            Log.d(MainActivity.TAG, "Interstitial should play: ${interstitialAd?.canPlayAd()}")
            interstitialAd?.play()
        } else {
            Log.d(MainActivity.TAG, "Interstitial is not ready: ${interstitialAd?.canPlayAd()}")
        }
    }

    override fun onAdLoaded(baseAd: BaseAd) {
        Log.d(MainActivity.TAG, "Creative id:" + baseAd.creativeId)
    }


    override fun onAdStart(baseAd: BaseAd) {
        Log.d(MainActivity.TAG, "Ad has started {onAdStart}")
    }


    override fun onAdImpression(baseAd: BaseAd) {
        Log.d(MainActivity.TAG, "Ad logged impression {onAdImpression}")
    }


    override fun onAdEnd(baseAd: BaseAd) {
        Log.d(MainActivity.TAG, "Ad has ended {onAdEnd}")
    }


    override fun onAdClicked(baseAd: BaseAd) {
        Log.d(MainActivity.TAG, "Ad was clicked {onAdClicked}")
    }


    override fun onAdLeftApplication(baseAd: BaseAd) {
        Log.d(MainActivity.TAG, "Ad routed user out of application {onAdLeftApplication}")
    }


    override fun onAdFailedToLoad(baseAd: BaseAd, adError: VungleError) {
        Log.d(MainActivity.TAG, "Ad failed to load {onAdFailedToLoad}")
    }


    override fun onAdFailedToPlay(baseAd: BaseAd, adError: VungleError) {
        Log.d(MainActivity.TAG, "Ad failed to play {onAdFailedToPlay}")
    }
}