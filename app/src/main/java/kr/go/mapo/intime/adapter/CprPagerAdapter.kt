package kr.go.mapo.intime.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.go.mapo.intime.fragment.*

private const val NUM_ITEMS: Int = 2

class CprPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> return FragmentCpr()
            1 -> return FragmentCprChild()
            else -> InfoFragment()
        }

    }

}