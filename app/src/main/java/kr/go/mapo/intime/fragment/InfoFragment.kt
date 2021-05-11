package kr.go.mapo.intime.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.FragmentInfoBinding

class InfoFragment : Fragment(R.layout.fragment_info) {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ibHowToAed.setOnClickListener(View.OnClickListener {
            val AedFragment = FragmentAed()
            val fragmentManager = getActivity()?.getSupportFragmentManager()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.frameLayout, AedFragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()

        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}