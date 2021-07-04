package at.sphericalk.gidget.model

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import at.sphericalk.gidget.utils.Constants
import com.google.gson.Gson

@Keep
@Entity
data class Event(
    @PrimaryKey val id: String,
    val type: EventType,
    @Embedded val actor: Actor,
    @Embedded val repo: Repo,
    @Embedded var repoExtra: RepoExtra? = RepoExtra(),
    @Embedded val payload: Payload,
    val public: Boolean,
    val created_at: String,
    @Ignore val org: Actor? = null
)

@Keep
data class RepoExtra(
    val html_url: String? = null,
    val description: String? = null,
    val language: String? = null
)

val languages: Map<String, String?> =
    Gson().fromJson(Constants.LANGUAGES, Map::class.java) as Map<String, String?>

@Keep
data class Actor(
    @Ignore val id: Long,
    val login: String,
    val display_login: String? = null,
    val gravatar_id: String,
    val url: String,
    val avatar_url: String
)

@Keep
data class Payload(
    val action: Action? = null,
    @Embedded val release: Release? = null,
    val ref: String? = null,
    val refType: String? = null,
    val masterBranch: String? = null,
    val description: String? = null,
    val pusherType: String? = null,
    val pushID: Long? = null,
    val size: Long? = null,
    val distinctSize: Long? = null,
    val head: String? = null,
    val before: String? = null,
)

@Keep
enum class Action {
    Published,
    Started
}

@Keep
data class Owner(
    val login: String,
    val id: Long,
    val nodeID: String,
    val avatarURL: String,
    val gravatarID: String,
    val url: String,
    val htmlURL: String,
    val followersURL: String,
    val followingURL: String,
    val gistsURL: String,
    val starredURL: String,
    val subscriptionsURL: String,
    val organizationsURL: String,
    val reposURL: String,
    val eventsURL: String,
    val receivedEventsURL: String,
    val siteAdmin: Boolean
)

@Keep
data class Release(
    val url: String,
    val assetsURL: String,
    val uploadURL: String,
    val htmlURL: String,
    val id: Long,
    val author: Owner,
    val nodeID: String,
    val tagName: String,
    val targetCommitish: String,
    val name: String,
    val draft: Boolean,
    val prerelease: Boolean,
    val createdAt: String,
    val publishedAt: String,
    val assets: List<Asset>,
    val tarballURL: String,
    val zipballURL: String,
    val body: String,
    val shortDescriptionHTML: String? = null,
    val isShortDescriptionHTMLTruncated: Boolean
)

@Keep
data class Asset(
    val url: String,
    @Ignore val id: Long,
    val nodeID: String,
    val name: String,
    val label: String,
    @Ignore val uploader: Owner,
    val contentType: String,
    val state: String,
    val size: Long,
    val downloadCount: Long,
    val createdAt: String,
    val updatedAt: String,
    val browserDownloadURL: String
)

@Keep
data class Repo(
    @Ignore val id: Long,
    val name: String,
    val url: String
)

@Keep
enum class EventType {
    CommitCommentEvent,
    CreateEvent,
    DeleteEvent,
    ForkEvent,
    GollumEvent,
    IssueCommentEvent,
    IssuesEvent,
    MemberEvent,
    PublicEvent,
    PullRequestEvent,
    PullRequestReviewEvent,
    PullRequestReviewCommentEvent,
    PushEvent,
    ReleaseEvent,
    SponsorshipEvent,
    WatchEvent;

    override fun toString() = when (this) {
        CreateEvent -> "created"
        ForkEvent -> "forked"
        PushEvent -> "pushed to"
        ReleaseEvent -> "created a release for"
        WatchEvent -> "starred"
        PublicEvent -> "publicly released"
        SponsorshipEvent -> "sponsored"
        CommitCommentEvent -> "commented on a commit in"
        DeleteEvent -> "deleted"
        GollumEvent -> "created a wiki page for"
        IssueCommentEvent -> "commented on an issue in"
        IssuesEvent -> "created an issue for"
        MemberEvent -> "became a member of"
        PullRequestEvent -> "created a pull request in"
        PullRequestReviewEvent -> "reviewed a pull request in"
        PullRequestReviewCommentEvent -> "commented on a pull request review in"
    }
}
