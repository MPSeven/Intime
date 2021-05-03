package kr.go.mapo.intime.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.naver.maps.map.widget.LocationButtonView
import kr.go.mapo.intime.MainActivity
import kr.go.mapo.intime.R
import kr.go.mapo.intime.api.AedService
import kr.go.mapo.intime.model.AedDto
import kr.go.mapo.intime.model.SortedAed
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.security.Permissions
import kotlin.properties.Delegates

class MapFragment : Fragment(), OnMapReadyCallback, Overlay.OnClickListener {
    private lateinit var mapView: MapView
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private var markerList: MutableList<Marker> = mutableListOf()
    private val marker = Marker()
    private lateinit var infoWindow: InfoWindow
    private var isFirstLocation = true
    private lateinit var locationButtonView: LocationButtonView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_map, container, false)
        var mapView = rootView.findViewById(R.id.map_view) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.map_view)
        locationButtonView = view.findViewById(R.id.location)
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

        naverMap.addOnCameraChangeListener { i, b ->
            Log.d(TAG, "i: ${i}, bool: $b")
            if (i == -3) {
                val latLng = naverMap.cameraPosition.target
                //fetchAedLocation(latLng.latitude, latLng.longitude, 0.7F)
            }

        }
        naverMap.addOnCameraIdleListener {
            val latLng = naverMap.cameraPosition.target
            fetchAedLocation(latLng.latitude, latLng.longitude, 0.7F)
        }

        naverMap.setOnMapClickListener { _, _ ->
            if(infoWindow.marker != null) {
                infoWindow.close()
            }
        }

        naverMap.addOnLocationChangeListener { location ->
            if(isFirstLocation) {
                val initializePosition: LatLng = LatLng(location.latitude, location.longitude)
                val cameraUpdate: CameraUpdate = CameraUpdate.scrollTo(initializePosition).animate(CameraAnimation.Easing)
                naverMap.moveCamera(cameraUpdate)
                fetchAedLocation(location.latitude, location.longitude, 0.7F)
            }
            isFirstLocation = false
        }

        infoWindow = InfoWindow()
        if (context != null) {
            infoWindow.adapter = object : InfoWindow.DefaultViewAdapter(context!!) {
                override fun getContentView(p0: InfoWindow): View {
                    val marker: Marker? = infoWindow.marker
                    val aed: SortedAed = marker?.tag as SortedAed

                    val view: View = View.inflate(context, R.layout.view_info_window, null)
                    view.findViewById<TextView>(R.id.address).text = aed.aed.address
                    view.findViewById<TextView>(R.id.addressDetail).text = aed.aed.addressDetail
                    view.findViewById<TextView>(R.id.tel).text = aed.aed.tel

                    return view
                }

            }
        }
    }

    override fun onClick(overlay: Overlay): Boolean {

        val marker: Marker = overlay as Marker
        infoWindow.open(marker)
        return false
    }

    private fun fetchAedLocation(lat: Double, lon: Double, km: Float) {
        Log.d(TAG, "fetchAEDLocation!!!!!!!")
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val aedService = retrofit.create(AedService::class.java)

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
                        }
                    }
                }

                override fun onFailure(call: Call<AedDto>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }
            })
    }

    private fun updateMapMarkers(result: AedDto) {
        resetMarkerList()
        if(result.aeds.isNotEmpty()) {
            for (sortedAed in result.aeds) {
                val marker = Marker()
                marker.tag = sortedAed
                val latLng = LatLng(sortedAed.aed.lat, sortedAed.aed.lon)
                marker.position = latLng

                if (sortedAed.distance < 0.3) {
                    marker.icon = MarkerIcons.RED
                    marker.iconTintColor = Color.RED
                }
                marker.map = naverMap
                marker.setOnClickListener { overlay ->
                    val marker: Marker = overlay as Marker
                    if(marker.infoWindow != null) {
                        infoWindow.close()
                    }else{
                        infoWindow.open(marker)
                    }
                    true
                }
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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val TAG = "MainActivity"
        private const val BASE_URL = "http://172.30.1.53:8080"
    }
}