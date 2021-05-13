package kr.go.mapo.intime

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.databinding.AmbulanceListBinding
import kr.go.mapo.intime.fragment.SosAmbFragment
import kr.go.mapo.intime.model.Ambulance

class AmbulanceAdapter(
    private val AmbulanceList: MutableList<Ambulance>,
    private val owner: SosAmbFragment
) : RecyclerView.Adapter<AmbulanceAdapter.AmbHolder>() {

    private lateinit var binding: AmbulanceListBinding

    inner class AmbHolder(view: View) : RecyclerView.ViewHolder(view){
        val ambName: TextView = view.findViewById(R.id.amb_company)
        val ambTel: TextView = view.findViewById(R.id.amb_tel)

        fun bind(item: Ambulance, view: View) {
            ambTel.text = item.telephone

            val pos = adapterPosition
            if(pos != RecyclerView.NO_POSITION) {
                view.setOnClickListener {
                    listener?.onItemClick(view, item, pos)
                    Log.d("여기여기", pos.toString())
                }
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(v:View, data: Ambulance, pos : Int)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmbHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.ambulance_list, parent, false)

        return AmbHolder(view)
    }

    override fun onBindViewHolder(holder: AmbHolder, position: Int) {
        val ambData = AmbulanceList[position]
        with(holder){
            ambName.text = ambData.company
            ambTel.text = ambData.telephone
        }

/*
        여기 to do
        1. recyclerview에 있는 버튼 어떻게 처리할지 생각해보고
        2. 버튼 누르면 전화 연결하기

            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${ambData()[0].telephone}")
            if(activity?.packageManager?.let { intent.resolveActivity(it) } != null){
                startActivity(intent)
            }
 */


    }

    override fun getItemCount() = AmbulanceList.size

}