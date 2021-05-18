package kr.go.mapo.intime.setting

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.ContactsAdapter
import kr.go.mapo.intime.R
import kr.go.mapo.intime.model.Contacts
import kr.go.mapo.intime.room.IntimeDatabase

class SettingContactsActivity : AppCompatActivity() {

    private lateinit var recyclerview: RecyclerView

    private val TAG = "SettingContactActivity"
    private var db : IntimeDatabase? = null
    private var conList = mutableListOf<Contacts>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_contact)

        recyclerview = findViewById(R.id.setting_con_rv)
        val adapter = ContactsAdapter(conList)

        db = IntimeDatabase.getInstance(this)

        val savedContacts = db!!.contactsDao().getCon()
        if(savedContacts.isNotEmpty()){
            conList.addAll(savedContacts)
        }

        recyclerview.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }


        findViewById<ImageButton>(R.id.setting_plus).setOnClickListener {

            val intent = (Intent(Intent.ACTION_PICK))
            intent.setData(ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(intent, 100)


            with(contentResolver) {
                query(ContactsContract.Data.CONTENT_URI, arrayOf(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                    ), null, null, null
                )
            }?.apply {
                moveToFirst()
                val name = getString(0)
                val number = getString(1)

                close()
                Log.d("여기", name)
                Log.d("여기",number)

                val contact = Contacts(0, name = name, phoneNumber = number)

//                room에 저장
//            db?.contactsDao()?.insertCon(contact)
//            conList.add(contact)
            adapter.notifyDataSetChanged()
            }

        }




        findViewById<ImageButton>(R.id.setting_con_x).setOnClickListener {
            onBackPressed()
        }
    }

//    @SuppressLint("ResourceType")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        if (requestCode == 100 && resultCode == RESULT_OK) {
//            if (data != null) {
//                data.getData()?.let {
//                    contentResolver.query(
//                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrayOf(
//                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
//                            ContactsContract.CommonDataKinds.Phone.NUMBER
//                        ), null, null, null
//                    )
//                }?.apply {
//                    moveToFirst()
//                    val name = getString(0)
//                    val number = getString(1)
//                    Log.d("여기", name)
//                    Log.d("여기", number)
//                }
//            }
//            super.onActivityResult(requestCode, resultCode, data)
//        }
//    }
//




}