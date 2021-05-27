package kr.go.mapo.intime.setting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_setting_favorite.*
import kotlinx.coroutines.*
import kr.go.mapo.intime.databinding.ActivitySettingFavoriteBinding
import kr.go.mapo.intime.setting.database.DataBaseProvider
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.coroutines.CoroutineContext

class SettingFavoriteActivity : AppCompatActivity(), CoroutineScope{

    val binding by lazy { ActivitySettingFavoriteBinding.inflate(layoutInflater) }

    private val tabTextList = arrayListOf("전체", "심장충격기", "대피소", "응급실", "24시 약국")
    private var aedCount = 0
    private var shelterCount = 0
    private var itemCount = 0

    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        bindViews()

    }

    private fun initViews() = with(binding) {
        containerFavorite.adapter = FavoriteCollectionAdapter(this@SettingFavoriteActivity)
        TabLayoutMediator(settingTabLayout, containerFavorite) { tab, position ->
            tab.text = tabTextList[position]
        }.attach()

        getAllItemCount()

    }

    private fun bindViews() = with(binding) {
        binding.settingFavX.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getAllItemCount() = launch {
        withContext(Dispatchers.IO) {
            val repository =
                DataBaseProvider.provideDB(applicationContext).bookmarkAedDao().getAll()

            val repository2 = DataBaseProvider.provideDB(applicationContext).bookmarkShelterDao().getAll()

            aedCount = repository.size
            shelterCount = repository2.size
            itemCount = aedCount + shelterCount

            withContext(Dispatchers.Main) {
                binding.settingFavCnt.text = "총 ${itemCount}개"
            }
        }
    }

}