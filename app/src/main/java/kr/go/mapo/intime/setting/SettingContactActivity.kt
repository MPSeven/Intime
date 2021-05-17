package kr.go.mapo.intime.setting

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import kr.go.mapo.intime.R

class SettingContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_contact)

        findViewById<ImageButton>(R.id.setting_con_x).setOnClickListener {
            onBackPressed()
        }

        findViewById<ImageButton>(R.id.setting_plus).setOnClickListener {

        }
    }
}