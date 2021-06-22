package at.sphericalk.gidget.model

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error<T>(val message: String) : ApiResult<T>()
    object Loading : ApiResult<Nothing>()
}