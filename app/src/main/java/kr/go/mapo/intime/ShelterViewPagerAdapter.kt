package kr.go.mapo.intime

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
import kr.go.mapo.intime.database.DataBaseProvider
import kr.go.mapo.intime.databinding.ItemDetailViewpagerBinding
import kr.go.mapo.intime.databinding.ItemShelterDetailViewpagerBinding
import kr.go.mapo.intime.model.SortedAed
import kr.go.mapo.intime.model.SortedShelter
import java.lang.Exception
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

class ShelterViewPagerAdapter :
    androidx.recyclerview.widget.ListAdapter<SortedShelter, ShelterViewPagerAdapter.ShelterItemViewHolder>(differ), CoroutineScope {

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

            if(sortedShelter.shelter.checked) {
                shelterFindPathButton
            }

        }

        fun bindViews(sortedShelter: SortedShelter) {
            binding.shelterFindPathButton.setOnClickListener {
                try {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("nmap://route/walk?dlat=${sortedShelter.shelter.lat}&dlng=${sortedShelter.shelter.lon}&dname=${sortedShelter.shelter.placeName}")
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

            binding.shelterCallNumber.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${sortedShelter.shelter.capacityArea}"))
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

//            binding.bookMarkButton.setOnClickListener {
//                // TODO DB
//                if (!sortedAed.aed.checked) {
//                    sortedAed.aed.checked = true
//                    val img: Drawable? =
//                        it.context.resources.getDrawable(R.drawable.map_bookmark_on)
//                    img?.setBounds(0, 0, 60, 60)
//                    binding.bookMarkButton.setCompoundDrawables(img, null, null, null)
//
//                    CoroutineScope(Dispatchers.IO).launch {
//                        DataBaseProvider.provideDB(it.context).bookmarkAedDao()
//                            .insertAed(sortedAed.aed)
//
//                        val aedRepo =
//                            DataBaseProvider.provideDB(it.context).bookmarkAedDao().getAll()
//                        Log.d("ViewPagerAdapter", aedRepo.toString())
//                    }
//                } else {
//                    sortedAed.aed.checked = false
//                    val img: Drawable? =
//                        it.context.resources.getDrawable(R.drawable.map_bookmark_off)
//                    img?.setBounds(0, 0, 60, 60)
//                    binding.bookMarkButton.setCompoundDrawables(img, null, null, null)
//
//                    CoroutineScope(Dispatchers.IO).launch {
//                        DataBaseProvider.provideDB(it.context).bookmarkAedDao()
//                            .delete(sortedAed.aed.lat)
//
//                        val aedRepo =
//                            DataBaseProvider.provideDB(it.context).bookmarkAedDao().getAll()
//                        Log.d("ViewPagerAdapter", aedRepo.toString())
//                    }
//                }
//            }
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

    companion object {
        val differ = object : DiffUtil.ItemCallback<SortedShelter>() {
            override fun areItemsTheSame(oldItem: SortedShelter, newItem: SortedShelter): Boolean {
                return oldItem.distance == newItem.distance
            }

            override fun areContentsTheSame(oldItem: SortedShelter, newItem: SortedShelter): Boolean {
                return oldItem == newItem
            }

        }

        private const val NAVER_MAP_PACKAGE_NAME = "com.nhn.android.nmap"
    }

}