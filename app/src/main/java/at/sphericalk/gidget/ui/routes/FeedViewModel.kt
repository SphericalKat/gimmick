package at.sphericalk.gidget.ui.routes

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import at.sphericalk.gidget.dataStore
import at.sphericalk.gidget.model.Event
import at.sphericalk.gidget.repo.GithubRepository
import at.sphericalk.gidget.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

    fun fetchEvents() {
        // clear events
        events.clear()

        // launch coroutine
        viewModelScope.launch(Dispatchers.IO) {
            // retrieve token and username
            app.dataStore.data.map {
                Pair(it[Constants.API_KEY] ?: "", it[Constants.USERNAME] ?: "")
            }.collect { data ->
                // fetch events from API
                repository.getReceivedEvents(data.first, data.second)
                    .catch { exception -> // catch any nasty exceptions
                        Log.e(
                            "VIEWMODEL",
                            "Error while fetching events:",
                            exception
                        )
                    }.collect { eventList -> // collect list of events
                        eventList.asFlow() // convert to flow, we want to fetch repos too
                            .flatMapMerge { // use flatmapmerge to fetch repo per event
                                flow {
                                    try { // try getting repo
                                        val extras =
                                            repository.getRepo(data.first, it.repo.name)
                                        it.repoExtra = extras
                                    } catch (e: Exception) { // catch nasty exceptions
                                        Log.e(
                                            "VIEWMODEL",
                                            "Error while fetching events:",
                                            e
                                        )
                                    }
                                    emit(it) // emit event
                                }
                            }
                            .onCompletion { exception -> // moar exception handling
                                if (exception != null) {
                                    Log.e(
                                        "VIEWMODEL",
                                        "Error while fetching events:",
                                        exception
                                    )
                                }
                            }
                            .collect {
                                events.add(it) // add collected events to state
                            }
                    }
            }
        }
    }
}