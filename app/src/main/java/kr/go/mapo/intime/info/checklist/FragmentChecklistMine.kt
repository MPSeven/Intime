package kr.go.mapo.intime.info.checklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.go.mapo.intime.databinding.FragmentChecklistMineBinding

class FragmentChecklistMine : Fragment() {

    private var _binding: FragmentChecklistMineBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChecklistMineBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.chMineBtn.setOnClickListener {
            val dialog: AddChecklistFragment = AddChecklistFragment()
            dialog.show(childFragmentManager, dialog.tag)
        }

        return root
    }
}
