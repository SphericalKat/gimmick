package at.sphericalk.gidget.data.network

import at.sphericalk.gidget.BuildConfig
import at.sphericalk.gidget.model.ApiResult
import retrofit2.Response
import javax.inject.Inject

class GithubDataSource @Inject constructor(private val service: GithubService) {
    suspend fun getReceivedEvents(token: String, username: String) =
        service.getReceivedEvents(token = "token $token", username = username)

    suspend fun getRepo(token: String, owner: String, name: String) =
        service.getRepo(token = "token $token", owner = owner, name = name)

    private suspend fun <T> getResult(request: suspend () -> Response<T>): ApiResult<T> {
        try {
            val response = request()
            return if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    ApiResult.Success(body)
                } else {
                    ApiResult.Error("Server response error")
                }
            } else {
                ApiResult.Error("${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            val errorMessage = e.message ?: e.toString()
            return if (BuildConfig.DEBUG) {
                ApiResult.Error("Network call failed with message: $errorMessage")
            } else {
                ApiResult.Error("Check your internet connection!")
            }
        }
    }
}