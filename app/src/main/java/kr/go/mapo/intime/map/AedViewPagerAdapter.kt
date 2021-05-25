package kr.go.mapo.intime.map

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.FragmentMapBinding
import kr.go.mapo.intime.setting.database.DataBaseProvider
import kr.go.mapo.intime.databinding.ItemDetailViewpagerBinding
import kr.go.mapo.intime.map.model.Aed
import kr.go.mapo.intime.map.model.SortedAed
import kr.go.mapo.intime.setting.dao.BookmarkAedDao
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

class AedViewPagerAdapter :
    androidx.recyclerview.widget.ListAdapter<SortedAed, AedViewPagerAdapter.ItemViewHolder>(differ),
    CoroutineScope {

    private lateinit var context: Context

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    inner class ItemViewHolder(
        private val binding: ItemDetailViewpagerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(sortedAed: SortedAed) = with(binding) {
            orgMarker.text = sortedAed.aed.org
            addressMarker.text = sortedAed.aed.address
            addressDetailMarker.text = sortedAed.aed.addressDetail
            distanceMarker.text = "${(sortedAed.distance * 1000).roundToInt()}m"

            launch {
                Log.d("viewPagerAdapter", "setLikeState")
                withContext(Dispatchers.IO) {
                    val repository = DataBaseProvider.provideDB(context).bookmarkAedDao().getAed(sortedAed.aed.lat)
                    Log.d("viewPagerAdapter", "repository: $repository")
                    val isLike = repository != null
                    Log.d("viewPagerAdapter", "isLike: $isLike")
                    withContext(Dispatchers.Main) {
                        setLikeImage(isLike)
                    }
                }
            }
        }

        fun bindViews(sortedAed: SortedAed) {
            binding.findPathButton.setOnClickListener {
                try {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("nmap://route/walk?dlat=${sortedAed.aed.lat}&dlng=${sortedAed.aed.lon}&dname=${sortedAed.aed.org}")
                    ).apply {
                        `package` = "com.nhn.android.nmap"
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    it.context.startActivity(intent)
                } catch (e: Exception) {
                    val intentPlayStore = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$NAVER_MAP_PACKAGE_NAME")
                    )
                    it.context.startActivity(intentPlayStore)
                }
            }

            binding.callNumber.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${sortedAed.aed.tel}"))
                it.context.startActivity(intent)
                // todo ACTION_CALL
//                val permissionCheck = ContextCompat.checkSelfPermission(it.context, Manifest.permission.CALL_PHONE)
//                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
//                    ActivityCompat.requestPermissions()
//                    Toast.makeText(it.context, "permissionDenied!", Toast.LENGTH_SHORT).show()
//                } else {
//
//                }
            }

            binding.bookMarkButton.setOnClickListener {
                setLikeState(sortedAed.aed)
            }

        }

        private fun setLikeState(aed: Aed) = launch {
            Log.d("viewPagerAdapter", "setLikeState")
            withContext(Dispatchers.IO) {
                val repository = DataBaseProvider.provideDB(context).bookmarkAedDao().getAed(aed.lat)
                Log.d("viewPagerAdapter", "repository: $repository")
                val isLike = repository != null
                Log.d("viewPagerAdapter", "isLike: $isLike")

                likeRepository(aed, isLike)

                withContext(Dispatchers.Main) {
                    setLikeImage(isLike)
                }
            }
        }

        private fun likeRepository(aed: Aed, isLike: Boolean) = launch {
            Log.d("viewpageradapter", "LikeRepository")
            withContext(Dispatchers.IO) {
                val dao = DataBaseProvider.provideDB(context).bookmarkAedDao()
                if(isLike) {
                    dao.delete(aed.lat)
                    Log.d("viewPagerAdapter", "delete aed")
                    val db = dao.getAll()
                    Log.d("viewPagerAdapter", "$db")
                } else {
                    dao.insertAed(aed)
                    Log.d("viewPagerAdapter", "insert aed")
                    val db = dao.getAll()
                    Log.d("viewPagerAdapter", "$db")
                }
                withContext(Dispatchers.Main) {
                    setLikeImage(isLike.not())
                }
            }
        }

        private fun setLikeImage(isLike: Boolean) = with(binding){
            Log.d("viewPagerAdapter", "setLikeImage")
            if(isLike) {
                val img: Drawable? =
                    bookMarkButton.context.resources.getDrawable(kr.go.mapo.intime.R.drawable.map_bookmark_on)
                img?.setBounds(0, 0, 80, 80)
                bookMarkButton.setCompoundDrawables(img, null, null, null)
            } else {
                val img: Drawable? =
                    bookMarkButton.context.resources.getDrawable(kr.go.mapo.intime.R.drawable.map_bookmark_off)
                img?.setBounds(0, 0, 80, 80)
                binding.bookMarkButton.setCompoundDrawables(img, null, null, null)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {

        val view =
            ItemDetailViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindData(currentList[position])
        holder.bindViews(currentList[position])
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
        private const val NAVER_MAP_PACKAGE_NAME = "com.nhn.android.nmap"
    }

}