package kr.go.mapo.intime.map

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.go.mapo.intime.R
import kr.go.mapo.intime.setting.database.DataBaseProvider
import kr.go.mapo.intime.databinding.ItemDetailViewpagerBinding
import kr.go.mapo.intime.map.model.SortedAed
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

class AedViewPagerAdapter :
    androidx.recyclerview.widget.ListAdapter<SortedAed, AedViewPagerAdapter.ItemViewHolder>(differ),
    CoroutineScope {

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

            if (sortedAed.aed.checked) {
                val img: Drawable? =
                    findPathButton.context.resources.getDrawable(R.drawable.map_bookmark_on)
                img?.setBounds(0, 0, 80, 80)
                findPathButton.setCompoundDrawables(img, null, null, null)
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
                if (!sortedAed.aed.checked) {
                    sortedAed.aed.checked = true
                    val img: Drawable? =
                        it.context.resources.getDrawable(R.drawable.map_bookmark_on)
                    img?.setBounds(0, 0, 80, 80)
                    binding.bookMarkButton.setCompoundDrawables(img, null, null, null)

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            DataBaseProvider.provideDB(it.context).bookmarkAedDao()
                                .insertAed(sortedAed.aed)
                        } catch (e: Exception) {
                            DataBaseProvider.provideDB(it.context).bookmarkAedDao()
                                .updateAed(sortedAed.aed)
                        }


                        val aedRepo =
                            DataBaseProvider.provideDB(it.context).bookmarkAedDao().getAll()
                        Log.d("ViewPagerAdapter", aedRepo.toString())
                    }
                } else {
                    sortedAed.aed.checked = false
                    val img: Drawable? =
                        it.context.resources.getDrawable(R.drawable.map_bookmark_off)
                    img?.setBounds(0, 0, 80, 80)
                    binding.bookMarkButton.setCompoundDrawables(img, null, null, null)

                    CoroutineScope(Dispatchers.IO).launch {
                        DataBaseProvider.provideDB(it.context).bookmarkAedDao()
                            .delete(sortedAed.aed.lat)

                        val aedRepo =
                            DataBaseProvider.provideDB(it.context).bookmarkAedDao().getAll()
                        Log.d("ViewPagerAdapter", aedRepo.toString())
                    }
                }
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