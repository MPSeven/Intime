package kr.go.mapo.intime.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
