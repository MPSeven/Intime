package kr.go.mapo.intime.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.AmbulanceAdapter
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.FragmentSosAmbBinding
import kr.go.mapo.intime.model.Ambulance

class SosAmbFragment : Fragment() {

    private lateinit var binding: FragmentSosAmbBinding

    private lateinit var recyclerview: RecyclerView
    private lateinit var amb: Ambulance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSosAmbBinding.inflate(inflater, container, false)

        recyclerview = binding.root.findViewById(R.id.amb_recycler_view)
        val adapter = AmbulanceAdapter(ambData(), this)

        recyclerview.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.VERTICAL,false)
        }


        return binding.root
    }

    private fun ambData() : MutableList<Ambulance>{
        val ambData = mutableListOf<Ambulance>()

        ambData.add(Ambulance("사회복지법인대한구조봉사회", "강남구", 34369111))
        ambData.add(Ambulance("(주)에스오에스", "강동구", 15660129))
        ambData.add(Ambulance("(주)한성응급환자이송단", "강동구", 4420129))
        ambData.add(Ambulance("㈜제일응급환자이송단", "강동구", 4261339))
        ambData.add(Ambulance("(주)서울911이송센터", "강서구", 15884129))
        ambData.add(Ambulance("㈜선응급환자이송", "금천구", 8534455))
        ambData.add(Ambulance("(주)서울129구급대", "노원구", 9710129))
        ambData.add(Ambulance("(주)9119구급센타", "도봉구", 9269119))
        ambData.add(Ambulance("쿱3119응급환자이송협동조합", "동대문구", 4810119))
        ambData.add(Ambulance("(주)대한응급환자이송단", "동작구", 8211119))
        ambData.add(Ambulance("(주)이엠에스응급환자이송센터", "마포구", 3131339))
        ambData.add(Ambulance("㈜24시응급환자이송단", "서대문구", 3040129))
        ambData.add(Ambulance("㈜이엠에스코리아", "서대문구", 4202114))
        ambData.add(Ambulance("㈜엠티케어", "서초구", 5951339))
        ambData.add(Ambulance("(주)129응급구조단", "성동구", 4561339))
        ambData.add(Ambulance("(주)코리아환자이송센터", "송파구", 4028129))
        ambData.add(Ambulance("㈜중앙응급환자이송단", "송파구", 4135129))
        ambData.add(Ambulance("(주)성심환자이송단", "양천구", 26911339))
        ambData.add(Ambulance("(주)천사구급센타", "양천구", 16668819))
        ambData.add(Ambulance("㈜전국응급환자이송단", "양천구", 26063114))
        ambData.add(Ambulance("(주)클로버이엠에스", "영등포구", 64050129))
        ambData.add(Ambulance("주식회사대한911구조대", "영등포구", 8320911))
        ambData.add(Ambulance("㈜한국응급구조대", "중랑구", 9489494))

        return  ambData
    }

}