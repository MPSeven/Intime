package kr.go.mapo.intime.map

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.R
import kr.go.mapo.intime.map.model.SortedAed
import java.lang.Exception
import kotlin.math.roundToInt

class AedListAdapter(fragmentManager: FragmentManager): ListAdapter<SortedAed, AedListAdapter.ItemViewHolder>(differ) {

    private lateinit var context: Context
    private var mFragmentManager: FragmentManager = fragmentManager

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(sortedAed: SortedAed) {
            val orgTextView = view.findViewById<TextView>(R.id.org)
            val addressTextView = view.findViewById<TextView>(R.id.address)
            val addressDetailTextView = view.findViewById<TextView>(R.id.addressDetail)
            val distanceTextView = view.findViewById<TextView>(R.id.distance)
            val findPathButton = view.findViewById<Button>(R.id.recyclerViewFindPathButton)
            val callButton = view.findViewById<Button>(R.id.recyclerViewCallButton)

            orgTextView.text = sortedAed.aed.org
            addressTextView.text = sortedAed.aed.address
            addressDetailTextView.text = sortedAed.aed.addressDetail
            distanceTextView.text = "${(sortedAed.distance * 1000).roundToInt()}m"

            findPathButton.setOnClickListener {
                val bottomSheetDialog = MapBottomSheetDialog(sortedAed)
                bottomSheetDialog.show(mFragmentManager, bottomSheetDialog.tag)
            }

            callButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${sortedAed.aed.tel}"))
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(R.layout.item_aed, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
        holder.view.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
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