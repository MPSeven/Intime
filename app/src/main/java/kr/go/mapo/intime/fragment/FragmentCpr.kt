package kr.go.mapo.intime.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.go.mapo.intime.R

class FragmentCpr : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_basic_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnClickListener(View.OnClickListener {

            when(view.id){
                R.id.info_tb_adult ->{
                    val fragmentManager = activity?.supportFragmentManager
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    fragmentTransaction?.replace(R.id.layout_cpr_basic, FragmentCpr())
                    fragmentTransaction?.addToBackStack(null)
                    fragmentTransaction?.commit()
                }else ->{
                val fragmentManager = activity?.supportFragmentManager
                val fragmentTransaction = fragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.container_cpr, FragmentCprChild())
                fragmentTransaction?.addToBackStack(null)
                fragmentTransaction?.commit()
                }
            }
        })
    }

}
