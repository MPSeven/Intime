package kr.go.mapo.intime.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import kr.go.mapo.intime.ContactsAdapter
import kr.go.mapo.intime.R
import kr.go.mapo.intime.model.Contacts
import kr.go.mapo.intime.room.IntimeDatabase

class SettingAddContact : AppCompatActivity() {

    private var db : IntimeDatabase? = null
    private var conList = mutableListOf<Contacts>()
    private var selectedCon: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_add_contact)

        db = IntimeDatabase.getInstance(this)

        findViewById<ImageButton>(R.id.setting_addcon_btn_from).setOnClickListener {
            val intent = (Intent(Intent.ACTION_PICK))
            intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
            startActivityForResult(intent, 100)
        }
    }

    @SuppressLint("ResourceType")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var selName = findViewById<EditText>(R.id.setting_addcon_con_namespace)
        var selNum = findViewById<EditText>(R.id.setting_addcon_con_numspace)
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

                        val contact = Contacts(0, name = selName.text.toString(), phoneNumber = selNum.text.toString())
                        findViewById<ImageButton>(R.id.setting_addcon_btn).setOnClickListener {

                            Log.d("여기", contact.toString())
                            db?.contactsDao()?.insertCon(contact)
                            conList.add(contact)
                            onBackPressed()
                        }
                    }
                }
            }
        }
    }
}