package kr.go.mapo.intime.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import kr.go.mapo.intime.R

class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)
        val fragmentList = arrayListOf<Fragment>(
            OnBoardingOne(),
            OnBoardingTwo(),
            OnBoardingThree(),
            OnBoardingFour()
        )
        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        view.findViewById<ViewPager2>(R.id.vp).adapter = adapter
        return view
    }

}