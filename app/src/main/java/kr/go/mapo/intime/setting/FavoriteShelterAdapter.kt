package kr.go.mapo.intime.setting

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kr.go.mapo.intime.R
import kr.go.mapo.intime.map.model.Shelter
import kotlin.coroutines.CoroutineContext

class FavoriteShelterAdapter : ListAdapter<Shelter, FavoriteShelterAdapter.ItemViewHolder>(differ), CoroutineScope {

    private val job: Job = Job()
    private lateinit var context: Context

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(shelter: Shelter) {
            val titleFavoriteTextView = view.findViewById<TextView>(R.id.favoriteShelterPlaceTextView)

            val bookmarkButton = view.findViewById<Button>(R.id.favoriteShelterBookmarkButton)

            Log.d("ItemViewHolder", "$shelter")

            titleFavoriteTextView.text = shelter.placeName

            bookmarkButton.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(R.layout.item_favorite_shelter, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<Shelter>() {
            override fun areItemsTheSame(oldItem: Shelter, newItem: Shelter): Boolean {
                return oldItem.lat == newItem.lat
            }

            override fun areContentsTheSame(oldItem: Shelter, newItem: Shelter): Boolean {
                return oldItem == newItem
            }
        }
    }
}