package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.vungle.ads.BaseAd
import com.vungle.ads.BaseAdListener
import androidx.fragment.app.Fragment

import com.vungle.ads.NativeAd
import com.vungle.ads.VungleError
import com.example.myapplication.databinding.FragmentNativeBinding


class NativeFragment : Fragment(R.layout.fragment_native), BaseAdListener {

    companion object {
        fun newInstance() = NativeFragment()
    }

    private lateinit var viewModel: FullScreenViewModel
    private var nativeAd: NativeAd? = null
    private var placementId = "NATIVE_TEST-5370128"

    private lateinit var binding: FragmentNativeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(MainActivity.TAG, "GOT TO CREATEVIEW")
        return inflater.inflate(R.layout.fragment_native, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[FullScreenViewModel::class.java]
//         TODO: Use the ViewModel

    }

    fun loadNative() {
        nativeAd = NativeAd(
            requireActivity(), placementId
        ).apply {
            adListener = this@NativeFragment
            load()
        }

        binding = FragmentNativeBinding.inflate(layoutInflater)

        Log.d(MainActivity.TAG, "TRYING TO INFLATE VIEW")

        nativeAd?.let { nativeAd ->
            val layout = FragmentNativeBinding.inflate(LayoutInflater.from(requireContext()))

            with(layout) {
                val nativeAdContainer = binding.root.findViewById<LinearLayout>(R.id.nativeAdContainer)
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
                nativeAdContainer.layoutDirection = View.LAYOUT_DIRECTION_INHERIT
                nativeAdContainer.visibility = View.VISIBLE
                nativeAdContainer.addView(root)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
        nativeAd?.unregisterView()

        var intent = Intent(getContext(), MainActivity::class.java)
        startActivity(intent)
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

private fun <ViewParent> LinearLayout.addView(parent: ViewParent?) {

}

private fun NativeAd?.registerViewForInteraction(rootView: LinearLayout, mediaView: ImageView, adIconView: ImageView, clickableViews: MutableList<View>) {

}
