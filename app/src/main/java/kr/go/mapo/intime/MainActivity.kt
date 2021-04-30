package kr.go.mapo.intime

import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction

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


class MainActivity : AppCompatActivity() {

    private val infoFragment = InfoFragment()
    private val mapFragment = MapFragment()
    private val sosFragment = SosFragment()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        bottomNavigationView.menu.getItem(1).isChecked = true

        replaceFragment(mapFragment)

        setSupportActionBar(findViewById(R.id.basic_toolbar))
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_sos -> {
                    this.setTitle(R.string.fragment_sos_tv)
                    replaceFragment(SosFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_map -> {
                    this.setTitle(R.string.fragment_map_tv)
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
        if (fragment != null) {
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayout, fragment)
            fragmentTransaction.commit()
        }
    }

}
