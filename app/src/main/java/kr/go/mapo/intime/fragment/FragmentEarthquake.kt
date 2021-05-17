package kr.go.mapo.intime.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.FragmentEarthquakeBinding

class FragmentEarthquake : Fragment(R.layout.fragment_earthquake) {
    private var _binding:FragmentEarthquakeBinding?=null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEarthquakeBinding.inflate(inflater, container, false)
//        val t:String = "떨어지는 낙하물을 최대한 피할 수 있도록 튼튼한 <b>탁자 아래</b>에 들어가서 탁자 다리를 꼭 잡고 몸을 보호합니다.\n\n\n" +
//                "        흔들림이 멈추면 <b>가스와 전깃불을 차단</b>하고 문을 열어 <b>출구를 확보</b>한 후 (계단을 이용하여) 밖으로 나갑니다. (엘리베이터 사용 금지)\n\n\n" +
//                "        바깥에서 손이나 가방 등으로 머리를 보호하며, <b>운동장, 공원 등 넓은 공간으로 대피</b>합니다.\n\n\n" +
//                "        대피 장소에 도착한 후 라디오나 공공기관 등 <b>안내 방송</b>에 따라 행동합니다."
//
//        binding.infoTvEarthquakeBody1.setText(Html.fromHtml(t, Html.FROM_HTML_MODE_COMPACT))

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
