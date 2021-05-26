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
import kr.go.mapo.intime.databinding.FragmentFavoriteEmergencyBinding
import kr.go.mapo.intime.map.model.Aed
import kr.go.mapo.intime.setting.FavoriteAedAdapter
import kr.go.mapo.intime.setting.database.DataBaseProvider
import kotlin.coroutines.CoroutineContext


class FavoriteEmergencyFragment : Fragment(){

    private var _binding: FragmentFavoriteEmergencyBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteEmergencyBinding.inflate(inflater, container, false)

        return binding.root
    }


}