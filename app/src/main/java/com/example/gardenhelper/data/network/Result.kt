package com.example.gardenhelper.data.network

sealed class Result<out T> {
    data class Success<T>(val data: T): Result<T>()
    data class Error(val message: String, val code: Int? = null): Result<Nothing>()
    object NetworkError: Result<Nothing>()
}
