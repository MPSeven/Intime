package kr.go.mapo.intime.info.checklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.go.mapo.intime.databinding.FragmentChecklistExtraBinding

class FragmentChecklistExtra : Fragment() {

    private var _binding: FragmentChecklistExtraBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChecklistExtraBinding.inflate(inflater, container, false)
        val root = binding.root


        return root
    }
}
