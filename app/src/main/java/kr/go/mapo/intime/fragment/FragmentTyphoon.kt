package kr.go.mapo.intime.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.FragmentTyphoonBinding

class FragmentTyphoon : Fragment(R.layout.fragment_typhoon) {
    private var _binding:FragmentTyphoonBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTyphoonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}
