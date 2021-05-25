package kr.go.mapo.intime.setting

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.R
import kr.go.mapo.intime.map.model.Aed

class FavoriteAedAdapter: ListAdapter<Aed, FavoriteAedAdapter.ItemViewHolder>(differ) {

    inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(aed: Aed) {
            val titleFavoriteTextView = view.findViewById<TextView>(R.id.favoriteAedPlaceTextView)
            Log.d("ItemViewHolder", "$aed")
            titleFavoriteTextView.text = aed.org
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(R.layout.item_favorite_aed, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<Aed>() {
            override fun areItemsTheSame(oldItem: Aed, newItem: Aed): Boolean {
                return oldItem.lat == newItem.lat
            }

            override fun areContentsTheSame(oldItem: Aed, newItem: Aed): Boolean {
                return oldItem == newItem
            }
        }
    }
}