package kr.go.mapo.intime.info.checklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.FragmentChecklistBasicBinding
import kr.go.mapo.intime.info.checklist.model.Checklist

class FragmentChecklistBasic : Fragment() {

    private var _binding: FragmentChecklistBasicBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerview: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChecklistBasicBinding.inflate(inflater, container, false)
        val root = binding.root

        recyclerview = binding.root.findViewById(R.id.ch_basic_rv)
        val adapter = ChecklistAdapter(chBasicData())
        recyclerview.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(binding.root.context,
            LinearLayoutManager.VERTICAL, false)
        }

        return root
    }

    private fun chBasicData(): MutableList<Checklist>{
        val chbData = mutableListOf<Checklist>()

        chbData.add(Checklist(1, "물", "음료 및 위생을 위해 며칠 동안 1 인당 하루 3.785L", 0, false))
        chbData.add(Checklist(2, "식품", "최소한 3 일분의 부패하지 않는 식품", 0, false))
        chbData.add(Checklist(3, "배터리 전원", "", 0, false))
        chbData.add(Checklist(4, "구급상자", "본인이 필요한 약 포함한 3 일분", 0, false))
        chbData.add(Checklist(5, "추가 배터리", "", 0, false))
        chbData.add(Checklist(6, "호루라기", "", 0, false))
        chbData.add(Checklist(7, "먼지 마스크", "", 0, false))
        chbData.add(Checklist(8, "플라스틱 시트 및 덕트 테이프", "대피용", 0, false))
        chbData.add(Checklist(9, "물티슈 혹은 간편 샤워타올, 쓰레기 봉투 및 플라스틱 끈", "", 0, false))
        chbData.add(Checklist(10, "렌치 또는 펜치", "유틸리티 끄기", 0, false))
        chbData.add(Checklist(11, "수동 캔 따개", "식품용", 0, false))
        chbData.add(Checklist(12, "지역지도", "", 0, false))

        return chbData
    }
}
