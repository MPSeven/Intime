package kr.go.mapo.intime.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.FragmentDisasterBinding

class FragmentDisaster : Fragment(R.layout.fragment_disaster) {
    private var _binding:FragmentDisasterBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDisasterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.infoTbEarthquake.setOnClickListener { replaceFragment(it)}
        binding.infoTbTyphoon.setOnClickListener { replaceFragment(it)}
        binding.infoTbFlood.setOnClickListener { replaceFragment(it)}
        binding.infoTbForrestfire.setOnClickListener { replaceFragment(it)}
        binding.infoTbFire.setOnClickListener { replaceFragment(it)}
    }

    private fun replaceFragment(view:View){
        val replaceFragment = when(view.id){
            binding.infoTbEarthquake.id -> FragmentEarthquake()
            binding.infoTbTyphoon.id -> FragmentTyphoon()
            binding.infoTbFlood.id -> FragmentFlood()
            binding.infoTbForrestfire.id -> FragmentForestFire()
            else -> FragmentUrbanFire()
        }
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.frameLayout, replaceFragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }

    companion object {
        fun newInstance(): FragmentDisaster {
            return FragmentDisaster()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}