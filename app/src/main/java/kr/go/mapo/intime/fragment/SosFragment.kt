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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kr.go.mapo.intime.databinding.FragmentSosBinding
import java.io.IOException
import java.util.*

class SosFragment : Fragment() {

    private var _binding: FragmentSosBinding? = null
    private val binding get() = _binding!!

    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    val SMS_SEND_PERMISSON = 1
    val PERMISSIONS_REQUEST_CODE = 100

    var lm: LocationManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSosBinding.inflate(inflater, container,false)
        val root = binding.root

        checkSmsPermission()

        binding.gpsAddress.setText(getAddress())

        return root
    }


    private fun getLatLng(): Location{

        var currentLatLng: Location? = null

        val hasFineLocationPermission = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION)

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED){
            if(LocationManager.GPS_PROVIDER.isBlank()){
                Toast.makeText(requireContext(), "1번", Toast.LENGTH_SHORT).show()
            } else {
                val locatioNProvider = LocationManager.GPS_PROVIDER
                currentLatLng = lm?.getLastKnownLocation(locatioNProvider)
            }
        }else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), REQUIRED_PERMISSIONS[0])){
                Toast.makeText(requireContext(), "SOS를 사용하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            }else{
                ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            }
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
        } else {
            userAddress = "gps 연결을 확인해주세요"
        }
        return userAddress
    }

    private fun checkSmsPermission() {

        val hasSendSmsPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
        if (hasSendSmsPermission != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "SMS 발신권한이 없습니다", Toast.LENGTH_SHORT).show()
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.SEND_SMS)) {
                Toast.makeText(requireContext(), "SMS 권한이 필요합니다", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.SEND_SMS), SMS_SEND_PERMISSON)
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.SEND_SMS), SMS_SEND_PERMISSON)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var smsContents = "[긴급문자]\n위급상황 시에 발신되는 긴급 문자입니다.\n"+ getAddress()
        binding.txtSms.setText(smsContents)
        var phoneNum = "즐겨찾기"
        val smsManager = SmsManager.getDefault()

        binding.btn119.setOnClickListener {
            val dialog: SosDialogFragment = SosDialogFragment {
                when (it) {
                    0 -> Toast.makeText(requireContext(), "전송취소", Toast.LENGTH_LONG).show()
                    1 -> {
                        smsManager.sendTextMessage("119", null, smsContents, null, null)
                        Toast.makeText(requireContext(), "전송완료", Toast.LENGTH_LONG).show()
                    }
                }
            }
            dialog.show(getChildFragmentManager(), dialog.tag)
        }
/*
        try{
            binding.btn119.setOnClickListener {
                val dialog = SosDialog()
                getFragmentManager()?.let {
                        it1 -> dialog.show(it1,"SosDialog")

                }

                smsManager.sendTextMessage("119", null, smsContents, null, null)
                Toast.makeText(requireContext(), "전송완료", Toast.LENGTH_LONG).show()
            }
            binding.btn112.setOnClickListener {
                smsManager.sendTextMessage("112", null, smsContents, null, null)
                Toast.makeText(requireContext(), "전송완료", Toast.LENGTH_LONG).show()
            }
            binding.btnFav.setOnClickListener{
                smsManager.sendTextMessage(phoneNum, null, smsContents, null, null)
                Toast.makeText(requireContext(), "전송완료", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception){
            Toast.makeText(requireContext(), "전송실패", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}