package kr.go.mapo.intime.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.go.mapo.intime.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
/*        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Handler().postDelayed({
            val intent = Intent(this,MainActivity::class.java )
            startActivity(intent)
            finish()
        },3000)
    */
    }
}