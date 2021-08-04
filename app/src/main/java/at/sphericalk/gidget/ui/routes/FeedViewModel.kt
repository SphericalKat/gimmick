package at.sphericalk.gidget.ui.routes

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import at.sphericalk.gidget.dataStore
import at.sphericalk.gidget.model.ApiResult
import at.sphericalk.gidget.model.Event
import at.sphericalk.gidget.repo.GithubRepository
import at.sphericalk.gidget.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val app: Application,
    private val repository: GithubRepository
) : AndroidViewModel(app) {

    // list of events, exposed as state
    var events = mutableStateListOf<Event>()
        private set

    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    var selectedEvent = mutableStateOf<Event?>(null)

    fun fetchEvents() {
        // fetch events from DB
        repository.getEventsFromDb()
            .onEach {
                events.clear()
                events.addAll(it)
            }
            .catch { e -> // catch any nasty exceptions
                Log.e("VIEWMODEL", "Error while fetching events from DB:", e)
            }.launchIn(viewModelScope)

        // retrieve events from API and update db
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve token and username
            val prefs = app.dataStore.data.first()
            val token = prefs[Constants.API_KEY] ?: ""
            val username = prefs[Constants.USERNAME] ?: ""

            // fetch events from API
            repository.fetchEvents(token, username).collect { repository.saveEventToDb(it) }
        }
    }

    fun refresh() {
        // retrieve events from API and update db
        viewModelScope.launch(Dispatchers.IO) {
            _isRefreshing.emit(true)
            // retrieve token and username
            val prefs = app.dataStore.data.first()
            val token = prefs[Constants.API_KEY] ?: ""
            val username = prefs[Constants.USERNAME] ?: ""

            // fetch events from API
            repository.fetchEvents(token, username).collect {
                repository.saveEventToDb(it)
                _isRefreshing.emit(false)
            }
        }
    }

    fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String,
        redirectUrl: String,
        dataStore: DataStore<Preferences>
    ) = liveData<ApiResult<Unit>> {
        emit(ApiResult.Loading)

        repository.getAccessToken(
            clientId,
            clientSecret,
            code,
            redirectUrl
        ).catch { e ->
            Log.e("VIEWMODEL", "Something went wrong fetching the auth token", e)
            emit(ApiResult.Error("Something went wrong fetching the auth token"))
        }.collect {
            if (it.error != null || it.access_token == null) {
                Log.e("VIEWMODEL", "Could not fetch access token")
                emit(ApiResult.Error("Something went wrong fetching the auth token"))
            } else {
                val user = repository.getUser(it.access_token)
                dataStore.edit { prefs ->
                    prefs[Constants.API_KEY] = it.access_token
                    prefs[Constants.USERNAME] = user.login
                }
                emit(ApiResult.Success(Unit))
            }
        }
    }
}