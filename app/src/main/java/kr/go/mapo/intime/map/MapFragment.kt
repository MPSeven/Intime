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
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
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
    private lateinit var aedRecyclerAdapter: AedListAdapter
    private lateinit var aedViewPagerAdapter: AedViewPagerAdapter
    private lateinit var shelterViewPagerAdapter: ShelterViewPagerAdapter
    private lateinit var shelterRecyclerAdapter: ShelterListAdapter
    private lateinit var aedNumberTextView: TextView
    private lateinit var spannableString: SpannableString
    private lateinit var rootView: View
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private lateinit var manager: FragmentManager

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
    private val emergencyCategoryButton: Button by lazy {
        rootView.findViewById(R.id.emergencyRoomCategoryButton)
    }
    private val pharmacyCategoryButton: Button by lazy {
        rootView.findViewById(R.id.pharmacyCategoryButton)
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

        manager = (context as FragmentActivity).supportFragmentManager

        aedRecyclerAdapter = AedListAdapter(manager)
        aedViewPagerAdapter = AedViewPagerAdapter(manager)

        shelterViewPagerAdapter = ShelterViewPagerAdapter(manager)
        shelterRecyclerAdapter = ShelterListAdapter(manager)

        recyclerView.adapter = aedRecyclerAdapter
        viewPager.adapter = aedViewPagerAdapter
        viewPager.isUserInputEnabled = false
        viewPager.setPageTransformer(null)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d(TAG, "$position")

                if (aedViewPagerAdapter.currentList.size > position) {
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

        aedRecyclerAdapter.setItemClickListener( object : AedListAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                Log.d(TAG, "$position 번 선택")

                viewPager.visibility = View.VISIBLE
                listViewButton.visibility = View.GONE
                recyclerViewHidden()

                val selectedModel = aedViewPagerAdapter.currentList[position]

                viewPager.currentItem = position

                Log.d(TAG, "$selectedModel")

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
        })

        shelterRecyclerAdapter.setItemClickListener( object : ShelterListAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                Log.d(TAG, "$position 번 선택")

                viewPager.visibility = View.VISIBLE
                listViewButton.visibility = View.GONE
                recyclerViewHidden()

                val selectedModel = shelterViewPagerAdapter.currentList[position]

                viewPager.currentItem = position

                Log.d(TAG, "$selectedModel")

                val cameraUpdate =
                    CameraUpdate.scrollTo(
                        LatLng(
                            selectedModel.shelter.lat,
                            selectedModel.shelter.lon
                        )
                    )
                        .animate(CameraAnimation.Easing)

                naverMap.moveCamera(cameraUpdate)
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(activity)

        val behavior = BottomSheetBehavior.from(
            rootView.findViewById(R.id.bottomSheetDialog)
        )

        behavior.isFitToContents = false
        behavior.halfExpandedRatio = 0.45F
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


            getOverlay(R.drawable.map_aed_overlay)

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Log.d(TAG, "$position")

                    if (aedViewPagerAdapter.currentList.size > position) {
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

            getOverlay(R.drawable.map_shelter_overlay)

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

        binding.bottomSheetDialog.emergencyRoomCategoryButton.setOnClickListener {
            setClickedEmergencyButtonAppearance()
            resetMarkerList()

            getOverlay(R.drawable.map_emergency_overlay)

            emptyResultTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.INVISIBLE

            emptyResultTextView.text = "서비스 준비중입니다"

            getCustomEmergencyRoomInfo(0)
        }

        binding.bottomSheetDialog.pharmacyCategoryButton.setOnClickListener {
            setClickedPharmacyButtonAppearance()
            resetMarkerList()

            getOverlay(R.drawable.map_pharmacy_overlay)

            emptyResultTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.INVISIBLE

            emptyResultTextView.text = "서비스 준비중입니다"

            getCustomPharmacyInfo(0)

        }

        binding.listViewButton.setOnClickListener {
            val behavior = BottomSheetBehavior.from(
                rootView.findViewById(R.id.bottomSheetDialog)
            )

            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            listViewButton.visibility = View.GONE

            emptyResultTextView.text = "서비스 준비중입니다"
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        Log.d(TAG, "ONRESUME!!!!")

        setClickedAedButtonAppearance()
        fetchAedLocation(latitude, longitude, DISTANCE)
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
        setClickedAedButtonAppearance()

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
        getOverlay(R.drawable.map_aed_overlay)

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

                        Log.d(TAG, "$result")

                        if (result == null) {
                            return
                        } else {
                            if(result.result == 0) {
                                emptyResultTextView.visibility = View.VISIBLE
                                recyclerView.visibility = View.INVISIBLE
                            } else {
                                emptyResultTextView.visibility = View.GONE
                                recyclerView.visibility = View.VISIBLE
                            }
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
                            return
                        } else {
                            if(result.result == 0) {
                                emptyResultTextView.visibility = View.VISIBLE
                                recyclerView.visibility = View.INVISIBLE
                            } else {
                                emptyResultTextView.visibility = View.GONE
                                recyclerView.visibility = View.VISIBLE
                            }

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

                getOverlay(R.drawable.map_aed_overlay)

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
            ForegroundColorSpan(Color.parseColor("#EE4B3C")),
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
            ForegroundColorSpan(Color.parseColor("#1874C1")),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        aedNumberTextView.text = spannableString
    }

    private fun getCustomEmergencyRoomInfo(size: Int) {
        val info = "나와 가장 가까운 대피소가 ${size}개 있습니다"

        var word = size.toString()
        val start = info.indexOf(word)
        val end = start + word.length

        spannableString = SpannableString(info)

        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#FFBC16")),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        aedNumberTextView.text = spannableString
    }

    private fun getCustomPharmacyInfo(size: Int) {
        val info = "나와 가장 가까운 24시 약국이 ${size}개 있습니다"

        var word = size.toString()
        val start = info.indexOf(word)
        val end = start + word.length

        spannableString = SpannableString(info)

        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#1EA849")),
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
        img?.setBounds(0, 0, ICON, ICON)
        aedCategoryButton.setCompoundDrawables(img, null, null, null)

        shelterCategoryButton.background = resources.getDrawable(R.drawable.map_category_button)
        shelterCategoryButton.setTextColor(resources.getColorStateList(R.color.black))

        val img2: Drawable? = context?.resources?.getDrawable(R.drawable.map_shelter_symbol)
        img2?.setBounds(0, 0, ICON, ICON)
        shelterCategoryButton.setCompoundDrawables(img2, null, null, null)

        emergencyCategoryButton.background = resources.getDrawable(R.drawable.map_category_button)
        emergencyCategoryButton.setTextColor(resources.getColorStateList(R.color.black))

        val img3: Drawable? = context?.resources?.getDrawable(R.drawable.map_emergency_room_symbol)
        img3?.setBounds(0, 0, ICON, ICON)
        emergencyCategoryButton.setCompoundDrawables(img3, null, null, null)

        pharmacyCategoryButton.background = resources.getDrawable(R.drawable.map_category_button)
        pharmacyCategoryButton.setTextColor(resources.getColorStateList(R.color.black))

        val img4: Drawable? = context?.resources?.getDrawable(R.drawable.map_pharmacy_symbol)
        img4?.setBounds(0, 0, ICON, ICON)
        pharmacyCategoryButton.setCompoundDrawables(img4, null, null, null)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "UseCompatLoadingForColorStateLists")
    private fun setClickedShelterButtonAppearance() {
        shelterCategoryButton.background =
            resources.getDrawable(R.drawable.map_category_shelter_button_clicked)
        shelterCategoryButton.setTextColor(resources.getColorStateList(R.color.white))

        val img: Drawable? = context?.resources?.getDrawable(R.drawable.map_shelter_symbol_clicked)
        img?.setBounds(0, 0, ICON, ICON)
        shelterCategoryButton.setCompoundDrawables(img, null, null, null)

        aedCategoryButton.background = resources.getDrawable(R.drawable.map_category_button)
        aedCategoryButton.setTextColor(resources.getColorStateList(R.color.black))

        val img2: Drawable? = context?.resources?.getDrawable(R.drawable.map_aed_symbol_fixed)
        img2?.setBounds(0, 0, ICON, ICON)
        aedCategoryButton.setCompoundDrawables(img2, null, null, null)

        emergencyCategoryButton.background = resources.getDrawable(R.drawable.map_category_button)
        emergencyCategoryButton.setTextColor(resources.getColorStateList(R.color.black))

        val img3: Drawable? = context?.resources?.getDrawable(R.drawable.map_emergency_room_symbol)
        img3?.setBounds(0, 0, ICON, ICON)
        emergencyCategoryButton.setCompoundDrawables(img3, null, null, null)

        pharmacyCategoryButton.background = resources.getDrawable(R.drawable.map_category_button)
        pharmacyCategoryButton.setTextColor(resources.getColorStateList(R.color.black))

        val img4: Drawable? = context?.resources?.getDrawable(R.drawable.map_pharmacy_symbol)
        img4?.setBounds(0, 0, ICON, ICON)
        pharmacyCategoryButton.setCompoundDrawables(img4, null, null, null)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "UseCompatLoadingForColorStateLists")
    private fun setClickedEmergencyButtonAppearance() {
        aedCategoryButton.background = resources.getDrawable(R.drawable.map_category_button)
        aedCategoryButton.setTextColor(resources.getColorStateList(R.color.black))

        val img: Drawable? = context?.resources?.getDrawable(R.drawable.map_aed_symbol)
        img?.setBounds(0, 0, ICON, ICON)
        aedCategoryButton.setCompoundDrawables(img, null, null, null)

        shelterCategoryButton.background = resources.getDrawable(R.drawable.map_category_button)
        shelterCategoryButton.setTextColor(resources.getColorStateList(R.color.black))

        val img2: Drawable? = context?.resources?.getDrawable(R.drawable.map_shelter_symbol)
        img2?.setBounds(0, 0, ICON, ICON)
        shelterCategoryButton.setCompoundDrawables(img2, null, null, null)

        emergencyCategoryButton.background = resources.getDrawable(R.drawable.map_category_emergency_room_button_clicked)
        emergencyCategoryButton.setTextColor(resources.getColorStateList(R.color.white))

        val img3: Drawable? = context?.resources?.getDrawable(R.drawable.map_emergency_room_symbol_clicked)
        img3?.setBounds(0, 0, ICON, ICON)
        emergencyCategoryButton.setCompoundDrawables(img3, null, null, null)

        pharmacyCategoryButton.background = resources.getDrawable(R.drawable.map_category_button)
        pharmacyCategoryButton.setTextColor(resources.getColorStateList(R.color.black))

        val img4: Drawable? = context?.resources?.getDrawable(R.drawable.map_pharmacy_symbol)
        img4?.setBounds(0, 0, ICON, ICON)
        pharmacyCategoryButton.setCompoundDrawables(img4, null, null, null)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "UseCompatLoadingForColorStateLists")
    private fun setClickedPharmacyButtonAppearance() {
        aedCategoryButton.background = resources.getDrawable(R.drawable.map_category_button)
        aedCategoryButton.setTextColor(resources.getColorStateList(R.color.black))

        val img: Drawable? = context?.resources?.getDrawable(R.drawable.map_aed_symbol)
        img?.setBounds(0, 0, ICON, ICON)
        aedCategoryButton.setCompoundDrawables(img, null, null, null)

        shelterCategoryButton.background = resources.getDrawable(R.drawable.map_category_button)
        shelterCategoryButton.setTextColor(resources.getColorStateList(R.color.black))

        val img2: Drawable? = context?.resources?.getDrawable(R.drawable.map_shelter_symbol)
        img2?.setBounds(0, 0, ICON, ICON)
        shelterCategoryButton.setCompoundDrawables(img2, null, null, null)

        emergencyCategoryButton.background = resources.getDrawable(R.drawable.map_category_button)
        emergencyCategoryButton.setTextColor(resources.getColorStateList(R.color.black))

        val img3: Drawable? = context?.resources?.getDrawable(R.drawable.map_emergency_room_symbol)
        img3?.setBounds(0, 0, ICON, ICON)
        emergencyCategoryButton.setCompoundDrawables(img3, null, null, null)

        pharmacyCategoryButton.background = resources.getDrawable(R.drawable.map_category_pharmacy_button_clicked)
        pharmacyCategoryButton.setTextColor(resources.getColorStateList(R.color.white))

        val img4: Drawable? = context?.resources?.getDrawable(R.drawable.map_pharmacy_symbol_clicked)
        img4?.setBounds(0, 0, ICON, ICON)
        pharmacyCategoryButton.setCompoundDrawables(img4, null, null, null)
    }

    private fun getOverlay(res: Int){
        val locationOverlay = naverMap.locationOverlay
        locationOverlay.icon = OverlayImage.fromResource(res)
        locationOverlay.iconWidth = 80
        locationOverlay.iconHeight = 80
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val TAG = "MapFragment"
        private const val DISTANCE = 0.5F
        private const val ICON = 40
    }


}