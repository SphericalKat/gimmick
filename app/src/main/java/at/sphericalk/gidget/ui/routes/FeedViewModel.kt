package at.sphericalk.gidget.ui.routes

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import at.sphericalk.gidget.dataStore
import at.sphericalk.gidget.model.Event
import at.sphericalk.gidget.repo.GithubRepository
import at.sphericalk.gidget.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val app: Application,
    private val repository: GithubRepository
) :
    AndroidViewModel(app) {

    // list of events, exposed as state
    var events = mutableStateListOf<Event>()
        private set

    @FlowPreview
    fun fetchEvents() {
        // fetch events from DB
        viewModelScope.launch(Dispatchers.IO) {
            repository.getEventsFromDb()
                .catch { exception -> // catch any nasty exceptions
                    Log.e(
                        "VIEWMODEL",
                        "Error while fetching events from DB:",
                        exception
                    )
                }.collect {
                    events.clear()
                    events.addAll(it)
                }
        }

        // retrieve events from API and update db
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve token and username
            val token = app.dataStore.data.map { it[Constants.API_KEY] ?: "" }.first()
            val username = app.dataStore.data.map { it[Constants.USERNAME] ?: "" }.first()

            // fetch events from API
            repository.getReceivedEvents(token, username)
        }
    }

    fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String,
        redirectUrl: String
    ) = liveData {
        try {
            repository.getAccessToken(
                clientId,
                clientSecret,
                code,
                redirectUrl
            ).catch { e ->
                Log.e(
                    "VIEWMODEL",
                    "Something went wrong fetching the auth token",
                    e
                )
            }
                .collect {
                    if (it.error != null || it.access_token == null) {
                    } else {
                        val user = repository.getUser(it.access_token)
                        emit(Pair(it.access_token, user.login))
                    }
                }
        } catch (e: Exception) {
            Log.e("VIEWMODEL", "Something went wrong fetching the auth token", e)
        }
    }
}