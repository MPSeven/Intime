package kr.go.mapo.intime.info

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kr.go.mapo.intime.R
import androidx.appcompat.widget.Toolbar

class TyphoonActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_typhoon)

        val toolbar = findViewById<Toolbar>(R.id.typhoon_appbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "재난상황 대처 방법"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }
}