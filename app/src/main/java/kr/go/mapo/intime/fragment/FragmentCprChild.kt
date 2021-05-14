package kr.go.mapo.intime.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.go.mapo.intime.databinding.FragmentCprChildBinding

class FragmentCprChild : Fragment() {
    private var _binding:FragmentCprChildBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCprChildBinding.inflate(inflater, container, false)
        return binding.root
    }

}