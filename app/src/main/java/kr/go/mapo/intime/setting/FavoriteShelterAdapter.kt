package kr.go.mapo.intime.setting

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.ItemFavoriteAedBinding
import kr.go.mapo.intime.databinding.ItemFavoriteShelterBinding
import kr.go.mapo.intime.map.model.Aed
import kr.go.mapo.intime.map.model.Shelter
import kr.go.mapo.intime.setting.database.DataBaseProvider
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

    inner class ItemViewHolder(private val binding: ItemFavoriteShelterBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shelter: Shelter) {

            Log.d("ItemViewHolder", "$shelter")

            binding.favoriteShelterPlaceTextView.text = shelter.placeName

            binding.favoriteShelterBookmarkButton.setOnClickListener {
                deleteShelter(shelter)
            }
        }

        private fun deleteShelter(shelter: Shelter) = launch {
            withContext(Dispatchers.IO) {
                val dao = DataBaseProvider.provideDB(context).bookmarkShelterDao()
                dao.delete(shelter.lat)
                Log.d("FavoriteAedAdapter", "delete aed")

                withContext(Dispatchers.Main) {
                    bookmarkToast("즐겨찾기가 해제되었습니다. ")
                    binding.favoriteShelterBookmarkButton.setBackgroundResource(R.drawable.map_bookmark_off)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = ItemFavoriteShelterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    private fun bookmarkToast(string: String) {
        val toast = Toast.makeText(context, "$string", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM, 0, 950)
        toast.show()
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