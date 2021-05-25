package kr.go.mapo.intime.info.checklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kr.go.mapo.intime.databinding.FragmentChecklistExtraBinding
import kr.go.mapo.intime.info.checklist.model.Checklist

class FragmentChecklistExtra : Fragment() {

    private var _binding: FragmentChecklistExtraBinding? = null
    private val binding get() = _binding!!
    private val childAdapter = ChecklistAdapter(chChildData())
    private val womenAdapter = ChecklistAdapter(chWomenData())
    private val medicineAdapter = ChecklistAdapter(chMedicineData())
    private val petAdapter = ChecklistAdapter(chPetData())
    private val valAdapter = ChecklistAdapter(chValData())
    private val clothingAdapter = ChecklistAdapter(chClothingData())
    private val etcAdapter = ChecklistAdapter(chEtcData())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChecklistExtraBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.chExtraChildrv.adapter = childAdapter
        binding.chExtraChildrv.layoutManager = LinearLayoutManager(activity)
        binding.chExtraWomenrv.adapter = womenAdapter
        binding.chExtraWomenrv.layoutManager = LinearLayoutManager(activity)
        binding.chExtraMedicinerv.adapter = medicineAdapter
        binding.chExtraMedicinerv.layoutManager = LinearLayoutManager(activity)
        binding.chExtraPetrv.adapter = petAdapter
        binding.chExtraPetrv.layoutManager = LinearLayoutManager(activity)
        binding.chExtraValuablesrv.adapter = valAdapter
        binding.chExtraValuablesrv.layoutManager = LinearLayoutManager(activity)
        binding.chExtraClothingrv.adapter = clothingAdapter
        binding.chExtraClothingrv.layoutManager = LinearLayoutManager(activity)
        binding.chExtraEtcrv.adapter = etcAdapter
        binding.chExtraEtcrv.layoutManager = LinearLayoutManager(activity)

        return root
    }

    private fun chChildData(): MutableList<Checklist>{
        val chChildData = mutableListOf<Checklist>()

        chChildData.add(Checklist(13, "마스크", "", 1, false))
        chChildData.add(Checklist(14, "유아용 조제 분유", "", 1, false))
        chChildData.add(Checklist(15, "젖병", "", 1, false))
        chChildData.add(Checklist(16, "기저귀", "", 1, false))
        chChildData.add(Checklist(17, "물티슈 및 기저귀 발진 크림", "", 1, false))

        return chChildData
    }
    private fun chWomenData(): MutableList<Checklist>{
        val chWomenData = mutableListOf<Checklist>()

        chWomenData.add(Checklist(18, "여성 용품", "", 2, false))
        chWomenData.add(Checklist(19, "개인 위생 용품", "", 2, false))

        return chWomenData
    }
    private fun chMedicineData(): MutableList<Checklist>{
        val chMedicineData = mutableListOf<Checklist>()

        chMedicineData.add(Checklist(20, "처방약", "", 3, false))
        chMedicineData.add(Checklist(21, "비처방약", "진통제, 설사약, 제산제, 완하제", 3, false))

        return chMedicineData
    }
    private fun chPetData(): MutableList<Checklist>{
        val chPetData = mutableListOf<Checklist>()

        chPetData.add(Checklist(22, "사료", "", 4, false))
        chPetData.add(Checklist(23, "물", "", 4, false))

        return chPetData
    }
    private fun chValData(): MutableList<Checklist>{
        val chValData = mutableListOf<Checklist>()

        chValData.add(Checklist(24, "현금, 수표", "", 5, false))
        chValData.add(Checklist(25, "귀중 문서", "보험 정책 사본, 신분증 및 은행 계좌 기록과 같은 중요한 가족 문서는 전자적으로 저장되거나 방수, 휴대용 용기에 저장", 5, false))

        return chValData
    }
    private fun chClothingData(): MutableList<Checklist>{
        val chClothingData = mutableListOf<Checklist>()

        chClothingData.add(Checklist(26, "침낭, 담뇨", "", 6, false))
        chClothingData.add(Checklist(27, "기후에 맞는 옷", "", 6, false))
        chClothingData.add(Checklist(28, "튼튼한 신발", "", 6, false))

        return chClothingData
    }
    private fun chEtcData(): MutableList<Checklist>{
        val chEtcData = mutableListOf<Checklist>()

        chEtcData.add(Checklist(29, "소독용품", "비누, 손 소독제, 표면 소독 용 물티슈", 7, false))
        chEtcData.add(Checklist(30, "소화기", "", 7, false))
        chEtcData.add(Checklist(31, "방수용기", "", 7, false))
        chEtcData.add(Checklist(32, "메스 키트", "", 7, false))
        chEtcData.add(Checklist(33, "식기", "종이컵, 접시, 종이타월, 플라스틱 식기", 7, false))
        chEtcData.add(Checklist(34, "종이, 연필", "", 7, false))
        chEtcData.add(Checklist(35, "책, 게임, 퍼즐", "", 7, false))

        return chEtcData
    }
}
