package kr.go.mapo.intime.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.R
import kr.go.mapo.intime.model.Contacts

class ContactsAdapter(private val ContactsList: MutableList<Contacts>): RecyclerView.Adapter<ContactsAdapter.ConHolder>() {

    override fun getItemCount() = ContactsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_list, parent,false)
        return ConHolder(view)
    }

    override fun onBindViewHolder(holder: ConHolder, position: Int) {
        val conData = ContactsList[position]

        with(holder){
            conName.text = conData.name
            conNum.text = conData.phoneNumber
            conChk.isChecked = conData.chk

            if(conData.chk) {
                conChk.visibility = View.VISIBLE
                conChTxt.visibility = View.VISIBLE
            }
        }

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.apply {
            ConHolder(itemView)
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class ConHolder(view: View): RecyclerView.ViewHolder(view) {
        val conName: TextView = itemView.findViewById(R.id.con_name)
        val conNum: TextView = itemView.findViewById(R.id.con_phone)
        val conChk: CheckBox = itemView.findViewById(R.id.con_chk)
        val conChTxt: TextView = itemView.findViewById(R.id.con_chk_txt)
    }
}