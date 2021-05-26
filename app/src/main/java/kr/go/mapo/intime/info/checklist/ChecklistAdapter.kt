package kr.go.mapo.intime.info.checklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.R
import kr.go.mapo.intime.info.checklist.model.Checklist
import kr.go.mapo.intime.sos.model.Ambulance

class ChecklistAdapter(
    private val ChecklistList: MutableList<Checklist>
): RecyclerView.Adapter<ChecklistAdapter.ChkHolder>() {

    interface OnItemClickListener{
        fun onItemClick(v: View, data: Ambulance, pos : Int)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = ChecklistList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistAdapter.ChkHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_checklist, parent, false)

        return ChkHolder(view)
    }

    override fun onBindViewHolder(holder: ChkHolder, position: Int) {
        val chbData = ChecklistList[position]

        with(holder){
            title.text = chbData.contents
            desc.text = chbData.description
            if(chbData.chk){
                chk.isChecked = chbData.chk
            }
        }
    }

    inner class ChkHolder(view: View): RecyclerView.ViewHolder(view){

        val title: TextView = itemView.findViewById(R.id.cb_title)
        val desc: TextView = itemView.findViewById(R.id.cb_detail)
        val chk: CheckBox = itemView.findViewById(R.id.cb_chk)
    }
}