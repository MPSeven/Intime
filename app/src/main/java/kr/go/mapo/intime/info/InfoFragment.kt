package kr.go.mapo.intime.info

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.FragmentInfoBinding
import kr.go.mapo.intime.info.checklist.ChecklistActivity
import kr.go.mapo.intime.info.checklist.FragmentChecklist

class InfoFragment : Fragment(R.layout.fragment_info) {
    private var _binding:FragmentInfoBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ibHowToAed.setOnClickListener {
            /*replaceFragment(it)*/
            activity?.let {
                val intent = Intent(context, AedActivity::class.java)
                startActivity(intent)
            }

        }
        binding.ibHowToCpr.setOnClickListener(View.OnClickListener {
            /*replaceFragment(it)*/
            activity?.let {
                val intent = Intent(context, CprActivity::class.java)
                startActivity(intent)
            }
        })
        binding.ibHowToDisaster.setOnClickListener(View.OnClickListener {
            /*replaceFragment(it)*/
            activity?.let {
                val intent = Intent(context, DisasterActivity::class.java)
                startActivity(intent)
            }
        })
        binding.ibHowToChecklist.setOnClickListener(View.OnClickListener {
            activity?.let{
                val intent = Intent(context, ChecklistActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun replaceFragment(view: View) {
        val replaceFragment = when (view.id) {
            binding.ibHowToAed.id -> {
                FragmentAed()
            }
            binding.ibHowToCpr.id -> {
                FragmentCpr()
            }
            binding.ibHowToDisaster.id -> {
                FragmentDisaster()
            }
            else -> {
                FragmentChecklist()
            }

        }
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.frameLayout, replaceFragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}