package kr.go.mapo.intime.info.checklist

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.FragmentAddChecklistDialogBinding
import kr.go.mapo.intime.info.checklist.database.ChecklistDatabase
import kr.go.mapo.intime.info.checklist.model.Checklist
import java.lang.ClassCastException

class AddChecklistDialogFragment() : DialogFragment() {
    private lateinit var binding: FragmentAddChecklistDialogBinding
    private var db: ChecklistDatabase? = null
    private var chkList = mutableListOf<Checklist>()
    private lateinit var addTitle: EditText
    private lateinit var addCon: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomFullDialog)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddChecklistDialogBinding.inflate(inflater, container, false)

        db = ChecklistDatabase.getInstance(requireContext())

        addTitle = binding.chAddTitle
        addCon = binding.chAddCon

        binding.chConfirm.setOnClickListener {
            val chk = Checklist(0, addTitle.text.toString(), addCon.text.toString(),8, false)
            db?.checklistDao()?.insert(chk)
            dismiss()
        }

        binding.chX.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
        dialog?.window?.setGravity(Gravity.BOTTOM)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val parentFragment: Fragment? = parentFragment

        if(parentFragment is DialogInterface.OnDismissListener) {
            (parentFragment as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }

}