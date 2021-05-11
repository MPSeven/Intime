package kr.go.mapo.intime

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import kr.go.mapo.intime.model.SortedAed
import org.w3c.dom.Text
import kotlin.math.roundToInt

class AedListAdapter : ListAdapter<SortedAed, AedListAdapter.ItemViewHolder>(differ) {

    inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(sortedAed: SortedAed) {
            val addressTextView = view.findViewById<TextView>(R.id.address)
            val addressDetailTextView = view.findViewById<TextView>(R.id.addressDetail)
            val tel = view.findViewById<TextView>(R.id.tel)
            val distance = view.findViewById<TextView>(R.id.distance)

            addressTextView.text = sortedAed.aed.address
            addressDetailTextView.text = sortedAed.aed.addressDetail
            tel.text = sortedAed.aed.tel
            distance.text = "${(sortedAed.distance * 1000).roundToInt()}m"

            view.setOnClickListener {
                Log.d("View setOnClickListener", "${getItem(adapterPosition)}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(R.layout.item_aed, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<SortedAed>() {
            override fun areItemsTheSame(oldItem: SortedAed, newItem: SortedAed): Boolean {
                return oldItem.distance == newItem.distance
            }

            override fun areContentsTheSame(oldItem: SortedAed, newItem: SortedAed): Boolean {
                return oldItem == newItem
            }

        }
    }
}