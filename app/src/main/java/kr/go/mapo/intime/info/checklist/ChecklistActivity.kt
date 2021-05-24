package kr.go.mapo.intime.info.checklist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kr.go.mapo.intime.databinding.ActivityChecklistBinding

class ChecklistActivity : AppCompatActivity() {

    val binding by lazy { ActivityChecklistBinding.inflate(layoutInflater) }
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        tabLayout = binding.chklistTab
        viewPager = binding.chklistTabContent

        val pagerAdapter = PagerFragmentStateAdapter(this).apply {
            addFragment(FragmentChecklistBasic())
            addFragment(FragmentChecklistExtra())
            addFragment(FragmentChecklistMine())
        }

        viewPager.apply {
            adapter = pagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }

        val tabLayoutTextArray = arrayOf("기본","추가물품","나의 리스트")

        TabLayoutMediator(tabLayout,viewPager){tab,position->
            tab.text = tabLayoutTextArray[position]
        }.attach()

        binding.chklistBack.setOnClickListener {
            onBackPressed()
        }
    }

    inner class ViewPagerAdapter(supportFragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(supportFragmentManager) {
        override fun getCount(): Int = 3
        override fun getItem(position: Int): Fragment {
            return when(position){
                0 ->  FragmentChecklistBasic()
                1 -> FragmentChecklistExtra()
                2 -> FragmentChecklistMine()
                else -> throw IndexOutOfBoundsException()
            }
        }
    }
}

