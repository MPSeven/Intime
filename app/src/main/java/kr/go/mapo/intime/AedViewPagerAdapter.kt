package kr.go.mapo.intime

import android.Manifest
import android.Manifest.permission_group.PHONE
import android.app.Activity
import android.bluetooth.BluetoothClass.Device.Major.PHONE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.fragment.MapFragment
import kr.go.mapo.intime.model.SortedAed
import java.lang.Exception
import kotlin.math.roundToInt

class AedViewPagerAdapter :
    androidx.recyclerview.widget.ListAdapter<SortedAed, AedViewPagerAdapter.ItemViewHolder>(differ) {

    inner class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(sortedAed: SortedAed) {
            val orgTextView = view.findViewById<TextView>(R.id.orgMarker)
            val addressTextView = view.findViewById<TextView>(R.id.addressMarker)
            val addressDetailTextView = view.findViewById<TextView>(R.id.addressDetailMarker)
            val distanceTextView = view.findViewById<TextView>(R.id.distanceMarker)
            val findPathButton = view.findViewById<Button>(R.id.findPathButton)
            val callButton = view.findViewById<Button>(R.id.callNumber)
            val bookmarkButton = view.findViewById<Button>(R.id.bookMarkButton)

            orgTextView.text = sortedAed.aed.org
            addressTextView.text = sortedAed.aed.address
            addressDetailTextView.text = sortedAed.aed.addressDetail
            distanceTextView.text = "${(sortedAed.distance * 1000).roundToInt()}m"

            view.setOnClickListener {
                Log.d("ViewP2 onClickListener", "${getItem(adapterPosition)}")
            }

            findPathButton.setOnClickListener {
                try{
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("nmap://route/walk?dlat=${sortedAed.aed.lat}&dlng=${sortedAed.aed.lon}&dname=${sortedAed.aed.org}")).apply {
                        `package` = "com.nhn.android.nmap"
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    it.context.startActivity(intent)
                }catch (e: Exception) {
                   val intentPlayStore = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$NAVER_MAP_PACKAGE_NAME" ))
                   it.context.startActivity(intentPlayStore)
                }
            }

            callButton.setOnClickListener {
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

            bookmarkButton.setOnClickListener {
                // TODO DB
                
            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(R.layout.item_detail_viewpager, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
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