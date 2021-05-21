package kr.go.mapo.intime.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.FragmentFloodBinding

class FragmentFlood : Fragment(R.layout.fragment_flood) {
    private var _binding:FragmentFloodBinding?=null
            private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFloodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


}
