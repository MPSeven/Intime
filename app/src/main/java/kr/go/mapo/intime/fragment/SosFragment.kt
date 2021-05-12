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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.FragmentSosBinding
import java.io.IOException
import java.util.*

class SosFragment : Fragment() {

    private var _binding: FragmentSosBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSosBinding.inflate(inflater, container,false)
        val root = binding.root

        tedPermission()

        binding.btnAmb.setOnClickListener {
            val ambFragment = SosAmbFragment()
            val fragmentManager = getActivity()?.getSupportFragmentManager()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.frameLayout, ambFragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }

        return root
    }


    private fun tedPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                sendSms()
                binding.gpsAddress.setText(getAddress())
            }
            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                binding.gpsAddress.setText("위치 접근 권한이 필요합니다.")
                binding.txtSms.setText("위치 접근 권한이 필요합니다.")
                binding.btn119.setOnClickListener {
                    Toast.makeText(requireContext(), "SMS 발신권한이 없습니다", Toast.LENGTH_SHORT).show()
                }
                binding.btn112.setOnClickListener {
                    Toast.makeText(requireContext(), "SMS 발신권한이 없습니다", Toast.LENGTH_SHORT).show()
                }
                binding.btnFav.setOnClickListener {
                    Toast.makeText(requireContext(), "SMS 발신권한이 없습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
        TedPermission.with(requireContext())
            .setPermissionListener(permissionListener)
//            .setRationaleMessage("SOS 사용을 위해서 위치 접근 권한과 SMS 발신 권한이 필요합니다.")
            .setDeniedMessage("SOS 사용을 위해서 위치 접근 권한과 SMS 발신 권한이 필요합니다.\n[설정] > [권한] 에서 권한을 설정할 수 있습니다.")
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.SEND_SMS
            )
            .check()
    }


    private fun getLatLng(): Location{

        var lm: LocationManager? = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var currentLatLng: Location? = null

        val hasFineLocationPermission = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION)

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED ||
        hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED){
            val locatioNProvider = LocationManager.GPS_PROVIDER
            currentLatLng = lm?.getLastKnownLocation(locatioNProvider)
        }else {
            tedPermission()
        }
        return currentLatLng!!
    }


    private fun getAddress(): String? {

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


    private fun sendSms() {

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