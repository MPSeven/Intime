package kr.go.mapo.intime.info.checklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.databinding.FragmentChecklistMineBinding
import kr.go.mapo.intime.info.checklist.database.ChecklistDatabase
import kr.go.mapo.intime.info.checklist.model.Checklist

class FragmentChecklistMine : Fragment() {

    private var _binding: FragmentChecklistMineBinding? = null
    private val binding get() = _binding!!
    private var db: ChecklistDatabase? = null
    private var chkList = mutableListOf<Checklist>()
    private lateinit var recyclerview: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChecklistMineBinding.inflate(inflater, container, false)
        val root = binding.root

        listSet()

        binding.chMineBtn.setOnClickListener {
//            val dialog: AddChecklistDialogFragment = AddChecklistDialogFragment()
//            dialog.show(childFragmentManager, dialog.tag)
            val intent = Intent(context, AddChecklist::class.java)
            startActivity(intent)

        }

        return root
    }

    override fun onResume() {
        super.onResume()
        listSet()
    }
    private lateinit var chkAdapter: ChecklistAdapter
    private fun listSet(){
        db = ChecklistDatabase.getInstance(requireContext())
        val savedChklist = db!!.checklistDao().getList()
        if(savedChklist.isNotEmpty()){
            chkList.clear()
            chkList.addAll(savedChklist)
            binding.chMineDefault.visibility = View.GONE
            binding.chMinerv.visibility = View.VISIBLE
        }
        chkAdapter = ChecklistAdapter(chkList)


        recyclerview = binding.chMinerv
        recyclerview.apply {
            adapter = chkAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}
