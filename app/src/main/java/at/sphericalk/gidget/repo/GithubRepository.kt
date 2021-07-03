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
}