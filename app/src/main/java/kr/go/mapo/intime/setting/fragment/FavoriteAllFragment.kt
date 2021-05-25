package kr.go.mapo.intime.setting.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kr.go.mapo.intime.R
import java.lang.reflect.Array.getInt

class FavoriteAllFragment : Fragment() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_all, container, false)
    }

}

//class FavoriteCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
//    override fun getItemCount(): Int = 100
//
//    override fun createFragment(position: Int): Fragment {
//        val fragment = FavoriteObjectFragment()
//        fragment.arguments = Bundle().apply {
//            putInt(ARG_OBJECT, position + 1)
//        }
//        return fragment
//    }
//}

