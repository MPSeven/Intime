package kr.go.mapo.intime.sos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.ActivityAmbulanceBinding
import kr.go.mapo.intime.sos.model.Ambulance

class AmbulanceActivity : AppCompatActivity() {

    val binding by lazy { ActivityAmbulanceBinding.inflate(layoutInflater) }
    private lateinit var recyclerview: RecyclerView
    private var type = "전체"
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recyclerview = binding.root.findViewById(R.id.amb_recycler_view)
        val adapter = AmbulanceAdapter(ambData(), this)

        recyclerview.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(binding.root.context,
                LinearLayoutManager.VERTICAL,false)
        }
//        setSpinner()

        binding.ambBack.setOnClickListener{
            onBackPressed()
        }

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


//    private fun setSpinner() {
//        val spinner = binding.ambSpinner
//        ArrayAdapter.createFromResource(
//            this, R.array.location, android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            spinner?.adapter = adapter
//        }
//
//        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                type = spinner?.selectedItem.toString()
//            }
//        }
//    }

}
