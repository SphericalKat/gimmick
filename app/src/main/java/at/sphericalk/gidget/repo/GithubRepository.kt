package at.sphericalk.gidget.repo

import at.sphericalk.gidget.data.network.GithubDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GithubRepository @Inject constructor(private val dataSource: GithubDataSource) {
    suspend fun getReceivedEvents(token: String, username: String) = flow {
        var eventList = dataSource.getReceivedEvents(token, username)
        emit(eventList)
    }.flowOn(Dispatchers.IO)
}