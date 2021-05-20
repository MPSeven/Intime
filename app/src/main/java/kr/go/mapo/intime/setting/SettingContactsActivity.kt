package kr.go.mapo.intime.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.ContactsAdapter
import kr.go.mapo.intime.R
import kr.go.mapo.intime.fragment.CommonDialogFragment
import kr.go.mapo.intime.model.Contacts
import kr.go.mapo.intime.room.IntimeDatabase

class SettingContactsActivity : AppCompatActivity() {

    private lateinit var recyclerview: RecyclerView
    private var db : IntimeDatabase? = null
    private var conList = mutableListOf<Contacts>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_contact)

        db = IntimeDatabase.getInstance(this)

        val savedContacts = db!!.contactsDao().getCon()
        if(savedContacts.isNotEmpty()){
            conList.addAll(savedContacts)
        }
        Log.d("여기", conList.toString())
        val adapter = ContactsAdapter(conList)


        recyclerview = findViewById(R.id.setting_con_rv)
        recyclerview.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }


        findViewById<ImageButton>(R.id.setting_plus).setOnClickListener {
            val intent = Intent(this, SettingAddContact::class.java)
            startActivity(intent)
        }

        adapter.setItemClickListener(object : ContactsAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {

                val dialog: CommonDialogFragment = CommonDialogFragment("비상연락처 삭제", "비상연락처를 삭제 하시겠습니까? \n삭제하면 복구가 불가능합니다.") {
                    when (it) {
                        0 -> Toast.makeText(applicationContext,"삭제취소", Toast.LENGTH_SHORT).show()
                        1 -> {
                            val contacts = conList[position]
                            db?.contactsDao()?.deleteCon(contacts = contacts)
                            conList.removeAt(position)
                            adapter.notifyDataSetChanged()
                            Toast.makeText(applicationContext, "삭제완료", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                dialog.show(supportFragmentManager, dialog.tag)
            }
        })


        findViewById<ImageButton>(R.id.setting_con_x).setOnClickListener {
            onBackPressed()
        }
    }


}