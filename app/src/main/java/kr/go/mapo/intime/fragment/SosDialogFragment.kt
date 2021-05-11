package kr.go.mapo.intime.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kr.go.mapo.intime.databinding.FragmentSosDialogBinding


class SosDialogFragment(val clickItem: (Int) -> Unit) : DialogFragment() {

    private lateinit var binding: FragmentSosDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSosDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = "119에 긴급 문자를 보내시겟습니까?"

        binding.popAsk.text = text

        binding.popCancel.setOnClickListener {
            clickItem(0)
            dialog?.dismiss()
        }

        binding.popConfirm.setOnClickListener {
            clickItem(1)
            dialog?.dismiss()
        }
    }
    /*
       override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

           return activity?.let {
               val builder = AlertDialog.Builder(it)
               // Get the layout inflater
               val inflater = requireActivity().layoutInflater;

               // Inflate and set the layout for the dialog
               // Pass null as the parent view because its going in the dialog layout
               builder.setView(inflater.inflate(R.layout.fragment_dialog, null))
   *//*
                // Add action buttons
                .setPositiveButton(R.string.confirm,
                    DialogInterface.OnClickListener { dialog, id ->
                        // sign in the user ...
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })*//*
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")

    }
    */
}