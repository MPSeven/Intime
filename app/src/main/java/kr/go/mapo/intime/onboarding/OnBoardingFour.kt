package kr.go.mapo.intime.onboarding

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import kr.go.mapo.intime.R
class OnBoardingFour : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_on_boarding_four, container, false)
        val vP = activity?.findViewById<ViewPager2>(R.id.vp)
        view.findViewById<ImageButton>(R.id.onboarding_ib_start).setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_mainActivity)
            onBoardingFinished()
        }
        return view
    }
    private fun onBoardingFinished(){
        val sharedPref= requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()

    }

}