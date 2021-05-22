package kr.go.mapo.intime.setting

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.ActivitySettingBinding
import kr.go.mapo.intime.widget.IntimeWidget

class SettingActivity : AppCompatActivity() {

    val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.settingCon.setOnClickListener {
            val conIntent = Intent(this, SettingContactsActivity::class.java)
            startActivity(conIntent)
        }

        binding.settingFav.setOnClickListener {
            val favIntent = Intent(this, SettingFavoriteActivity::class.java)
            startActivity(favIntent)
        }

        binding.settingX.setOnClickListener {
            finish()
        }

    }
}