package kr.go.mapo.intime.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.ActivityTablayoutBinding

class FragmentCpr : Fragment() {
    private lateinit var cprAdapter:CprAdapter
    private lateinit var viewPager: ViewPager2
    private var _binding: ActivityTablayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityTablayoutBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cprAdapter = CprAdapter(this)
        viewPager = binding.viewPager
        viewPager.adapter = cprAdapter
        val tabLayout = binding.tabs
        TabLayoutMediator(tabLayout, viewPager){
            tab, position -> tab.text="OBJECT${(position+1)}"
        }.attach()

    }

    override fun onDestroyView() {
        _binding=null
        super.onDestroyView()
    }
}
class CprAdapter(fragment:Fragment):FragmentStateAdapter(fragment){
    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment {
        val fragment = CprObjectFragment()
        fragment.arguments = Bundle().apply{
            putInt(ARG_OBJECT, position+1)
        }
        return fragment
    }

}
private const val ARG_OBJECT = "object"
class CprObjectFragment:Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.item_cpr_adult, container, false)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        arguments?.takeIf{it.containsKey(ARG_OBJECT)}?.apply{
//            val textView: TextView = view.findViewById(android.R.id.text1)
//            textView.text = getInt(ARG_OBJECT).toString()
//        }
//    }

}
