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
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.R
import kr.go.mapo.intime.map.model.SortedShelter
import java.lang.Exception
import kotlin.math.roundToInt

class ShelterListAdapter(fragmentManager: FragmentManager) :
    ListAdapter<SortedShelter, ShelterListAdapter.ShelterItemViewHolder>(differ) {

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

    inner class ShelterItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(sortedShelter: SortedShelter) {
            val orgTextView = view.findViewById<TextView>(R.id.orgShelter)
            val addressTextView = view.findViewById<TextView>(R.id.addressShelter)
            val addressDetailTextView = view.findViewById<TextView>(R.id.addressCategoryShelter)
            val distanceTextView = view.findViewById<TextView>(R.id.distanceShelter)
            val findPathButton = view.findViewById<Button>(R.id.shelterRecyclerViewFindPathButton)
            val callButton = view.findViewById<Button>(R.id.shelterRecyclerViewCallButton)


            orgTextView.text = sortedShelter.shelter.placeName
            addressTextView.text = sortedShelter.shelter.address
            addressDetailTextView.text = sortedShelter.shelter.shelterCategory
            distanceTextView.text = "${(sortedShelter.distance * 1000).roundToInt()}m"

            view.setOnClickListener {
                Log.d("View setOnClickListener", "${getItem(adapterPosition)}")
            }

            findPathButton.setOnClickListener {
                val bottomSheetDialog = MapShelterBottomSheetDialog(sortedShelter)
                bottomSheetDialog.show(mFragmentManager, bottomSheetDialog.tag)
            }

            callButton.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:${sortedShelter.shelter.capacityArea}")
                )
                it.context.startActivity(intent)
                // todo ACTION_CALL
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShelterItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ShelterItemViewHolder(inflater.inflate(R.layout.item_shelter, parent, false))
    }

    override fun onBindViewHolder(holder: ShelterItemViewHolder, position: Int) {
        holder.bind(currentList[position])
        holder.view.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<SortedShelter>() {
            override fun areItemsTheSame(oldItem: SortedShelter, newItem: SortedShelter): Boolean {
                return oldItem.distance == newItem.distance
            }

            override fun areContentsTheSame(
                oldItem: SortedShelter,
                newItem: SortedShelter
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}