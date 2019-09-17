package com.example.nasa.data

sealed class NetworkState {

    object Loading : NetworkState()

    object Success : NetworkState()

    object BadRequestError : NetworkState()

    object NotFoundError : NetworkState()

    object ServerError : NetworkState()

    // God only knows what happened ¯\_(ツ)_/¯
    data class UnknownError(val errorCode: Int):NetworkState()

    // To handle exceptions for network states, separating failure cases from exceptions
    data class Exception(val message: String? = null) : NetworkState()
}