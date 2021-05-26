package kr.go.mapo.intime.map

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.go.mapo.intime.R
import kr.go.mapo.intime.map.model.SortedAed
import kr.go.mapo.intime.map.model.SortedShelter

class MapShelterBottomSheetDialog(sortedShelter: SortedShelter): BottomSheetDialogFragment() {

//    private lateinit var mListener: BottomSheetListener
    private lateinit var rootView: View

    private val sortedShelter = sortedShelter

    private val naverMapShelterButton: ImageButton by lazy {
        rootView.findViewById(R.id.naverMapShelterButton)
    }

    private val kakaoMapShelterButton: ImageButton by lazy {
        rootView.findViewById(R.id.kakaoMapShelterButton)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.map_shelter_download_bottom_sheet, container, false)

        naverMapShelterButton.setOnClickListener {
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("nmap://route/walk?dlat=${sortedShelter.shelter.lat}&dlng=${sortedShelter.shelter.lon}&dname=${sortedShelter.shelter.placeName}")
                ).apply {
                    `package` = NAVER_MAP_PACKAGE_NAME
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                it.context.startActivity(intent)
            } catch (e: Exception) {
                val intentPlayStore = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${NAVER_MAP_PACKAGE_NAME}")
                )
                it.context.startActivity(intentPlayStore)
            }
        }

        kakaoMapShelterButton.setOnClickListener {
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("kakaomap://route?ep=${sortedShelter.shelter.lat},${sortedShelter.shelter.lon}&by=FOOT")
                ).apply {
                    `package` = KAKAO_MAP_PACKAGE_NAME
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                it.context.startActivity(intent)
            } catch (e: Exception) {
                val intentPlayStore = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${KAKAO_MAP_PACKAGE_NAME}")
                )
                it.context.startActivity(intentPlayStore)
            }
        }

        return rootView
    }

    companion object {
        private const val NAVER_MAP_PACKAGE_NAME = "com.nhn.android.nmap"
        private const val KAKAO_MAP_PACKAGE_NAME = "net.daum.android.map"
    }
}