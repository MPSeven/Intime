package kr.go.mapo.intime


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.naver.maps.map.*
import kotlinx.android.synthetic.main.activity_main.*
import kr.go.mapo.intime.info.InfoFragment
import kr.go.mapo.intime.map.MapFragment
import kr.go.mapo.intime.setting.SettingActivity
import kr.go.mapo.intime.sos.SosFragment

class MainActivity : AppCompatActivity() {

    private val infoFragment = InfoFragment()
    private val mapFragment = MapFragment()
    private val sosFragment = SosFragment()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        frag manager 만들어서 이부분 다 바꾸기
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        bottomNavigationView.menu.getItem(1).isChecked = true
        replaceFragment(mapFragment)
        toolbar.setTitle("INTIME")
        toolbar.setTitleTextAppearance(this, R.style.Head3)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.top_app_bar, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_btn -> {
                val settingIntent = Intent(this, SettingActivity::class.java)
                startActivity(settingIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //    네비바 컨트롤
    private fun fragmentManager() {
        val fragManager = supportFragmentManager
        fragManager.beginTransaction().apply {
        }
    }

    //    바텀네비바
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_sos -> {

                    replaceFragment(SosFragment())
                    toolbar.setTitle("SOS")
                    toolbar.setTitleCentered(true)
                    toolbar.setTitleTextAppearance(this, R.style.Body1 )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_map -> {

                    replaceFragment(MapFragment())
                    toolbar.setTitle("INTIME")
                    toolbar.setTitleCentered(false)
                    toolbar.setTitleTextAppearance(this, R.style.Head3)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_info -> {

                    replaceFragment(InfoFragment())
                    toolbar.setTitle("정보")
                    toolbar.setTitleCentered(true)
                    toolbar.setTitleTextAppearance(this, R.style.Body1 )
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
