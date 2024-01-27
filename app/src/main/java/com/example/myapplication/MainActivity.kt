package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


class MainActivity : AppCompatActivity() {
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
            bannerFragment.loadBanner()
        }
        binding.showBn.setOnClickListener {
            bannerFragment.playBanner()
        }
        binding.loadMrec.setOnClickListener {
            bannerFragment.loadMREC()
        }
        binding.showMrec.setOnClickListener{
            bannerFragment.playMREC()
        }
        binding.loadNative.setOnClickListener {
            Log.d(TAG, "Clicked load native ad button")
//            nativeFragment.loadNativeAd()
        }
        binding.showNative.setOnClickListener {
            Log.d(TAG, "Clicked play native ad button")
//            nativeFragment.showNativeAd()
        }
    }
}