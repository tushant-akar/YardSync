package com.example.yardsync.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yardsync.model.Employee
import com.example.yardsync.model.EmployeeState
import com.example.yardsync.utils.DataStoreManager
import com.example.yardsync.utils.Supabase.client
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthViewModel(context: Context) : ViewModel() {
    private val _employeeState = MutableLiveData<EmployeeState>(EmployeeState.Loading)
    val employeeState: LiveData<EmployeeState> = _employeeState
    private val dataStoreManager = DataStoreManager(context)

    suspend fun signInWithCredentials(
        userEmail: String,
        userPassword: String
    ) {
        try {
            _employeeState.value = EmployeeState.Loading
            client.auth.signInWith(Email) {
                email = userEmail
                password = userPassword
            }
            saveToken()
            Log.d("AuthViewModel", "Token Saved: ${getToken()}")
            saveEmail()
            Log.d("AuthViewModel", "Email Saved: ${dataStoreManager.getEmployeeEmail().first()}")
            _employeeState.value = EmployeeState.Success("Logged in successfully!")
            Log.d("AuthViewModel", "${_employeeState.value}")
        } catch (e: Exception) {
            _employeeState.value = EmployeeState.Error("Error logging in: ${e.message}")
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _employeeState.value = EmployeeState.Loading
                client.auth.signOut()
                dataStoreManager.clearPreferences()
            } catch (e: Exception) {
                _employeeState.value = EmployeeState.Error("Error logging out: ${e.message}")
            }
        }
    }

    suspend fun isUserLoggedIn() {
        try {
            _employeeState.value = EmployeeState.Loading
            val token = getToken()
            if (token.isNullOrEmpty()) {
                _employeeState.value = EmployeeState.Success("User not logged in!")
            } else {
                client.auth.retrieveUser(token)
                client.auth.refreshCurrentSession()
                saveToken()
                saveEmail()
                _employeeState.value = EmployeeState.Success("User already logged in!")
            }
        } catch (e: Exception) {
            _employeeState.value = EmployeeState.Error("${e.message}")
        }
    }

    private suspend fun saveToken() {
        val accessToken = client.auth.currentAccessTokenOrNull()
        dataStoreManager.saveAccessToken(accessToken ?: "")
    }

    private suspend fun getToken(): String? {
        return dataStoreManager.getAccessToken().first()
    }

    private suspend fun saveEmail() {
        val token = getToken()
        if (token.isNullOrEmpty()) {
            return
        }
        val email = client.auth.retrieveUser(token).email
        dataStoreManager.saveEmployeeEmail(email ?: "")
    }

    suspend fun getEmployee(context: Context): Employee? {
        val email = dataStoreManager.getEmployeeEmail().first()
        return try {
            client.from("employee").select {
                filter {
                    eq("email", email ?: "")
                }
            }.decodeSingle<Employee>()
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to load employee: ${e.message}", Toast.LENGTH_SHORT)
                .show()
            null
        }
    }
}