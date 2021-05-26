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

class MapBottomSheetDialog(sortedAed: SortedAed): BottomSheetDialogFragment() {

//    private lateinit var mListener: BottomSheetListener
    private lateinit var rootView: View

    private val sortedAed = sortedAed

    private val naverMapButton: ImageButton by lazy {
        rootView.findViewById(R.id.naverMapButton)
    }

    private val kakaoMapButton: ImageButton by lazy {
        rootView.findViewById(R.id.kakaoMapButton)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.map_download_bottom_sheet, container, false)

        naverMapButton.setOnClickListener {
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("nmap://route/walk?dlat=${sortedAed.aed.lat}&dlng=${sortedAed.aed.lon}&dname=${sortedAed.aed.org}")
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

        kakaoMapButton.setOnClickListener {
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("kakaomap://route?ep=${sortedAed.aed.lat},${sortedAed.aed.lon}&by=FOOT")
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