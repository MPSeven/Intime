package kr.go.mapo.intime.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.FragmentDisasterBinding

class FragmentDisaster : Fragment() {
    private var _binding:FragmentDisasterBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDisasterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance():FragmentDisaster{
            return FragmentDisaster()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}