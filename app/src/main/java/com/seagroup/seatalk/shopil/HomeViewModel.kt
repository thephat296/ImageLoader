package com.seagroup.seatalk.shopil

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope

class HomeViewModel : ViewModel() {
    fun loadImages(): LiveData<List<String>> = liveData(viewModelScope.coroutineContext) {
        emit(listOf("https://picsum.photos/id/119/3264/2176"))
    }
}
