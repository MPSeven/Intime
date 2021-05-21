package kr.go.mapo.intime.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kr.go.mapo.intime.databinding.FragmentCommonDialogBinding


class CommonDialogFragment(
    private val title: String,
    private val message: String,
    val clickItem: (Int) -> Unit) : DialogFragment() {

    private lateinit var binding: FragmentCommonDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommonDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.popTitle.text = title

        binding.popAsk.text = message

        binding.popCancel.setOnClickListener {
            clickItem(0)
            dialog?.dismiss()
        }

        binding.popConfirm.setOnClickListener {
            clickItem(1)
            dialog?.dismiss()
        }
    }
}