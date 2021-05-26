package kr.go.mapo.intime.map

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.MapAddressToastBinding

object MapToast {

    fun createToast(context: Context, message: String): Toast? {
        val inflater = LayoutInflater.from(context)
        val binding: MapAddressToastBinding =
            DataBindingUtil.inflate(inflater, R.layout.map_address_toast, null, false)

        binding.mapToastTextView.text = message

        return Toast(context).apply {
            setGravity(Gravity.TOP, 0, 400)
            duration = Toast.LENGTH_LONG
            view = binding.root
        }
    }

}