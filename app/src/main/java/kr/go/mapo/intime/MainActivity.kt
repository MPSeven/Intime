package kr.go.mapo.intime

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.room.Room

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import kr.go.mapo.intime.api.AedService
import kr.go.mapo.intime.fragment.InfoFragment
import kr.go.mapo.intime.fragment.MapFragment
import kr.go.mapo.intime.fragment.SosFragment
import kr.go.mapo.intime.model.AedDto
import kr.go.mapo.intime.model.SortedAed
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity(){

    private val infoFragment = InfoFragment()
    private val mapFragment = MapFragment()
    private val sosFragment = SosFragment()
//    private val textView: TextView by lazy {
//        findViewById(R.id.addressTextView)
//    }
//    private var mAddress: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        bottomNavigationView.menu.getItem(1).isChecked = true

        replaceFragment(mapFragment)

        setSupportActionBar(findViewById(R.id.basic_toolbar))

    }

    override fun onStart() {
        super.onStart()
//        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
//        EventBus.getDefault().unregister(this)
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onAddressEvent(event: AddressEvent) {
//        if(event.address.isNotEmpty()) {
//            Log.d(TAG, "Event address: ${event.address}")
//            textView.text = event.address
//        } else {
//            Log.d(TAG, "Event is NULL")
//        }
//    }


    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_sos -> {
                    this.setTitle(R.string.fragment_sos_tv)
                    replaceFragment(SosFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_map -> {
                    this.setTitle(R.string.app_name)
                    replaceFragment(MapFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_info -> {
                    this.setTitle(R.string.fragment_info_tv)
                    replaceFragment(InfoFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            true
        }
    private val bottomNavigationView: BottomNavigationView by lazy {
        findViewById(R.id.bottomNavigationView)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

}
