package kr.go.mapo.intime.onboarding

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_on_boarding_one.*
import kotlinx.android.synthetic.main.fragment_on_boarding_one.view.*
import kr.go.mapo.intime.R

class OnBoardingOne : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_boarding_one, container, false)
        activity?.findViewById<ViewPager2>(R.id.vp)
        view.findViewById<Button>(R.id.onBoarding_tb_skip).setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_mainActivity)
        }
        return view
    }

}