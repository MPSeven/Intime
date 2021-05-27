package kr.go.mapo.intime.sos

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.telephony.SmsManager
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kr.go.mapo.intime.common.CommonDialogFragment
import kr.go.mapo.intime.databinding.FragmentSosBinding
import kr.go.mapo.intime.setting.database.ContactsDatabase
import java.io.IOException
import java.util.*

class SosFragment : Fragment() {

    private var _binding: FragmentSosBinding? = null
    private val binding get() = _binding!!
    private var db : ContactsDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSosBinding.inflate(inflater, container,false)
        val root = binding.root

        tedPermission()

        binding.btnAmb.setOnClickListener {
            activity?.let{
                val intent = Intent(context, AmbulanceActivity::class.java)
                startActivity(intent)
            }
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
            .setDeniedMessage("SOS 사용을 위해서 위치 접근 권한과 전화 및 SMS 발신 권한이 필요합니다.\n[설정] > [권한] 에서 권한을 설정할 수 있습니다.")
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.SEND_SMS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS
            )
            .check()
    }


    private fun getLatLng(): Location{

        var lm: LocationManager? = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
        val spannableString = SpannableString(smsContents)
        val tBold = StyleSpan(Typeface.BOLD)
        spannableString.setSpan(tBold, 28, spannableString.length, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE)
        binding.txtSms.text = spannableString
        binding.txtSms.setText(spannableString)

        db = ContactsDatabase.getInstance(requireContext())
        var phoneNum = db?.contactsDao()?.selectSms(check = true)?.phoneNumber.toString()

        val smsManager = SmsManager.getDefault()

        binding.btn119.setOnClickListener {
            val dialog: CommonDialogFragment = CommonDialogFragment("알림", "119에 긴급 문자를 보내시겠습니까?") {
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

        binding.btn112.setOnClickListener {
            val dialog: CommonDialogFragment = CommonDialogFragment("알림", "112에 긴급 문자를 보내시겠습니까?",) {
                when (it) {
                    0 -> Toast.makeText(requireContext(), "전송취소", Toast.LENGTH_LONG).show()
                    1 -> {
                        smsManager.sendTextMessage("112", null, smsContents, null, null)
                        Toast.makeText(requireContext(), "전송완료", Toast.LENGTH_LONG).show()
                    }
                }
            }
            dialog.show(getChildFragmentManager(), dialog.tag)
        }

        binding.btnFav.setOnClickListener{

            if (db?.contactsDao()?.countSms(check = true) == 0){
                Toast.makeText(requireContext(), "등록된 비상연락처가 없습니다\n등록 후 사용해주세요", Toast.LENGTH_LONG).show()
            } else{
                val dialog: CommonDialogFragment = CommonDialogFragment("알림", "비상연락처에 긴급 문자를 보내시겠습니까?",) {
                    when (it) {
                        0 -> Toast.makeText(requireContext(), "전송취소", Toast.LENGTH_LONG).show()
                        1 -> {
                            smsManager.sendTextMessage(phoneNum, null, smsContents, null, null)
                            Toast.makeText(requireContext(), "전송완료", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                dialog.show(getChildFragmentManager(), dialog.tag)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}