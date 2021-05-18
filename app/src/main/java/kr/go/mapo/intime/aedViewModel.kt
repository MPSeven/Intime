package kr.go.mapo.intime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.go.mapo.intime.model.SortedAed

class aedViewModel: ViewModel() {

    val aeds: MutableLiveData<List<SortedAed>> by lazy {
        MutableLiveData<List<SortedAed>>()
    }

    fun getAed() : LiveData<List<SortedAed>> {
        return aeds
    }



}