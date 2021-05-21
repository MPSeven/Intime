package kr.go.mapo.intime.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.AmbulanceAdapter
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.DialogLocationPickerBinding
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

//        binding.amdLoc.setOnClickListener {
//            callLocationPicker(binding.root)
//        }

        return binding.root
    }

    private fun ambData() : MutableList<Ambulance>{
        val ambData = mutableListOf<Ambulance>()

        ambData.add(Ambulance("사회복지법인대한구조봉사회", "강남구", "02-3436-9111"))
        ambData.add(Ambulance("(주)에스오에스", "강동구", "1566-0129"))
        ambData.add(Ambulance("(주)한성응급환자이송단", "강동구", "02-442-0129"))
        ambData.add(Ambulance("㈜제일응급환자이송단", "강동구", "02-426-1339"))
        ambData.add(Ambulance("(주)서울911이송센터", "강서구", "1588-4129"))
        ambData.add(Ambulance("㈜선응급환자이송", "금천구", "02-853-4455"))
        ambData.add(Ambulance("(주)서울129구급대", "노원구", "02-971-0129"))
        ambData.add(Ambulance("(주)9119구급센타", "도봉구", "02-926-9119"))
        ambData.add(Ambulance("쿱3119응급환자이송협동조합", "동대문구", "02-481-0119"))
        ambData.add(Ambulance("(주)대한응급환자이송단", "동작구", "02-821-1119"))
        ambData.add(Ambulance("(주)이엠에스응급환자이송센터", "마포구", "02-313-1339"))
        ambData.add(Ambulance("㈜24시응급환자이송단", "서대문구", "02-304-0129"))
        ambData.add(Ambulance("㈜이엠에스코리아", "서대문구", "02-420-2114"))
        ambData.add(Ambulance("㈜엠티케어", "서초구", "02-595-1339"))
        ambData.add(Ambulance("(주)129응급구조단", "성동구", "02-456-1339"))
        ambData.add(Ambulance("(주)코리아환자이송센터", "송파구", "02-402-8129"))
        ambData.add(Ambulance("㈜중앙응급환자이송단", "송파구", "02-413-5129"))
        ambData.add(Ambulance("(주)성심환자이송단", "양천구", "02-2691-1339"))
        ambData.add(Ambulance("(주)천사구급센타", "양천구", "02-1666-8819"))
        ambData.add(Ambulance("㈜전국응급환자이송단", "양천구", "02-2606-3114"))
        ambData.add(Ambulance("(주)클로버이엠에스", "영등포구", "02-6405-0129"))
        ambData.add(Ambulance("주식회사대한911구조대", "영등포구", "02-832-0911"))
        ambData.add(Ambulance("㈜한국응급구조대", "중랑구", "02-948-9494"))

        return  ambData
    }


    /*
        여기 to do
        클릭->넘버피커 다이얼로그 띄움 -> 로케이션 선택 ->앰뷸런스리스트 분기처리

        */

    private fun callLocationPicker(view: ViewGroup) {
        val inflater: LayoutInflater = requireActivity().layoutInflater
        val dialog: DialogLocationPickerBinding =
            DialogLocationPickerBinding.inflate(inflater, view, false)

//        val locationPicker: NumberPicker = dialog.picker.apply {
//            minValue = 0
//            maxValue = 25
//            wrapSelectorWheel = false
//            setOnValueChangedListener { _, _, newVal -> }
//        }
        val dialogBuilder: AlertDialog.Builder =
            AlertDialog.Builder(requireActivity()).apply {
                setView(dialog.root)

            }
        val alertLocationPickerDialog: AlertDialog = dialogBuilder.create()
        alertLocationPickerDialog.show()
    }




}