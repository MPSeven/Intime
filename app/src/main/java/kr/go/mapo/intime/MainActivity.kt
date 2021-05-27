package kr.go.mapo.intime


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.naver.maps.map.*
import kr.go.mapo.intime.databinding.ActivityMainBinding
import kr.go.mapo.intime.info.InfoFragment
import kr.go.mapo.intime.map.MapFragment
import kr.go.mapo.intime.setting.SettingActivity
import kr.go.mapo.intime.sos.SosFragment

class MainActivity : AppCompatActivity(){

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    private val infoFragment = InfoFragment()
    private val mapFragment = MapFragment()
    private val sosFragment = SosFragment()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        bottomNavigationView.menu.getItem(1).isChecked = true
        replaceFragment(mapFragment)

        binding.toolbar.title = "INTIME"
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.top_app_bar, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.toolbar_btn -> {
                val settingIntent = Intent(this, SettingActivity::class.java)
                startActivity(settingIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    네비바 컨트롤
    private fun fragmentManager(){
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
                    binding.toolbar.title = ""
                    binding.toolbarMain.text ="SOS"
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_map -> {

                    replaceFragment(MapFragment())
                    binding.toolbarMain.text = ""
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_info -> {

                    replaceFragment(InfoFragment())
                    binding.toolbar.title = ""
                    binding.toolbarMain.text ="정보"

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
