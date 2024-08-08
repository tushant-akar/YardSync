package com.example.yardsync.model

sealed class EmployeeState {
    object Loading : EmployeeState()
    data class Success(val message: String) : EmployeeState()
    data class Error(val message: String) : EmployeeState()
}