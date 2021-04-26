package kr.go.mapo.intime

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.MessageListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.go.mapo.intime.fragment.InfoFragment
import kr.go.mapo.intime.fragment.MapFragment
import kr.go.mapo.intime.fragment.SosFragment

class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_sos -> {
                    println("SOS")
                    replaceFragment(SosFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_map -> {
                    println("MAP")
                    replaceFragment(MapFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_info -> {
                    println("INFO")
                    replaceFragment(InfoFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private val bottomNavigationView: BottomNavigationView by lazy {
        findViewById(R.id.bottomNavigationView)
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        bottomNavigationView.menu.getItem(1).isChecked = true


        replaceFragment(MapFragment())

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }


}
