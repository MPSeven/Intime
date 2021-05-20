package kr.go.mapo.intime.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.ContactsAdapter
import kr.go.mapo.intime.R
import kr.go.mapo.intime.model.Contacts
import kr.go.mapo.intime.room.IntimeDatabase
import java.util.*

class SettingContactsActivity : AppCompatActivity() {

    private lateinit var recyclerview: RecyclerView
    private var db : IntimeDatabase? = null
    private var conList = mutableListOf<Contacts>()
    private var selectedCon: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_contact)

        db = IntimeDatabase.getInstance(this)

        val savedContacts = db!!.contactsDao().getCon()
        if(savedContacts.isNotEmpty()){
            conList.addAll(savedContacts)
        }
//        Log.d("여기", conList.toString())
        val adapter = ContactsAdapter(conList)

        recyclerview = findViewById(R.id.setting_con_rv)
        recyclerview.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

//        findViewById<ImageButton>(R.id.setting_plus).setOnClickListener {
//            val intent = (Intent(Intent.ACTION_PICK))
//            intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
//            startActivityForResult(intent, 100)
//        }

        findViewById<ImageButton>(R.id.setting_plus).setOnClickListener {
            val intent = Intent(this, SettingAddContact::class.java)
            startActivity(intent)
        }

        adapter.setItemClickListener(object : ContactsAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {

                val contacts = conList[position]

                db?.contactsDao()?.deleteCon(contacts = contacts) //DB에서 삭제
                conList.removeAt(position) //리스트에서 삭제
                adapter.notifyDataSetChanged() //리스트뷰 갱신
            }
        })


        findViewById<ImageButton>(R.id.setting_con_x).setOnClickListener {
            onBackPressed()
        }
    }

//    @SuppressLint("ResourceType")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        val refresh = ContactsAdapter(conList)
//        if (resultCode == RESULT_OK) {
//            when (requestCode) {
//                100 -> {
//                    selectedCon = data?.data
//                    with(contentResolver) {
//                        selectedCon?.let {
//                            query(
//                                it, arrayOf(
//                                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
//                                    ContactsContract.CommonDataKinds.Phone.NUMBER
//                                ), null, null, null
//                            )
//                        }
//                    }?.apply {
//                        moveToFirst()
//                        val contact = Contacts(0, name = getString(0), phoneNumber = getString(1))
//                        db?.contactsDao()?.insertCon(contact)
//                        conList.add(contact)
//                        close()
////                        Log.d("여기", contact.toString()
//                    }
//                }
//            }
//        }
//    }

}