package kr.go.mapo.intime.setting.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import kr.go.mapo.intime.R
import kr.go.mapo.intime.databinding.FragmentFavoriteAedBinding
import kr.go.mapo.intime.databinding.FragmentFavoriteShelterBinding
import kr.go.mapo.intime.setting.FavoriteAedAdapter
import kr.go.mapo.intime.setting.FavoriteShelterAdapter
import kr.go.mapo.intime.setting.database.DataBaseProvider
import kotlin.coroutines.CoroutineContext


class FavoriteShelterFragment : Fragment(), CoroutineScope {

    private val favoriteShelterAdapter = FavoriteShelterAdapter()

    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var _binding: FragmentFavoriteShelterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteShelterBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    fun init() = with(binding) {
        favoriteShelterRecyclerView.adapter = favoriteShelterAdapter
        favoriteShelterRecyclerView.layoutManager = LinearLayoutManager(activity)

        getAedFromDB()
    }

    private fun getAedFromDB() = launch {
        withContext(Dispatchers.IO) {
            val repository =
                context?.let { DataBaseProvider.provideDB(it).bookmarkShelterDao().getAll() }
            Log.d("FavoriteAedFragment", "$repository")

            withContext(Dispatchers.Main) {

                if (repository?.size == 0) {
                    binding.favoriteShelterRecyclerView.visibility = View.GONE
                    binding.favoriteShelterTitle.isVisible = false
                    binding.favoriteNoShelterTextView.visibility = View.VISIBLE
                } else {
                    binding.favoriteShelterTitle.isVisible = true
                    binding.favoriteShelterRecyclerView.isVisible = true
                    binding.favoriteNoShelterTextView.isVisible = false
                }

                favoriteShelterAdapter.submitList(repository)
            }
        }
    }

}