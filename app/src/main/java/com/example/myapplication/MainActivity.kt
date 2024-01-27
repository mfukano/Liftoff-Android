package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.findNavController
import androidx.fragment.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.ActivityMainBinding

// Vungle Imports
import com.vungle.ads.*

class MainActivity : AppCompatActivity(), BaseAdListener {
    // Sets up keys for logging and initialization
    companion object {
        const val APP_KEY = "65b42d54aad06991469bd314"
        const val TAG = "VUNGLE"
    }

    // Creates ability to programmatically fetch the components on screen for rendering and click handling
    private lateinit var binding: ActivityMainBinding

    private lateinit var interstitialFragment: InterstitialFragment
    private lateinit var rewardedFragment: RewardedVideoFragment
    private lateinit var bannerFragment: BannerFragment
    private lateinit var nativeFragment: NativeFragment


    private var bannerAd: BannerAd? = null

    private var bannerPlacementId = "BANNER_TEST-0796908"
    private var MRECPlacementId = "MREC_TEST-7003332"
    private var bannerAdContainer: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binds components of the relevant layout to a reference and sets content view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        interstitialFragment = InterstitialFragment.newInstance()
        rewardedFragment = RewardedVideoFragment.newInstance()
        bannerFragment = BannerFragment.newInstance()
        nativeFragment = NativeFragment.newInstance()

        // Vungle initialization helper
        initVungle(applicationContext)

    }

    private fun initVungle(context: Context) {
        VungleAds.init(context, APP_KEY, object : InitializationListener {
            override fun onSuccess() {
                Log.d(TAG, "Vungle SDK init onSuccess()")
                interstitialFragment.onAttach(applicationContext)
                rewardedFragment.onAttach(applicationContext)
                bannerFragment.onAttach(applicationContext)
                nativeFragment.onAttach(applicationContext)
                initUIElements()
            }

            override fun onError(vungleError: VungleError) {
                Log.d(TAG, "onError(): ${vungleError.localizedMessage}")
            }
        })
    }

    private fun initUIElements() {
        binding.loadInterstitial.setOnClickListener {
            interstitialFragment.loadInterstitial()
        }
        binding.showInterstitial.setOnClickListener {
            interstitialFragment.playInterstitial()
        }
        binding.loadRv.setOnClickListener {
            rewardedFragment.loadRewardedVideo()
        }
        binding.showRv.setOnClickListener {
            rewardedFragment.playRewardedVideo()
        }
        binding.loadBn.setOnClickListener {
            loadBanner()
        }
        binding.showBn.setOnClickListener {
            playBanner()
        }
        binding.loadMrec.setOnClickListener {
            loadMREC()
        }
        binding.showMrec.setOnClickListener{
            playMREC()
        }
        binding.loadNative.setOnClickListener {
            Log.d(TAG, "Clicked load native ad button")
//            nativeFragment.loadNativeAd()
        }
        binding.showNative.setOnClickListener {
            Log.d(TAG, "Clicked play native ad button")
//            nativeFragment.showNativeAd()
        }
        bannerAdContainer = binding.bannerAdContainer
    }

    fun loadMREC() {
        bannerAd = BannerAd(applicationContext, MRECPlacementId, BannerAdSize.VUNGLE_MREC)
            .apply {
                adListener = this@MainActivity
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
        bannerAd = BannerAd(applicationContext, bannerPlacementId, BannerAdSize.BANNER)
            .apply {
                adListener = this@MainActivity
                load()
            }
    }

    fun playBanner() {
        Log.d(TAG, "In playBanner")

        val bannerView: BannerView? = bannerAd?.getBannerView()
        val params = FrameLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER_HORIZONTAL
        )
        if (bannerAd?.canPlayAd() == true)
            bannerAdContainer?.addView(bannerView, params)
    }

    override fun onAdLoaded(baseAd: BaseAd) {
        Log.d(TAG, "Creative id:" + baseAd.creativeId)
    }

    override fun onAdStart(baseAd: BaseAd) {
        Log.d(TAG, "Ad has started {onAdStart}")
    }

    override fun onAdImpression(baseAd: BaseAd) {
        Log.d(TAG, "Ad logged impression {onAdImpression}")
    }

    override fun onAdEnd(baseAd: BaseAd) {
        // In test, it seems like this is never called
        Log.d(TAG, "Ad has ended {onAdEnd}")
        bannerAdContainer?.removeAllViews()
        bannerAd?.finishAd()
        bannerAd = null
    }


    override fun onAdClicked(baseAd: BaseAd) {
        Log.d(TAG, "Ad was clicked {onAdClicked}")

        // Workaround to close the ad; click on it to open and then swipe back to the application
        bannerAdContainer?.removeAllViews()
        bannerAd?.finishAd()
        bannerAd = null
    }

    override fun onAdLeftApplication(baseAd: BaseAd) {
        Log.d(TAG, "Ad routed user out of application {onAdLeftApplication}")
    }

    override fun onAdFailedToLoad(baseAd: BaseAd, adError: VungleError) {
        Log.d(TAG, "Ad failed to load {onAdFailedToLoad}")
    }

    override fun onAdFailedToPlay(baseAd: BaseAd, adError: VungleError) {
        Log.d(TAG, "Ad failed to play {onAdFailedToPlay}")
    }
}