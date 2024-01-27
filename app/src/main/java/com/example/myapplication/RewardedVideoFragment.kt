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

import com.vungle.ads.RewardedAd
import com.vungle.ads.VungleError

class RewardedVideoFragment : Fragment(R.layout.fragment_rewarded_video), BaseAdListener {

    companion object {
        fun newInstance() = RewardedVideoFragment()
    }

    private lateinit var viewModel: FullScreenViewModel

    private var rewardedAd: RewardedAd? = null

    private var placementId = "RVTEST-9462001"

    private lateinit var appContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rewarded_video, container, false)
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

    fun loadRewardedVideo() {
        rewardedAd = RewardedAd(appContext, placementId, AdConfig().apply {
        }).apply {
            adListener = this@RewardedVideoFragment
            load()
        }
    }

    fun playRewardedVideo() {
        Log.d(MainActivity.TAG, "In playRewarded")
        if (rewardedAd?.canPlayAd() == true) {
            Log.d(MainActivity.TAG, "RV should play: ${rewardedAd?.canPlayAd()}")
            rewardedAd?.play()
        } else {
            Log.d(MainActivity.TAG, "RV is not ready: ${rewardedAd?.canPlayAd()}")
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

    fun onAdRewarded(baseAd: BaseAd) {
        Log.d(MainActivity.TAG, "Ad rewarded the user for watching")
    }

    override fun onAdFailedToLoad(baseAd: BaseAd, adError: VungleError) {
        Log.d(MainActivity.TAG, "Ad failed to load {onAdFailedToLoad}")
    }


    override fun onAdFailedToPlay(baseAd: BaseAd, adError: VungleError) {
        Log.d(MainActivity.TAG, "Ad failed to play {onAdFailedToPlay}")
    }
}