package kr.go.mapo.intime.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.naver.maps.map.widget.LocationButtonView
import kr.go.mapo.intime.*
import kr.go.mapo.intime.R
import kr.go.mapo.intime.api.AedService
import kr.go.mapo.intime.model.AedDto
import kr.go.mapo.intime.model.SortedAed
import org.greenrobot.eventbus.EventBus
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.RuntimeException

import java.security.Permissions
import kotlin.properties.Delegates

class MapFragment : Fragment(), OnMapReadyCallback, Overlay.OnClickListener {
    private lateinit var mapView: MapView
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private var markerList: MutableList<Marker> = mutableListOf()
    private var isFirstLocation = true
    private lateinit var locationButtonView: LocationButtonView
    private lateinit var geoCoder: Geocoder
    private var myAddress: String = "init"
    private lateinit var addressTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private val recyclerAdapter = AedListAdapter()
    private lateinit var aedNumberTextView: TextView
    private lateinit var spannableString: SpannableString
    private lateinit var rootView: View
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private val aedCategoryButton: Button by lazy {
        rootView.findViewById(R.id.aedCategoryButton)
    }
    private val shelterCategoryButton: Button by lazy {
        rootView.findViewById(R.id.shelterCategoryButton)
    }
    private val viewPager: ViewPager2 by lazy {
        rootView.findViewById(R.id.viewPager)
    }
    private val listViewButton: Button by lazy {
        rootView.findViewById<Button>(R.id.listViewButton)
    }
    private val viewPagerAdapter = AedViewPagerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_map, container, false)
        var mapView = rootView.findViewById(R.id.map_view) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        addressTextView = rootView.findViewById<TextView>(R.id.addressTextView)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        aedNumberTextView = rootView.findViewById(R.id.aedNumber)
        locationButtonView = rootView.findViewById(R.id.location)


        val behavior = BottomSheetBehavior.from(
            rootView.findViewById(R.id.bottomSheetDialog)
        )

        recyclerView.adapter = recyclerAdapter
        viewPager.adapter = viewPagerAdapter

        recyclerView.layoutManager = LinearLayoutManager(activity)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val selectedModel = viewPagerAdapter.currentList[position]
                val cameraUpdate =
                    CameraUpdate.scrollTo(LatLng(selectedModel.aed.lat, selectedModel.aed.lon))
                        .animate(CameraAnimation.Easing)

                naverMap.moveCamera(cameraUpdate)
            }
        })

        behavior.isFitToContents = false
        behavior.halfExpandedRatio = 0.35F
        behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        setClickedAedButtonAppearance()

        aedCategoryButton.setOnClickListener {
            Log.d(TAG, "aedCategoryButton onClick!!!")
            resetMarkerList()
            setClickedAedButtonAppearance()

            fetchAedLocation(latitude, longitude, DISTANCE)
        }

        shelterCategoryButton.setOnClickListener {
            Log.d(TAG, "aedCategoryButton onClick!!!")
            resetMarkerList()
            setClickedShelterButtonAppearance()

        }

        listViewButton.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            listViewButton.visibility = View.GONE
        }


        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                Log.d(TAG, "newState: $newState")
                //behavior.saveFlags = BottomSheetBehavior.SAVE_ALL
                when (newState) {
                    BottomSheetBehavior.STATE_SETTLING -> {

                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        listViewButton.visibility = View.VISIBLE

                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        listViewButton.visibility = View.VISIBLE

                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d(TAG, "offset: $slideOffset")
            }

        })


        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


    @SuppressLint("ResourceType")
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        naverMap.maxZoom = 18.0


        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true

        val uiSettings: UiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = false
        uiSettings.isCompassEnabled = false
        uiSettings.isZoomControlEnabled = false
        uiSettings.isScrollGesturesEnabled = true

        locationButtonView.map = naverMap

        naverMap.setOnMapClickListener { _, _ ->
            viewPager.visibility = View.GONE
            listViewButton.visibility = View.VISIBLE
            val behavior = BottomSheetBehavior.from(
                rootView.findViewById(R.id.bottomSheetDialog)
            )
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        naverMap.addOnLocationChangeListener { location ->
            if (isFirstLocation) {
                val initializePosition = LatLng(location.latitude, location.longitude)
                val cameraUpdate: CameraUpdate =
                    CameraUpdate.scrollTo(initializePosition).animate(CameraAnimation.Easing)
                naverMap.moveCamera(cameraUpdate)

                fetchAedLocation(location.latitude, location.longitude, 0.7F)
            }
            isFirstLocation = false
        }
    }

    override fun onClick(overlay: Overlay): Boolean {
        viewPager.visibility = View.VISIBLE
        listViewButton.visibility = View.GONE

        val selectedModel = viewPagerAdapter.currentList.firstOrNull {
            it == overlay.tag
        }

        selectedModel?.let {
            val position = viewPagerAdapter.currentList.indexOf(it)
            viewPager.currentItem = position
        }

        return true
    }

    private fun fetchAedLocation(lat: Double, lon: Double, km: Float) {
        latitude = lat
        longitude = lon

        Log.d(TAG, "fetchAEDLocation!!!!!!!")

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val aedService = retrofit.create(AedService::class.java)

        myAddress = setGeo(lat, lon)
        addressTextView.text = myAddress

        aedService.getAedsByDistance(lat, lon, km)
            .enqueue(object : Callback<AedDto> {
                override fun onResponse(
                    call: Call<AedDto>,
                    response: Response<AedDto>
                ) {
                    if (response.code() == 200) {
                        val result: AedDto? = response.body()
                        if (result == null) {
                            // todo no result page
                            return
                        } else {
                            updateMapMarkers(result)
                            recyclerAdapter.submitList(result.aeds)
                            viewPagerAdapter.submitList(result.aeds)

                            getCustomAedInfo(result.aeds.size)
                        }
                    }
                }

                override fun onFailure(call: Call<AedDto>, t: Throwable) {
                    Log.e(TAG, t.toString())
                    // todo fail handling
                }
            })
    }

    private fun updateMapMarkers(result: AedDto) {
        resetMarkerList()
        if (result.aeds.isNotEmpty()) {
            for (sortedAed in result.aeds) {
                val marker = Marker()
                marker.tag = sortedAed
                val latLng = LatLng(sortedAed.aed.lat, sortedAed.aed.lon)
                marker.position = latLng

                marker.icon = OverlayImage.fromResource(R.drawable.map_aed_marker)
                marker.width = 150
                marker.height = 150
                marker.map = naverMap
                marker.onClickListener = this
                markerList.add(marker)
            }
        }
    }

    private fun resetMarkerList() {
        if (markerList.size > 0) {
            for (marker in markerList) {
                marker.map = null
            }
            markerList.clear()
        }
    }

    private fun setGeo(lat: Double, lon: Double): String {
        geoCoder = Geocoder(context)
        var list: List<Address> = emptyList()

        try {
            list = geoCoder.getFromLocation(lat, lon, 10)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d(TAG, "I/O ERROR!!")
        }

        if (list.isEmpty()) {
            Log.d(TAG, "No Address!")
            return ""
        }

        return list[0].getAddressLine(0).toString()
    }

    private fun getCustomAedInfo(size: Int) {
        val info = "나와 가장 가까운 심장충격기가 ${size}개 있습니다"

        var word = size.toString()
        val start = info.indexOf(word)
        val end = start + word.length

        spannableString = SpannableString(info)

        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#FF6702")),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        aedNumberTextView.text = spannableString
    }

    @SuppressLint("UseCompatLoadingForDrawables", "UseCompatLoadingForColorStateLists")
    private fun setClickedAedButtonAppearance() {
        aedCategoryButton.background =
            resources.getDrawable(R.drawable.map_category_aed_button_clicked)
        aedCategoryButton.setTextColor(resources.getColorStateList(R.color.white))

        val img: Drawable? = context?.resources?.getDrawable(R.drawable.map_aed_symbol_clicked)
        img?.setBounds(0, 0, 60, 60)
        aedCategoryButton.setCompoundDrawables(img, null, null, null)

        shelterCategoryButton.background = resources.getDrawable(R.drawable.map_category_button)
        shelterCategoryButton.setTextColor(resources.getColorStateList(R.color.black))

        val img2: Drawable? = context?.resources?.getDrawable(R.drawable.map_shelter_symbol)
        img2?.setBounds(0, 0, 60, 60)
        shelterCategoryButton.setCompoundDrawables(img2, null, null, null)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "UseCompatLoadingForColorStateLists")
    private fun setClickedShelterButtonAppearance() {
        shelterCategoryButton.background =
            resources.getDrawable(R.drawable.map_category_shelter_button_clicked)
        shelterCategoryButton.setTextColor(resources.getColorStateList(R.color.white))

        val img: Drawable? = context?.resources?.getDrawable(R.drawable.map_shelter_symbol_clicked)
        img?.setBounds(0, 0, 60, 60)
        shelterCategoryButton.setCompoundDrawables(img, null, null, null)

        aedCategoryButton.background = resources.getDrawable(R.drawable.map_category_button)
        aedCategoryButton.setTextColor(resources.getColorStateList(R.color.black))

        val img2: Drawable? = context?.resources?.getDrawable(R.drawable.map_aed_symbol)
        img2?.setBounds(0, 0, 60, 60)
        aedCategoryButton.setCompoundDrawables(img2, null, null, null)
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val TAG = "MapFragment"
        private const val BASE_URL = "http://172.30.1.51:8080"
        private const val DISTANCE = 0.5F
    }
}