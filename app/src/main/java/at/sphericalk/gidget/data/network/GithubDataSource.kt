package at.sphericalk.gidget.data.network

import javax.inject.Inject

class GithubDataSource @Inject constructor(private val service: GithubService) {
    suspend fun getReceivedEvents(token: String, username: String) =
        service.getReceivedEvents(token = "token $token", username = username)

    suspend fun getRepo(token: String, owner: String, name: String) =
        service.getRepo(token = "token $token", owner = owner, name = name)

    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String,
        redirectUrl: String
    ) = service.getAccessToken(clientId, clientSecret, code, redirectUrl)

    suspend fun getUser(token: String) = service.getUser(token)
}