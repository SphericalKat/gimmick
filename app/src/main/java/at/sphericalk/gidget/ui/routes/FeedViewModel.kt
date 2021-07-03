package at.sphericalk.gidget.ui.routes

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import at.sphericalk.gidget.dataStore
import at.sphericalk.gidget.model.Event
import at.sphericalk.gidget.repo.GithubRepository
import at.sphericalk.gidget.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val app: Application,
    val repository: GithubRepository
) :
    AndroidViewModel(app) {


    var events = mutableStateListOf<Event>()
        private set

    fun fetchEvents() {
        viewModelScope.launch {
            app.dataStore.data.map {
                Pair(it[Constants.API_KEY] ?: "", it[Constants.USERNAME] ?: "")
            }.collect { data ->
                viewModelScope.launch(Dispatchers.IO) {
                    repository.getReceivedEvents(data.first, data.second)
                        .catch { exception ->
                            Log.e(
                                "VIEWMODEL",
                                "Error while fetching events:",
                                exception
                            )
                        }.collect {
                            events.clear()
                            events.addAll(it)
                        }
                }
            }
        }
    }
}