package kr.go.mapo.intime.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.PhoneNumberFormattingTextWatcher
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.go.mapo.intime.databinding.ActivitySettingAddContactBinding
import kr.go.mapo.intime.model.Contacts
import kr.go.mapo.intime.setting.database.ContactsDatabase

class SettingAddContact : AppCompatActivity() {

    private var db : ContactsDatabase? = null
    private var conList = mutableListOf<Contacts>()
    private var selectedCon: Uri? = null
    private lateinit var selName: EditText
    private lateinit var selNum: EditText
    private lateinit var chk: CheckBox
    val binding by lazy { ActivitySettingAddContactBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        selName = binding.settingAddconConNamespace
        selNum = binding.settingAddconConNumspace
        chk = binding.settingAddconChk

        selNum.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        db = ContactsDatabase.getInstance(this)

        binding.settingAddconBtnFrom.setOnClickListener {
            val intent = (Intent(Intent.ACTION_PICK))
            intent.data = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            startActivityForResult(intent, 100)
        }

        binding.settingAddconX.setOnClickListener {
            onBackPressed()
        }

        binding.settingAddconBtn.setOnClickListener {
            if(selName.text.toString().isBlank() || selNum.text.toString().isBlank() ){
                Toast.makeText(applicationContext,"비상연락처를 입력해주세요", Toast.LENGTH_LONG).show()
            } else if (db?.contactsDao()?.countAll()!! >= 5) {
                Toast.makeText(applicationContext, "비상연락처는 다섯개까지 등록 가능합니다", Toast.LENGTH_LONG).show()
            } else if (chk.isChecked) {
                if (db?.contactsDao()?.countSms(check = true)!! >= 1) {
                    Toast.makeText(applicationContext, "대표 연락처는 하나만 등록 가능합니다", Toast.LENGTH_LONG).show()
                } else{
                    Toast.makeText(applicationContext,"대표 연락처로 설정되었습니다", Toast.LENGTH_LONG).show()
                    val contact = Contacts(0, name = selName.text.toString(), phoneNumber = selNum.text.toString(), chk = chk.isChecked)
                    db?.contactsDao()?.insertCon(contact)
                    conList.add(contact)
                    onBackPressed()
                }
            } else {
                val contact = Contacts(0, name = selName.text.toString(), phoneNumber = selNum.text.toString(), chk = chk.isChecked)
                db?.contactsDao()?.insertCon(contact)
                conList.add(contact)
                onBackPressed()
            }
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