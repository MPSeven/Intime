package kr.go.mapo.intime.onboarding

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_on_boarding_one.*
import kr.go.mapo.intime.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
//        getWindow().setStatusBarColor(Color.WHITE);

    }

    override fun onBackPressed() {
        finishAffinity()
        finish()
    }
}
