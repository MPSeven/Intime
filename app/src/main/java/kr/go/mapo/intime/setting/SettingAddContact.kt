package kr.go.mapo.intime.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.EditText
import android.widget.ImageButton
import kr.go.mapo.intime.R
import kr.go.mapo.intime.model.Contacts
import kr.go.mapo.intime.room.IntimeDatabase

class SettingAddContact : AppCompatActivity() {

    private var db : IntimeDatabase? = null
    private var conList = mutableListOf<Contacts>()
    private var selectedCon: Uri? = null
    private lateinit var selName: EditText
    private lateinit var selNum: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_add_contact)

        selName = findViewById(R.id.setting_addcon_con_namespace)
        selNum = findViewById(R.id.setting_addcon_con_numspace)

        db = IntimeDatabase.getInstance(this)

        findViewById<ImageButton>(R.id.setting_addcon_btn_from).setOnClickListener {
            val intent = (Intent(Intent.ACTION_PICK))
            intent.data = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            startActivityForResult(intent, 100)
        }

        findViewById<ImageButton>(R.id.setting_addcon_x).setOnClickListener {
            onBackPressed()
        }


        findViewById<ImageButton>(R.id.setting_addcon_btn).setOnClickListener {
            val contact = Contacts(0, name = selName.text.toString(), phoneNumber = selNum.text.toString())
//            Log.d("여기", contact.toString())
            db?.contactsDao()?.insertCon(contact)
            conList.add(contact)
            onBackPressed()
        }

    }

    @SuppressLint("ResourceType")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                100 -> {
                    selectedCon = data?.data
                    with(contentResolver) {
                        selectedCon?.let {
                            query(
                                it, arrayOf(
                                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                                ), null, null, null
                            )
                        }
                    }?.apply {
                        moveToFirst()
                        selName.setText(getString(0))
                        selNum.setText(getString(1))
                        close()
                    }
                }
            }
        }
    }

}