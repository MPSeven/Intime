package kr.go.mapo.intime.map

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.widget.LocationButtonView
import kotlinx.android.synthetic.main.item_detail_viewpager.view.*
import kotlinx.coroutines.*
import kr.go.mapo.intime.R
import kr.go.mapo.intime.map.api.AedService
import kr.go.mapo.intime.map.api.ShelterService
import kr.go.mapo.intime.databinding.FragmentMapBinding
import kr.go.mapo.intime.map.model.Aed
import kr.go.mapo.intime.map.response.AedDto
import kr.go.mapo.intime.map.response.ShelterDto
import kr.go.mapo.intime.map.response.Url
import kr.go.mapo.intime.setting.database.DataBaseProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class MapFragment : Fragment(), OnMapReadyCallback, Overlay.OnClickListener, CoroutineScope {

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private var markerList: MutableList<Marker> = mutableListOf()
    private var isFirstLocation = true
    private lateinit var locationButtonView: LocationButtonView
    private lateinit var geoCoder: Geocoder
    private var myAddress: String = "init"
    private lateinit var addressTextView: TextView
    private val aedRecyclerAdapter = AedListAdapter()
    private val shelterRecyclerAdapter = ShelterListAdapter()
    private val aedViewPagerAdapter = AedViewPagerAdapter()
    private val shelterViewPagerAdapter = ShelterViewPagerAdapter()
    private lateinit var aedNumberTextView: TextView
    private lateinit var spannableString: SpannableString
    private lateinit var rootView: View
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val recyclerView: RecyclerView by lazy {
        rootView.findViewById(R.id.recyclerView)
    }

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
        rootView.findViewById(R.id.listViewButton)
    }
    private val emptyResultTextView: TextView by lazy {
        rootView.findViewById(R.id.emptyResultTextView)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        rootView = binding.root
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        addressTextView = rootView.findViewById(R.id.addressTextView)
        aedNumberTextView = rootView.findViewById(R.id.aedNumber)
        locationButtonView = rootView.findViewById(R.id.location)

        recyclerView.adapter = aedRecyclerAdapter
        viewPager.adapter = aedViewPagerAdapter

        recyclerView.layoutManager = LinearLayoutManager(activity)

        val behavior = BottomSheetBehavior.from(
            rootView.findViewById(R.id.bottomSheetDialog)
        )

        behavior.isFitToContents = false
        behavior.halfExpandedRatio = 0.35F
        behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                Log.d(TAG, "newState: $newState")
                //behavior.saveFlags = BottomSheetBehavior.SAVE_ALL
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        listViewButton.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d(TAG, "offset: $slideOffset")
            }
        })

        setClickedAedButtonAppearance()
        initViews()
        bindViews()

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
    }

    private fun initViews() = with(binding) {

    }


    private fun bindViews() = with(binding) {

        binding.bottomSheetDialog.aedCategoryButton.setOnClickListener {
            Log.d(TAG, "aedCategoryButton onClick!!!")
            resetMarkerList()
            setClickedAedButtonAppearance()

            fetchAedLocation(latitude, longitude, DISTANCE)

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Log.d(TAG, "$position")

                    if (aedViewPagerAdapter.currentList.size >= position) {
                        val selectedModel = aedViewPagerAdapter.currentList[position]

                        val cameraUpdate =
                            CameraUpdate.scrollTo(
                                LatLng(
                                    selectedModel.aed.lat,
                                    selectedModel.aed.lon
                                )
                            )
                                .animate(CameraAnimation.Easing)

                        naverMap.moveCamera(cameraUpdate)
                    }
                }
            })
        }


        binding.bottomSheetDialog.shelterCategoryButton.setOnClickListener {
            Log.d(TAG, "shelterCategoryButton onClick!!!")
            resetMarkerList()
            setClickedShelterButtonAppearance()
            fetchShelterLocation(latitude, longitude, DISTANCE)

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Log.d(TAG, "$position")

                    if (shelterViewPagerAdapter.currentList.size >= position) {
                        val cameraUpdate =
                            CameraUpdate.scrollTo(
                                LatLng(
                                    shelterViewPagerAdapter.currentList[position].shelter.lat,
                                    shelterViewPagerAdapter.currentList[position].shelter.lon
                                )
                            )
                                .animate(CameraAnimation.Easing)

                        naverMap.moveCamera(cameraUpdate)
                    }
                }
            })
        }


        binding.listViewButton.setOnClickListener {
            val behavior = BottomSheetBehavior.from(
                rootView.findViewById(R.id.bottomSheetDialog)
            )

            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            listViewButton.visibility = View.GONE
        }
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

        initMap()

        locationButtonView.map = naverMap

        naverMap.setOnMapClickListener { _, _ ->
            viewPager.visibility = View.GONE
            listViewButton.visibility = View.VISIBLE
            recyclerViewHidden()
        }


        naverMap.addOnLocationChangeListener { location ->
            if (isFirstLocation) {
                val initializePosition = LatLng(location.latitude, location.longitude)
                val cameraUpdate: CameraUpdate =
                    CameraUpdate.scrollTo(initializePosition).animate(CameraAnimation.Easing)
                naverMap.moveCamera(cameraUpdate)

                fetchAedLocation(location.latitude, location.longitude, DISTANCE)
            }
            isFirstLocation = false
        }

        fetchAedLocation(latitude, longitude, DISTANCE)
    }

    private fun initMap() {
        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true

        val uiSettings: UiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = false
        uiSettings.isCompassEnabled = false
        uiSettings.isZoomControlEnabled = false
        uiSettings.isScrollGesturesEnabled = true
    }

    override fun onClick(overlay: Overlay): Boolean {
        viewPager.visibility = View.VISIBLE
        listViewButton.visibility = View.GONE
        recyclerViewHidden()

        val selectedModel = aedViewPagerAdapter.currentList.firstOrNull {
            it == overlay.tag
        }

        val selectedShelterModel = shelterViewPagerAdapter.currentList.firstOrNull {
            it == overlay.tag
        }

        selectedModel?.let {
            val position = aedViewPagerAdapter.currentList.indexOf(it)
            viewPager.currentItem = position
        }

        selectedShelterModel?.let {
            val position = shelterViewPagerAdapter.currentList.indexOf(it)
            viewPager.currentItem = position
        }

        return true
    }

    private fun fetchAedLocation(lat: Double, lon: Double, km: Float) {
        latitude = lat
        longitude = lon

        recyclerView.adapter = aedRecyclerAdapter
        viewPager.adapter = aedViewPagerAdapter

        Log.d(TAG, "fetchAEDLocation!!!!!!!")

        val retrofit = Retrofit.Builder()
            .baseUrl(Url.INTIME_URL)
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
                            emptyResultTextView.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                            return
                        } else {
                            updateAedMapMarkers(result)
                            aedRecyclerAdapter.submitList(result.aeds)
                            aedViewPagerAdapter.submitList(result.aeds)
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

    private fun fetchShelterLocation(lat: Double, lon: Double, km: Float) {
        latitude = lat
        longitude = lon

        recyclerView.adapter = shelterRecyclerAdapter
        viewPager.adapter = shelterViewPagerAdapter

        Log.d(TAG, "fetchShelterLocation!!!!!!!")

        val retrofit = Retrofit.Builder()
            .baseUrl(Url.INTIME_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val shelterService = retrofit.create(ShelterService::class.java)

        myAddress = setGeo(lat, lon)
        addressTextView.text = myAddress

        shelterService.getSheltersByDistance(lat, lon, km)
            .enqueue(object : Callback<ShelterDto> {
                override fun onResponse(
                    call: Call<ShelterDto>,
                    response: Response<ShelterDto>
                ) {
                    if (response.code() == 200) {
                        val result: ShelterDto? = response.body()

                        Log.d(TAG, "$result")

                        if (result == null) {
                            emptyResultTextView.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                            return
                        } else {

                            updateShelterMapMarkers(result)
                            shelterRecyclerAdapter.submitList(result.shelters)
                            shelterViewPagerAdapter.submitList(result.shelters)
                            getCustomShelterInfo(result.shelters.size)
                        }
                    }
                }

                override fun onFailure(call: Call<ShelterDto>, t: Throwable) {
                    Log.e(TAG, t.toString())
                    // todo fail handling
                }
            })
    }

    private fun updateAedMapMarkers(result: AedDto) {
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

    private fun updateShelterMapMarkers(result: ShelterDto) {
        resetMarkerList()

        if (result.shelters.isNotEmpty()) {
            for (sortedShelter in result.shelters) {
                val marker = Marker()
                marker.tag = sortedShelter
                val latLng = LatLng(
                    sortedShelter.shelter.lat.toDouble(),
                    sortedShelter.shelter.lon.toDouble()
                )
                marker.position = latLng

                marker.icon = OverlayImage.fromResource(R.drawable.map_shelter_marker)
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

        return list[0].getAddressLine(0).toString().substring(4)
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

    private fun getCustomShelterInfo(size: Int) {
        val info = "나와 가장 가까운 대피소가 ${size}개 있습니다"

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

    private fun recyclerViewHidden() {
        val behavior = BottomSheetBehavior.from(
            rootView.findViewById(R.id.bottomSheetDialog)
        )
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
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
        private const val DISTANCE = 0.8F
    }


}