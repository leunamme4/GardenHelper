package com.example.gardenhelper.ui.settings.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenhelper.data.network.Result
import com.example.gardenhelper.domain.api.repositories.ServerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(val serverRepository: ServerRepository, val context: Context): ViewModel() {
    private val _registrationCompleted = MutableLiveData<Boolean>()
    val registrationCompleted: LiveData<Boolean> = _registrationCompleted

    private val _loginCompleted = MutableLiveData<Boolean>()
    val loginCompleted: LiveData<Boolean> = _loginCompleted

    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("AuthDebug", "$email $password $confirmPassword")
            val result = serverRepository.register(email, password, confirmPassword)
            when(result) {
                is Result.Error -> {
                    _registrationCompleted.postValue(false)
                    Log.d("AuthDebug", "Result.Error")
                }
                Result.NetworkError -> {
                    _registrationCompleted.postValue(false)
                    Log.d("AuthDebug", "Result.NetworkError")
                }
                is Result.Success<*> -> {
                    _registrationCompleted.postValue(true)
                    Log.d("AuthDebug", "Result.Success")
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = serverRepository.login(email, password)
            when(result) {
                is Result.Error -> {
                    _loginCompleted.postValue(false)
                }
                Result.NetworkError -> {
                    _loginCompleted.postValue(false)
                }
                is Result.Success -> {
                    saveToken(result.data.token)
                    _loginCompleted.postValue(true)
                }
            }
        }
    }

    fun saveToken(token: String) {
        val prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        with (prefs.edit()) {
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    companion object {
        const val PREFS_KEY = "auth_key"
        const val TOKEN_KEY = "token_key"
    }
}