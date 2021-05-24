package kr.go.mapo.intime.info

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.ActivityAedBinding

class AedActivity : AppCompatActivity() {
    val binding by lazy { ActivityAedBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.aed_appbar))
        supportActionBar?.title = "재난상황 대처 방법"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }
}