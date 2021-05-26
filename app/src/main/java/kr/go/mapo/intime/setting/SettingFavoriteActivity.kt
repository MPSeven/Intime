package kr.go.mapo.intime.setting

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_setting_favorite.*
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.ActivitySettingFavoriteBinding
import kr.go.mapo.intime.info.InfoFragment
import kr.go.mapo.intime.map.MapFragment
import kr.go.mapo.intime.setting.fragment.FavoriteAedFragment
import kr.go.mapo.intime.setting.fragment.FavoriteAllFragment
import kr.go.mapo.intime.setting.fragment.FavoriteShelterFragment
import kr.go.mapo.intime.sos.SosFragment

class SettingFavoriteActivity : AppCompatActivity(){

    val binding by lazy { ActivitySettingFavoriteBinding.inflate(layoutInflater) }

    private val tabTextList = arrayListOf("전체", "심장충격기", "대피소", "응급실", "24시 약국")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        bindViews()
    }

    private fun initViews() = with(binding) {
        containerFavorite.adapter = FavoriteCollectionAdapter(this@SettingFavoriteActivity)
        TabLayoutMediator(settingTabLayout, containerFavorite) { tab, position ->
            tab.text = tabTextList[position]
        }.attach()

    }

    private fun bindViews() = with(binding) {
        binding.settingFavX.setOnClickListener {
            onBackPressed()
        }
    }

}