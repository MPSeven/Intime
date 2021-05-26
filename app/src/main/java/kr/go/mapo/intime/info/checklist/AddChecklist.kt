package kr.go.mapo.intime.info.checklist

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kr.go.mapo.intime.databinding.ActivityAddChecklistBinding
import kr.go.mapo.intime.info.checklist.database.ChecklistDatabase
import kr.go.mapo.intime.info.checklist.model.Checklist

class AddChecklist : AppCompatActivity() {

    val binding by lazy { ActivityAddChecklistBinding.inflate(layoutInflater) }
    private var db: ChecklistDatabase? = null
    private var chkList = mutableListOf<Checklist>()
    private lateinit var addTitle: EditText
    private lateinit var addCon: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        db = ChecklistDatabase.getInstance(this)

        addTitle = binding.chAddTitle
        addCon = binding.chAddCon

        binding.chConfirm.setOnClickListener {
            val chk = Checklist(0, addTitle.text.toString(), addCon.text.toString(),8, false)
            db?.checklistDao()?.insert(chk)
            finish()
        }



        binding.chX.setOnClickListener {
            onBackPressed()
        }
    }


}