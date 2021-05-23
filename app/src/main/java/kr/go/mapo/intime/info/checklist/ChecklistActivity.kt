package kr.go.mapo.intime.info.checklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import kr.go.mapo.intime.databinding.ActivityChecklistBinding

class ChecklistActivity : AppCompatActivity() {

    val binding by lazy { ActivityChecklistBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)








        binding.chklistBack.setOnClickListener{
            onBackPressed()
        }
    }

}