package kr.go.mapo.intime.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.R
import kr.go.mapo.intime.common.CommonDialogFragment
import kr.go.mapo.intime.databinding.ActivitySettingContactBinding
import kr.go.mapo.intime.model.Contacts
import kr.go.mapo.intime.setting.database.ContactsDatabase

class SettingContactsActivity : AppCompatActivity() {

    private lateinit var recyclerview: RecyclerView
    private var db : ContactsDatabase? = null
    private var conList = mutableListOf<Contacts>()
    val binding by lazy { ActivitySettingContactBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        displayList()

        binding.settingPlus.setOnClickListener {
            if (db?.contactsDao()?.countAll()!! >= 5) {
                Toast.makeText(applicationContext, "비상연락처는 다섯개까지 등록 가능합니다", Toast.LENGTH_LONG).show()
            } else{
                val intent = Intent(this, SettingAddContact::class.java)
                startActivity(intent)
            }
        }

        contactAdapter.setItemClickListener(object : ContactsAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {

                val dialog = CommonDialogFragment("비상연락처 삭제", "비상연락처를 삭제 하시겠습니까? \n삭제하면 복구가 불가능합니다.") {
                    when (it) {
                        0 -> Toast.makeText(applicationContext,"삭제취소", Toast.LENGTH_SHORT).show()
                        1 -> {
                            val contacts = conList[position]
                            db?.contactsDao()?.deleteCon(contacts = contacts)
                            conList.removeAt(position)
                            contactAdapter.notifyDataSetChanged()
                            Toast.makeText(applicationContext, "삭제완료", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                dialog.show(supportFragmentManager, dialog.tag)
            }
        })


        binding.settingConX.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()

        displayList()
    }

    override fun onRestart(){
        super.onRestart()

    }
    private lateinit var contactAdapter: ContactsAdapter
    private fun displayList(){
        db = ContactsDatabase.getInstance(this)

        val savedContacts = db!!.contactsDao().getCon()
        if(savedContacts.isNotEmpty()){
            conList.clear()
            conList.addAll(savedContacts)
        }
//        Log.d("여기", conList.toString())
        contactAdapter = ContactsAdapter(conList)


        recyclerview = binding.settingConRv
        recyclerview.apply {
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}