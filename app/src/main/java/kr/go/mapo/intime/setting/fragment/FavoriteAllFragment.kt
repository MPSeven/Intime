package kr.go.mapo.intime.setting.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.FragmentFavoriteAedBinding
import kr.go.mapo.intime.databinding.FragmentFavoriteAllBinding
import kr.go.mapo.intime.setting.FavoriteAedAdapter
import kr.go.mapo.intime.setting.database.DataBaseProvider
import java.lang.reflect.Array.getInt
import kotlin.coroutines.CoroutineContext

class FavoriteAllFragment : Fragment(), CoroutineScope {

    private val favoriteAedAdapterAll = FavoriteAedAdapter()

    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var _binding: FragmentFavoriteAllBinding? = null
    private val binding get() = _binding!!

    //private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteAllBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    fun init() = with(binding) {
        favoriteAedRecyclerViewAll.adapter = favoriteAedAdapterAll
        favoriteAedRecyclerViewAll.layoutManager = LinearLayoutManager(activity)

        LinearLayoutManager.VERTICAL


        getAedFromDB()
    }

    private fun getAedFromDB() = launch {
        withContext(Dispatchers.IO) {
            val repository =
                context?.let { DataBaseProvider.provideDB(it).bookmarkAedDao().getAll() }
            Log.d("FavoriteAedFragment", "$repository")

            withContext(Dispatchers.Main) {
                favoriteAedAdapterAll.submitList(repository)
            }
        }
    }

}


