package kr.go.mapo.intime.map

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.viewmodel.R
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kr.go.mapo.intime.databinding.ItemShelterDetailViewpagerBinding
import kr.go.mapo.intime.map.model.Aed
import kr.go.mapo.intime.map.model.Shelter
import kr.go.mapo.intime.map.model.SortedShelter
import kr.go.mapo.intime.setting.database.DataBaseProvider
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

class ShelterViewPagerAdapter(fragmentManager: FragmentManager) :
    androidx.recyclerview.widget.ListAdapter<SortedShelter, ShelterViewPagerAdapter.ShelterItemViewHolder>(
        differ
    ), CoroutineScope {

    private lateinit var context: Context
    private var mFragmentManager: FragmentManager = fragmentManager

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    inner class ShelterItemViewHolder(
        private val binding: ItemShelterDetailViewpagerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(sortedShelter: SortedShelter) = with(binding) {

            shelterOrgMarker.text = sortedShelter.shelter.placeName
            shelterAddressMarker.text = sortedShelter.shelter.address
            shelterCategoryMarker.text = sortedShelter.shelter.shelterCategory
            shelterDistanceMarker.text = "${(sortedShelter.distance * 1000).roundToInt()}m"

            launch {
                Log.d("viewPagerAdapter", "setLikeState")
                withContext(Dispatchers.IO) {
                    val repository = DataBaseProvider.provideDB(context).bookmarkShelterDao().getShelter(sortedShelter.shelter.lat)
                    Log.d("viewPagerAdapter", "repository: $repository")
                    val isLike = repository != null
                    Log.d("viewPagerAdapter", "isLike: $isLike")
                    withContext(Dispatchers.Main) {
                        setLikeImage(isLike)
                    }
                }
            }

        }

        fun bindViews(sortedShelter: SortedShelter) {
            binding.shelterFindPathButton.setOnClickListener {
                val bottomSheetDialog = MapShelterBottomSheetDialog(sortedShelter)
                bottomSheetDialog.show(mFragmentManager, bottomSheetDialog.tag)
            }

            binding.shelterCallNumber.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${sortedShelter.shelter.capacityArea}"))
                it.context.startActivity(intent)
            }

            binding.shelterBookMarkButton.setOnClickListener {
                // TODO DB
                setLikeState(sortedShelter.shelter)
            }
        }

        private fun setLikeState(shelter: Shelter) = launch {
            Log.d("viewPagerAdapter", "setLikeState")
            withContext(Dispatchers.IO) {
                val repository = DataBaseProvider.provideDB(context).bookmarkShelterDao().getShelter(shelter.lat)
                Log.d("viewPagerAdapter", "repository: $repository")
                val isLike = repository != null
                Log.d("viewPagerAdapter", "isLike: $isLike")

                likeRepository(shelter, isLike)

                withContext(Dispatchers.Main) {
                    setLikeImage(isLike)
                }
            }
        }

        private fun likeRepository(shelter: Shelter, isLike: Boolean) = launch {
            Log.d("viewpageradapter", "LikeRepository")
            withContext(Dispatchers.IO) {
                val dao = DataBaseProvider.provideDB(context).bookmarkShelterDao()
                if(isLike) {
                    dao.delete(shelter.lat)

                    withContext(Dispatchers.Main) {
                        bookmarkToast("즐겨찾기가 해제되었습니다. ")
                    }
                } else {
                    dao.insertShelter(shelter)

                    withContext(Dispatchers.Main) {
                        bookmarkToast("즐겨찾기에 추가되었습니다. ")
                    }
                }
                withContext(Dispatchers.Main) {
                    setLikeImage(isLike.not())
                }
            }
        }

        private fun setLikeImage(isLike: Boolean) = with(binding){
            Log.d("viewPagerAdapter", "setLikeImage")
            if(isLike) {
                binding.shelterBookMarkButton.setBackgroundResource(kr.go.mapo.intime.R.drawable.map_bookmark_on)

            } else {
                binding.shelterBookMarkButton.setBackgroundResource(kr.go.mapo.intime.R.drawable.map_bookmark_off)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShelterItemViewHolder {
        val view =
            ItemShelterDetailViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShelterItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShelterItemViewHolder, position: Int) {
        holder.bindData(currentList[position])
        holder.bindViews(currentList[position])
    }

    private fun bookmarkToast(string: String) {
        val toast = Toast.makeText(context, "$string", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM, 0, 550)
        toast.show()
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<SortedShelter>() {
            override fun areItemsTheSame(oldItem: SortedShelter, newItem: SortedShelter): Boolean {
                return oldItem.distance == newItem.distance
            }
            override fun areContentsTheSame(oldItem: SortedShelter, newItem: SortedShelter): Boolean {
                return oldItem == newItem
            }
        }
    }

}