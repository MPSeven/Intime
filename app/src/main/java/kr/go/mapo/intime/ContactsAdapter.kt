package kr.go.mapo.intime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        }
    }

    inner class ConHolder(view: View): RecyclerView.ViewHolder(view) {
        val conName: TextView = itemView.findViewById(R.id.con_name)
        val conNum: TextView = itemView.findViewById(R.id.con_phone)
    }
}