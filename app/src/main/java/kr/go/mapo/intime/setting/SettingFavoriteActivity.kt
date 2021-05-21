package kr.go.mapo.intime.setting

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import kr.go.mapo.intime.R

class SettingFavoriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_favorite)

        findViewById<ImageButton>(R.id.setting_fav_x).setOnClickListener {
            onBackPressed()
        }
    }
}