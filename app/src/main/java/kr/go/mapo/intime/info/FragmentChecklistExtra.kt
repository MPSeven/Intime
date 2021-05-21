package kr.go.mapo.intime.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class FragmentChecklistExtra : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    companion object{
        fun newInstance(): Fragment{
            val fragment:Fragment = FragmentChecklistExtra()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
