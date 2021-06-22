package at.sphericalk.gidget.data.network

import at.sphericalk.gidget.model.Event
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GithubService {
    @GET("/users/{username}/received_events")
    fun getReceivedEvents(
        @Header("accept") accept: String = "application/vnd.github.v3+json",
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Response<List<Event>>
}