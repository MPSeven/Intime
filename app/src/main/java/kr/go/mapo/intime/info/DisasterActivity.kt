package kr.go.mapo.intime.info

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.ActivityDisasterBinding

class DisasterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDisasterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisasterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.disaster_appbar))
        supportActionBar?.title = "재난상황 대처법"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.infoTbEarthQuake.setOnClickListener {
            Log.e("TAG:", "click")
            val intent = Intent(this, EarthquakeActivity::class.java)
            startActivity(intent)
        }
        binding.infoTbTyphoon.setOnClickListener {
            Log.e("TAG:", "click")
            val intent = Intent(this, TyphoonActivity::class.java)
            startActivity(intent)
        }
        binding.infoTbFlood.setOnClickListener {
            Log.e("TAG:", "click")
            val intent = Intent(this, FloodActivity::class.java)
            startActivity(intent)
        }
        binding.infoTbForrestfire.setOnClickListener {
            Log.e("TAG:", "click")
            val intent = Intent(this, ForestFireActivity::class.java)
            startActivity(intent)
        }
        binding.infoTBUrbanFire.setOnClickListener {
            Log.e("TAG:", "click")
            val intent = Intent(this, UrbanFireActivity::class.java)
            startActivity(intent)
        }


}}


