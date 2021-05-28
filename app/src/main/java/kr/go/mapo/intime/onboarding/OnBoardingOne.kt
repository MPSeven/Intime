package kr.go.mapo.intime.onboarding

import android.os.Bundle
import android.text.SpannableString
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.FragmentOnBoardingOneBinding

class OnBoardingOne : Fragment() {
    private lateinit var spannableString: SpannableString
    private var _binding: FragmentOnBoardingOneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOnBoardingOneBinding.inflate(inflater, container, false)

        activity?.findViewById<ViewPager2>(R.id.vp)
        
        // 건너뛰기 없어짐
/*        binding.onBoardingTbSkip.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_mainActivity)
        }*/

/*        textOneSpan()*/
        return binding.root

    }
//      볼드 텍스트 적용 필요 없게 됨: 
/*    private fun textOneSpan(){
        spannableString = SpannableString("일상 속 예상치 못한 응급상황!\n미리 대비하세요!")
        val tfBold = StyleSpan(Typeface.BOLD)
        spannableString.setSpan(tfBold, 12, 23, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        binding.onBoardingTvSlideHeader.text = spannableString
    }*/

}