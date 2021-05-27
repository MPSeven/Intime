package kr.go.mapo.intime.info

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.ActivityAedBinding

class AedActivity : AppCompatActivity() {
    val binding by lazy { ActivityAedBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        clickableSpanOne()

        binding.infoAedBack.setOnClickListener {
            onBackPressed()
        }


    }

    private fun clickableSpanOne() {
        val spanText = SpannableStringBuilder("심폐소생술 방법 바로 알아보기")
        Log.i("SPAN", "spanText called")

        val clickable = object : ClickableSpan() {
            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.setARGB(1, 24, 116, 193)
            }

            override fun onClick(view: View) {
                Log.i("SPAN", "intent called?")
                startActivity(Intent(this@AedActivity, CprActivity::class.java))
                view.invalidate()
            }
        }
        spanText.setSpan(clickable, 0, spanText.length, 0)
        spanText.setSpan(
            ForegroundColorSpan(Color.parseColor("#1874C1")),
            0,
            spanText.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        val clickableId = findViewById<TextView>(R.id.info_tv_cpr_clickable)
        clickableId.setText(spanText, TextView.BufferType.SPANNABLE)
        clickableId.movementMethod = LinkMovementMethod.getInstance()

    }
}