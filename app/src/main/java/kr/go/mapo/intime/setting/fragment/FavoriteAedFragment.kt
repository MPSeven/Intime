package kr.go.mapo.intime.setting.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.FragmentFavoriteAedBinding
import kr.go.mapo.intime.map.model.Aed
import kr.go.mapo.intime.setting.FavoriteAedAdapter
import kr.go.mapo.intime.setting.database.DataBaseProvider
import kotlin.coroutines.CoroutineContext


class FavoriteAedFragment : Fragment(), CoroutineScope{

    private val favoriteAedAdapter = FavoriteAedAdapter()

    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var _binding: FragmentFavoriteAedBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteAedBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    fun init() = with(binding) {
        favoriteAedRecyclerView.adapter = favoriteAedAdapter
        favoriteAedRecyclerView.layoutManager = LinearLayoutManager(activity)

        getAedFromDB()
    }

    private fun getAedFromDB() = launch {
        withContext(Dispatchers.IO) {
            val repository =
                context?.let { DataBaseProvider.provideDB(it).bookmarkAedDao().getAll() }
            Log.d("FavoriteAedFragment", "$repository")

            withContext(Dispatchers.Main) {
                favoriteAedAdapter.submitList(repository)
            }
        }
    }

}