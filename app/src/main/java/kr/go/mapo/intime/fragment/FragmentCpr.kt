package kr.go.mapo.intime.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.go.mapo.intime.databinding.FragmentBasicInfoBinding

class FragmentCpr : Fragment() {
    private var _binding: FragmentBasicInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasicInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object{
        fun newInstance():FragmentCpr{
            return FragmentCpr()
        }
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
