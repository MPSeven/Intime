package kr.go.mapo.intime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.naver.maps.map.*
import kr.go.mapo.intime.fragment.InfoFragment
import kr.go.mapo.intime.fragment.MapFragment
import kr.go.mapo.intime.fragment.SosFragment


class MainActivity : AppCompatActivity(){

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

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
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
