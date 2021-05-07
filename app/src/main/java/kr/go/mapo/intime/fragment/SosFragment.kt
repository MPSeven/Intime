package kr.go.mapo.intime.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kr.go.mapo.intime.R
import java.io.IOException
import java.util.*


class SosFragment : Fragment() {

    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    val PERMISSIONS_REQUEST_CODE = 100
    val SMS_SEND_PERMISSON = 1

    var lm: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_sos, container, false)
        var addressBox = root.findViewById<TextView>(R.id.gps_address)
        var sendButton = root.findViewById<ImageButton>(R.id.btn_fav)

        addressBox.setText(getAddress())

        checkSmsPermission()

        sendButton.setOnClickListener{
            var phoneNum = "119 112 fav 분기처리 해야함"
            try {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(phoneNum, null, getAddress(), null, null)
                Toast.makeText(requireContext(), "전송완료", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "전송실패", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
        return root
    }



    private fun getLatLng(): Location{

        var currentLatLng: Location? = null

        val hasFineLocationPermission = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION)

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED){
            val locatioNProvider = LocationManager.GPS_PROVIDER
            currentLatLng = lm?.getLastKnownLocation(locatioNProvider)
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), REQUIRED_PERMISSIONS[0])){
                Toast.makeText(requireContext(), "SOS를 사용하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            }else{
                ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            }
            currentLatLng = getLatLng()
        }
        return currentLatLng!!
    }


    private fun getAddress(): String? {

        lm = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var userLocation: Location = getLatLng()
        var userAddress: String? = null

        if (userLocation != null) {
            var latitude = userLocation.latitude
            var longitude = userLocation.longitude

            var mGeoCoder = Geocoder(requireContext(), Locale.KOREAN)
            var currentAddress: List<Address>? = null
            try {
                currentAddress = mGeoCoder.getFromLocation(
                    latitude!!, longitude!!, 1
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (currentAddress != null) {
                userAddress = currentAddress[0].getAddressLine(0).substring(5)
            }
        }
        return userAddress
    }

    fun checkSmsPermission() {

        val hasSendSmsPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
        if (hasSendSmsPermission == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "SMS를 발신할 수 있습니다", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "SMS 발신권한이 없습니다", Toast.LENGTH_SHORT).show()
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.SEND_SMS)) {
                Toast.makeText(requireContext(), "SMS 권한이 필요합니다", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.SEND_SMS), SMS_SEND_PERMISSON)
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.SEND_SMS), SMS_SEND_PERMISSON)
            }
        }
    }
}