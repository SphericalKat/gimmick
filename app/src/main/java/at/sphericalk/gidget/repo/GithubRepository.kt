package at.sphericalk.gidget.repo

import android.util.Log
import at.sphericalk.gidget.data.db.EventDao
import at.sphericalk.gidget.data.network.GithubDataSource
import at.sphericalk.gidget.model.Event
import at.sphericalk.gidget.model.EventType
import at.sphericalk.gidget.model.RepoExtra
import at.sphericalk.gidget.utils.destructure
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val dataSource: GithubDataSource,
    private val eventDao: EventDao
) {

    private suspend fun getRepo(token: String, repo: String): RepoExtra {
        val (owner, name) = repo.split("/").destructure()
        return dataSource.getRepo(token, owner, name)
    }

    fun getEventsFromDb(): Flow<List<Event>> {
        return eventDao.getAllEvents()
    }

    suspend fun saveEventToDb(event: Event) = eventDao.insertEvent(event)

    @OptIn(FlowPreview::class)
    suspend fun fetchEvents(token: String, username: String) =
        dataSource.getReceivedEvents(token, username).asFlow().flatMapMerge {
            flow {
                try { // try getting repo
                    val extras =
                        getRepo(token, it.repo.name)
                    it.repoExtra = extras
                } catch (e: Exception) { // catch nasty exceptions
                    Log.e("REPOSITORY", "Error while fetching repos for events:", e)
                }
                emit(it) // emit event
            }
        }.onCompletion { exception -> // moar exception handling
            if (exception != null) {
                Log.e("REPOSITORY", "Error while fetching events:", exception)
            }
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