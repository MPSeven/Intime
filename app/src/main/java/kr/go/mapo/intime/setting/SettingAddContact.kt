package kr.go.mapo.intime.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.go.mapo.intime.R
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

        db = ContactsDatabase.getInstance(this)

        binding.settingAddconBtnFrom.setOnClickListener {
            val intent = (Intent(Intent.ACTION_PICK))
            intent.data = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            startActivityForResult(intent, 100)
        }

        binding.settingAddconX.setOnClickListener {
            onBackPressed()
        }

        chk.setOnClickListener {
            if(chk.isChecked){
                if(selName.text.toString().isBlank() || selNum.text.toString().isBlank()){
                    Toast.makeText(applicationContext,"비상연락처를 입력해주세요", Toast.LENGTH_LONG).show()
                } else{
                    Toast.makeText(applicationContext,"대표 연락처로 설정되었습니다", Toast.LENGTH_LONG).show()
                }
            }
        }


        binding.settingAddconBtn.setOnClickListener {
            if(selName.text.toString().isBlank() || selNum.text.toString().isBlank() ){
                Toast.makeText(applicationContext,"비상연락처를 입력해주세요", Toast.LENGTH_LONG).show()
            } else{
                val contact = Contacts(0, name = selName.text.toString(), phoneNumber = selNum.text.toString(), chk = chk.isChecked)
//            Log.d("여기", contact.toString())
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