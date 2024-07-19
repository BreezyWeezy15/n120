package com.nbc.nbcreplicate.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.nbcreplicate.models.HomePage
import com.nbc.nbcreplicate.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    private val _dataStateFlow : MutableStateFlow<UiStates> = MutableStateFlow(UiStates.INITIAL)
    val dataState : StateFlow<UiStates> get() = _dataStateFlow


    fun getData(){
        viewModelScope.launch {
            _dataStateFlow.value = UiStates.LOADING
            delay(1500)
            try {
                appRepository.getHomePageData().collectLatest {
                    _dataStateFlow.value = UiStates.SUCCESS(it)
                }
            } catch (ex : Exception){
                _dataStateFlow.value = UiStates.ERROR(ex.message.toString())
            }
        }
    }


    sealed class UiStates {
        data object INITIAL : UiStates()
        data class  SUCCESS(var homePage: HomePage) : UiStates()
        data class  ERROR(var error : String) : UiStates()
        data object LOADING : UiStates()

    }

}