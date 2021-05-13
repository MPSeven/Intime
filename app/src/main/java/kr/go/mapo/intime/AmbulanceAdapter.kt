package kr.go.mapo.intime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.fragment.SosAmbFragment
import kr.go.mapo.intime.model.Ambulance

class AmbulanceAdapter(
    private val AmbulanceList: MutableList<Ambulance>,
    private val owner: SosAmbFragment
) : RecyclerView.Adapter<AmbulanceAdapter.AmbHolder>() {

    inner class AmbHolder(rowRoot: View) : RecyclerView.ViewHolder(rowRoot){
        val ambName: TextView = rowRoot.findViewById(R.id.amb_company)
        val ambTel: TextView = rowRoot.findViewById(R.id.amb_tel)
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
    }

    override fun getItemCount() = AmbulanceList.size

}