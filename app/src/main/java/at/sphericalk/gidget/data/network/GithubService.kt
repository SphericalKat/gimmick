package at.sphericalk.gidget.data.network

import at.sphericalk.gidget.model.Event
import at.sphericalk.gidget.model.RepoExtra
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("/users/{username}/received_events")
    suspend fun getReceivedEvents(
        @Header("accept") accept: String = "application/vnd.github.v3+json",
        @Header("Authorization") token: String,
        @Path("username") username: String,
        @Query("per_page") perPage: Int = 60
    ): List<Event>

    @GET("/repos/{owner}/{name}")
    suspend fun getRepo(
        @Header("accept") accept: String = "application/vnd.github.v3+json",
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("name") name: String,
    ): RepoExtra
}