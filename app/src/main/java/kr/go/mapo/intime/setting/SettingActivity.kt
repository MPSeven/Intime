package kr.go.mapo.intime.setting

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import kr.go.mapo.intime.R

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        findViewById<ImageButton>(R.id.setting_btn_con).setOnClickListener {
            val conIntent = Intent(this, SettingContactActivity::class.java)
            startActivity(conIntent)
        }

        findViewById<ImageButton>(R.id.setting_btn_fav).setOnClickListener {
            val favIntent = Intent(this, SettingFavoriteActivity::class.java)
            startActivity(favIntent)
        }

        findViewById<ImageButton>(R.id.setting_x).setOnClickListener {
            finish()
        }

    }
}