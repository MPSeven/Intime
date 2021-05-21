package kr.go.mapo.intime.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import kr.go.mapo.intime.databinding.ActivityTablayoutChecklistBinding
import java.lang.IllegalStateException

class FragmentChecklist : Fragment() {
    private val tabTextList = arrayListOf<String>("기본", "추가물품", "나의 리스트")
    private var _binding: ActivityTablayoutChecklistBinding ?= null
    private val binding get() = _binding!!
    /*    private var _binding: ActivityTablayoutChecklistBinding? = null
        private val binding get() = _binding!!*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTablayoutChecklistBinding.inflate(layoutInflater)
        binding.root

        val checklistAdapter = ChecklistAdapter(this.fragmentManager)
        val viewPager: ViewPager = binding.tabContent
        viewPager.adapter = checklistAdapter
        val tabLayout = binding.tabLayoutChecklist
        tabLayout.setupWithViewPager(viewPager)
/*        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = tabTextList[position]
        }.attach()*/
    }
}

class ChecklistAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
    override fun getCount(): Int = 3
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FragmentChecklistBasic.newInstance()
            1 -> fragment = FragmentChecklistExtra.newInstance()
            2 -> fragment = FragmentChecklistMine.newInstance()
            else -> throw IllegalStateException()
        }
        return fragment
    }

/*    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> FragmentChecklistBasic()
            2 -> FragmentChecklistExtra()
            else -> FragmentChecklistMine()
        }
    }*/
}

