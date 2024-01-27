package com.example.myapplication

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.vungle.ads.BannerAd
import com.vungle.ads.BannerAdSize
import com.vungle.ads.BannerView
import com.vungle.ads.BaseAd
import com.vungle.ads.BaseAdListener
import com.vungle.ads.VungleError

class BannerFragment : Fragment(R.layout.activity_main), BaseAdListener {
    companion object {
        fun newInstance() = BannerFragment()
    }

    private lateinit var viewModel: FullScreenViewModel

    private var bannerAd: BannerAd? = null

    private var bannerPlacementId = "BANNER_TEST-0796908"
    private var MRECPlacementId = "MREC_TEST-7003332"
    private var bannerAdContainer: RelativeLayout? = null

    private lateinit var appContext: Context
    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater.inflate(R.layout.activity_main, container, false)
        bannerAdContainer = viewOfLayout.findViewById(R.id.bannerAdContainer)
//        return inflater.inflate(R.layout.fragment_banner, container, false)
        return viewOfLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this)[FullScreenViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context

    }

    fun loadMREC() {
        bannerAd = BannerAd(appContext, MRECPlacementId, BannerAdSize.VUNGLE_MREC)
            .apply {
                adListener = this@BannerFragment
                load()
            }
    }

    fun playMREC() {
        Log.d(MainActivity.TAG, "In playBanner")

        val bannerView: BannerView? = bannerAd?.getBannerView()
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER_HORIZONTAL
        )
        bannerAdContainer?.addView(bannerView, params)
   }

    fun loadBanner() {
        bannerAd = BannerAd(appContext, bannerPlacementId, BannerAdSize.BANNER)
        .apply {
            adListener = this@BannerFragment
            load()
        }
    }

    fun playBanner() {
        Log.d(MainActivity.TAG, "In playBanner")

        val bannerView: BannerView? = bannerAd?.getBannerView()
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER_HORIZONTAL
        )
        bannerAdContainer?.addView(bannerView, params)
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
        bannerAdContainer?.removeAllViews()
        bannerAd?.finishAd()
//        bannerAd?.setAdListener(null)
        bannerAd = null
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