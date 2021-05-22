package kr.go.mapo.intime.setting

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.ActivitySettingFavoriteBinding

class SettingFavoriteActivity : AppCompatActivity() {

    val binding by lazy { ActivitySettingFavoriteBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.settingFavX.setOnClickListener {
            onBackPressed()
        }
    }
}