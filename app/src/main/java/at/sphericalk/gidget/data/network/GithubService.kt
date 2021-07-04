package at.sphericalk.gidget.data.network

import at.sphericalk.gidget.model.AccessTokenResponse
import at.sphericalk.gidget.model.Event
import at.sphericalk.gidget.model.GithubUser
import at.sphericalk.gidget.model.RepoExtra
import retrofit2.http.*

interface GithubService {
    @Headers("Accept: application/json")
    @POST("https://github.com/login/oauth/access_token")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUrl: String
    ): AccessTokenResponse

    @GET("/users/{username}/received_events")
    suspend fun getReceivedEvents(
        @Header("accept") accept: String = "application/vnd.github.v3+json",
        @Header("Authorization") token: String,
        @Path("username") username: String,
        @Query("per_page") perPage: Int = 60
    ): List<Event>

    @GET("/user")
    suspend fun getUser(
        @Header("Authorization") token: String,
    ): GithubUser

    @GET("/repos/{owner}/{name}")
    suspend fun getRepo(
        @Header("accept") accept: String = "application/vnd.github.v3+json",
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("name") name: String,
    ): RepoExtra
}