package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
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
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.fragment.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.FragmentNativeBinding

// Vungle Imports
import com.vungle.ads.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.logging.Handler

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
    private lateinit var nativeFragment: NativeFragment

    private var bannerAd: BannerAd? = null

    private var bannerPlacementId = "BANNER_TEST-0796908"
    private var MRECPlacementId = "MREC_TEST-7003332"
    private var bannerAdContainer: RelativeLayout? = null

    private var nativeAd: NativeAd? = null
    private var placementId = "NATIVE_TEST-5370128"

    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binds components of the relevant layout to a reference and sets content view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        interstitialFragment = InterstitialFragment.newInstance()
        rewardedFragment = RewardedVideoFragment.newInstance()
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
                nativeFragment.onAttach(applicationContext)
                initUIElements()
                startUpdates()
            }

            override fun onError(vungleError: VungleError) {
                Log.d(TAG, "onError(): ${vungleError.localizedMessage}")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        initUIElements()

    }

    private fun initUIElements() {
        binding.loadInterstitial.setOnClickListener {
            interstitialFragment.loadInterstitial()
            startUpdates()
        }
        binding.showInterstitial.setOnClickListener {
            interstitialFragment.playInterstitial()
        }
        binding.loadRv.setOnClickListener {
            rewardedFragment.loadRewardedVideo()
            startUpdates()
        }
        binding.showRv.setOnClickListener {
            rewardedFragment.playRewardedVideo()
        }
        binding.loadBn.setOnClickListener {
            loadBanner()
            startUpdates()
        }
        binding.showBn.setOnClickListener {
            playBanner()
            startUpdates()
        }
        binding.loadMrec.setOnClickListener {
            loadMREC()
            startUpdates()
        }
        binding.showMrec.setOnClickListener {
            playMREC()
        }
        binding.loadNative.setOnClickListener {
            // TODO: Currently loads the ad correctly but doesn't show it
            Log.d(TAG, "Clicked load native ad button")


            nativeAd = NativeAd(
                this@MainActivity, placementId
            ).apply {
                adListener = this@MainActivity
                load()
            }

            Log.d(MainActivity.TAG, "TRYING TO INFLATE VIEW")

            nativeAd?.let { nativeAd ->
                val layout = FragmentNativeBinding.inflate(LayoutInflater.from(applicationContext))

                with(layout) {
                    val nativeAdContainer =
                        binding.root.findViewById<LinearLayout>(R.id.nativeAdContainer)
                    nativeAdContainer.removeAllViews()
                    val clickableViews = mutableListOf(imgAdIcon, pnlVideoAd, btnAdCta)
                    lbAdTitle.text = nativeAd.getAdTitle()
                    lbAdBody.text = nativeAd.getAdBodyText()
                    lbAdRating.text = String.format("Rating: %s", nativeAd.getAdStarRating())
                    lbAdSponsor.text = nativeAd.getAdSponsoredText()
                    btnAdCta.text = nativeAd.getAdCallToActionText()
                    btnAdCta.isVisible = nativeAd.hasCallToAction()
                    nativeAd.registerViewForInteraction(
                        root, pnlVideoAd, imgAdIcon, clickableViews
                    )
                    nativeAdContainer.top = 0
                    nativeAdContainer.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                    nativeAdContainer.addView(root)
                }
            }
        }
        binding.closeBanner.setOnClickListener {
            Log.d(TAG, "Clicked play native ad button")
            bannerAdContainer?.removeAllViews()
            bannerAd?.finishAd()
            bannerAd = null
            startUpdates()
        }



        bannerAdContainer = binding.bannerAdContainer
    }

    fun checkButtonState() {
        if (bannerAd?.canPlayAd() == true) {
            binding.loadBn.isEnabled = false
            binding.loadMrec.isEnabled = false
            binding.showBn.isEnabled = true
            binding.showMrec.isEnabled = true
        } else {
            binding.loadBn.isEnabled = true
            binding.loadMrec.isEnabled = true
            binding.showBn.isEnabled = false
            binding.showMrec.isEnabled = false
        }

        if (rewardedFragment.rewardedReady() == true) {
            binding.loadRv.isEnabled = false
            binding.showRv.isEnabled = true
        } else {
            binding.loadRv.isEnabled = true
            binding.showRv.isEnabled = false
        }

        if (interstitialFragment.interstitialReady() == true) {
            binding.loadInterstitial.isEnabled = false
            binding.showInterstitial.isEnabled = true
        } else {
            binding.loadInterstitial.isEnabled = true
            binding.showInterstitial.isEnabled = false
        }
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
    }


    override fun onAdClicked(baseAd: BaseAd) {
        Log.d(TAG, "Ad was clicked {onAdClicked}")
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

    private fun NativeAd?.registerViewForInteraction(
        rootView: LinearLayout,
        mediaView: ImageView,
        adIconView: ImageView,
        clickableViews: MutableList<View>
    ) {

    }

    fun startUpdates() {
        MainScope().launch(Dispatchers.Main) {
            for (i in 5 downTo 1) {
                checkButtonState()
                delay(500)
            }
        }
    }
}