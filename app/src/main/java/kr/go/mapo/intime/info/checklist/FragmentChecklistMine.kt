package kr.go.mapo.intime.info.checklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class FragmentChecklistMine : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    companion object{
        fun newInstance(): Fragment{
            val fragment:Fragment = FragmentChecklistMine()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
