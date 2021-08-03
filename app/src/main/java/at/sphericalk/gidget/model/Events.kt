package at.sphericalk.gidget.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Keep
@Entity
data class Event(
    @PrimaryKey val id: String,
    val type: EventType,
    @Embedded val actor: Actor,
    @Embedded val repo: Repo,
    @Embedded var repoExtra: RepoExtra? = RepoExtra(),
    @Embedded val payload: Payload?,
    @ColumnInfo(name = "event_created_at") val created_at: String,
)

@Keep
data class RepoExtra(
    @ColumnInfo(name = "repo_extra_html_url") val html_url: String? = null,
    @ColumnInfo(name = "repo_extra_description") val description: String? = null,
    val language: String? = null
)

@Keep
data class Actor(
    val login: String,
    val display_login: String? = null,
    val gravatar_id: String,
    @ColumnInfo(name = "actor_url") val url: String,
    val avatar_url: String
)

@Keep
data class Payload(
    val action: String? = null,
    @Embedded val release: Release? = null,
    val ref: String? = null,
    val ref_type: String? = null,
    val master_branch: String? = null,
    @ColumnInfo(name = "payload_description") val description: String? = null,
    val pusher_type: String? = null,
    val push_id: Long? = null,
    val size: Long? = null,
    val distinct_size: Long? = null,
    val head: String? = null,
    val before: String? = null,
    @Embedded val member: Member?,
    @Embedded val forkee: Forkee?,
    @Embedded val issue: Issue?
)

@Keep
data class Member(
    @ColumnInfo(name = "payload_member_name") val login: String
)

@Keep
data class Forkee(
    @ColumnInfo(name = "forkee_full_name") val full_name: String,
    @ColumnInfo(name = "forkee_html_url") val html_url: String,
    @ColumnInfo(name = "forkee_description") val description: String?
)

@Keep
data class Issue(
    @ColumnInfo(name = "issue_html_url") val html_url: String,
    @ColumnInfo(name = "issue_number") val number: Long,
    @ColumnInfo(name = "issue_title") val title: String,
)

@Keep
data class Release(
    @ColumnInfo(name = "release_url") val url: String,
    val assets_url: String,
    val upload_url: String,
    @ColumnInfo(name = "release_html_url") val html_url: String,
    val tag_name: String,
    val target_commitish: String,
    val name: String,
    val draft: Boolean,
    val prerelease: Boolean,
    @ColumnInfo(name = "release_created_at") val created_at: String,
    val published_at: String,
    val assets: List<Asset>,
    val tarball_url: String,
    val zipball_url: String,
    val body: String,
    val short_description_html: String? = null,
    val is_short_description_html_truncated: Boolean
)

@Keep
data class Asset(
    @ColumnInfo(name = "asset_url") val url: String,
    val name: String,
    val label: String,
    val content_type: String,
    val state: String,
    val size: Long,
    val download_count: Long,
    @ColumnInfo(name = "asset_created_at") val created_at: String,
    @ColumnInfo(name = "asset_updated_at") val updated_at: String,
    val browser_download_url: String
)

@Keep
data class Repo(
    @ColumnInfo(name = "repo_name") val name: String,
    @ColumnInfo(name = "repo_url") val url: String
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
        IssueCommentEvent -> ""
        IssuesEvent -> ""
        MemberEvent -> "added"
        PullRequestEvent -> "created a pull request in"
        PullRequestReviewEvent -> "reviewed a pull request in"
        PullRequestReviewCommentEvent -> "commented on a pull request review in"
    }
}
