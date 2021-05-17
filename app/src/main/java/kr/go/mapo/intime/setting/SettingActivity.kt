package kr.go.mapo.intime.setting

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kr.go.mapo.intime.R

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        findViewById<ConstraintLayout>(R.id.setting_con).setOnClickListener {
            val conIntent = Intent(this, SettingContactActivity::class.java)
            startActivity(conIntent)
        }

        findViewById<ConstraintLayout>(R.id.setting_fav).setOnClickListener {
            val favIntent = Intent(this, SettingFavoriteActivity::class.java)
            startActivity(favIntent)
        }

        findViewById<ImageButton>(R.id.setting_x).setOnClickListener {
            finish()
        }

    }
}