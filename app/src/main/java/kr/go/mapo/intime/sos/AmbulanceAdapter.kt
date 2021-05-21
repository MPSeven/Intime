package kr.go.mapo.intime.sos

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.AmbulanceListBinding
import kr.go.mapo.intime.sos.model.Ambulance

class AmbulanceAdapter(
    private val AmbulanceList: MutableList<Ambulance>,
    private val owner: SosAmbFragment
) : RecyclerView.Adapter<AmbulanceAdapter.AmbHolder>() {

    private lateinit var binding: AmbulanceListBinding

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

    override fun getItemCount() = AmbulanceList.size

    override fun onBindViewHolder(holder: AmbHolder, position: Int) {
        val ambData = AmbulanceList[position]

        with(holder){
            ambName.text = ambData.company
            ambTel.text = ambData.telephone
            ambLocation.text = ambData.location

            ambBtn.setOnClickListener {

                var intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${ambData.telephone}"))

                it.context.startActivity(intent)
            }
        }
    }



    inner class AmbHolder(view: View) : RecyclerView.ViewHolder(view){
        val ambName: TextView = itemView.findViewById(R.id.amb_company)
        val ambTel: TextView = itemView.findViewById(R.id.amb_tel)
        val ambLocation: TextView = itemView.findViewById(R.id.amb_location)
        val ambBtn: ImageButton = itemView.findViewById(R.id.amb_tel_btn)

    }

}