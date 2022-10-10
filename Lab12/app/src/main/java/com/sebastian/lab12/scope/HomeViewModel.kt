package com.sebastian.lab12.scope

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.net.ssl.SSLEngineResult

class HomeViewModel:ViewModel() {

    private val _statusApp:MutableStateFlow<StatusApp> = MutableStateFlow<StatusApp>(StatusApp.default)
    val statusApp: StateFlow<StatusApp> = _statusApp

    sealed class StatusApp{
        object default: StatusApp()
        object succes: StatusApp()
        object failure: StatusApp()
        object Empty: StatusApp()
        object loading: StatusApp()
    }

    fun reset(){
        _statusApp.value = StatusApp.default

    }

    fun exito(){
        _statusApp.value = StatusApp.succes
    }

    fun fallo(){
        _statusApp.value = StatusApp.failure
    }

    fun vacio(){
        _statusApp.value = StatusApp.Empty
    }

    fun carga(){
        viewModelScope.launch {
            _statusApp.value = StatusApp.loading
            delay(10000L)

        }
    }
}