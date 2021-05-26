package kr.go.mapo.intime.onboarding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import kr.go.mapo.intime.R
import me.relex.circleindicator.CircleIndicator3

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
//            OnBoardingThree(),
            OnBoardingFour()
        )
        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        val viewpager: ViewPager2 = view.findViewById(R.id.vp)
        viewpager.adapter = adapter
        val indicator: CircleIndicator3 = view.findViewById(R.id.indicator)
        indicator.createIndicators(2, 0)
        indicator.animatePageSelected(2)
        indicator.setViewPager(viewpager)
        return view
    }

/*    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewpager: ViewPager2 = view.findViewById(R.id.vp)
        Log.INFO
        val mAdapter = SampleRecyclerAdapter(3)
        viewpager.adapter = mAdapter

        val indicator: CircleIndicator3 = view.findViewById(R.id.indicator)
        indicator.setViewPager(viewpager)
    }*/

}