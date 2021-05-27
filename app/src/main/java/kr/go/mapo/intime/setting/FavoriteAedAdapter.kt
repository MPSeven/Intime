package kr.go.mapo.intime.setting

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.ItemDetailViewpagerBinding
import kr.go.mapo.intime.databinding.ItemFavoriteAedBinding
import kr.go.mapo.intime.map.model.Aed
import kr.go.mapo.intime.setting.database.DataBaseProvider
import kotlin.coroutines.CoroutineContext

class FavoriteAedAdapter: ListAdapter<Aed, FavoriteAedAdapter.ItemViewHolder>(differ), CoroutineScope {

    private val job: Job = Job()
    private lateinit var context: Context

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    inner class ItemViewHolder(private val binding: ItemFavoriteAedBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(aed: Aed) {
            val titleFavoriteTextView = binding.favoriteAedPlaceTextView

            val bookmarkButton = binding.favoriteAedBookmarkButton

            Log.d("ItemViewHolder", "$aed")

            titleFavoriteTextView.text = aed.org
            binding.favoriteAedAddress.text = aed.address
            binding.favorriteAedAddressDetail.text = aed.addressDetail

            bookmarkButton.setOnClickListener {
                deleteAed(aed)
            }
        }

        private fun deleteAed(aed: Aed) = launch {
            withContext(Dispatchers.IO) {
                val dao = DataBaseProvider.provideDB(context).bookmarkAedDao()
                dao.delete(aed.lat)
                Log.d("FavoriteAedAdapter", "delete aed")

                withContext(Dispatchers.Main) {
                    bookmarkToast("즐겨찾기가 해제되었습니다. ")
                    binding.favoriteAedBookmarkButton.setBackgroundResource(R.drawable.map_bookmark_off)
                }
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = ItemFavoriteAedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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