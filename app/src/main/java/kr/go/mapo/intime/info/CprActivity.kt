package kr.go.mapo.intime.info

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.ActivityCprBinding
import kr.go.mapo.intime.onboarding.ViewPagerAdapter

class CprActivity: AppCompatActivity () {
    private val tabTextList = arrayListOf<String>("성인", "소아")
    private val fragmentList = arrayListOf<Fragment>(
        FragmentCpr(),
        FragmentCprChild()
    )
    private lateinit var adapter: CprAdapter
    private lateinit var viewPager: ViewPager2


    val binding by lazy { ActivityCprBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        setSupportActionBar(findViewById(R.id.cpr_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    private fun init(){
        viewPager= findViewById(R.id.cpr_viewPager)
        adapter = CprAdapter(this)
        viewPager.adapter= adapter
        val tabLayout = findViewById<TabLayout>(R.id.cpr_tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            viewPager.setCurrentItem(tab.position, true)
            tab.text = tabTextList[position]
        }.attach()
    }
}