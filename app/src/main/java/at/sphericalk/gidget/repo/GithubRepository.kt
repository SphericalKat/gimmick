package at.sphericalk.gidget.repo

import at.sphericalk.gidget.data.network.GithubDataSource
import at.sphericalk.gidget.model.RepoExtra
import at.sphericalk.gidget.utils.destructure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GithubRepository @Inject constructor(private val dataSource: GithubDataSource) {
    suspend fun getReceivedEvents(token: String, username: String) = flow {
        emit(dataSource.getReceivedEvents(token, username))
    }.flowOn(Dispatchers.IO)

    suspend fun getRepo(token: String, repo: String): RepoExtra {
        val (owner, name) = repo.split("/").destructure()
        return dataSource.getRepo(token, owner, name)
    }

    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String,
        redirectUrl: String
    ) = flow {
        emit(dataSource.getAccessToken(clientId, clientSecret, code, redirectUrl))
    }

    suspend fun getUser(token: String) = dataSource.getUser("token $token")
}