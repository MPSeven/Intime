package kr.go.mapo.intime.info

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kr.go.mapo.intime.R
import androidx.appcompat.widget.Toolbar

class ForestFireActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forest_fire)

        val toolbar = findViewById<Toolbar>(R.id.forestFire_appbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "재난상황 대처 방법"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}