package at.sphericalk.gidget.model

data class Event(
    val id: String,
    val type: EventType,
    val actor: Actor,
    val repo: Repo,
    val payload: Payload,
    val public: Boolean,
    val createdAt: String,
    val org: Actor? = null
)

data class Actor(
    val id: Long,
    val login: String,
    val displayLogin: String? = null,
    val gravatarID: String,
    val url: String,
    val avatarURL: String
)

data class Payload(
    val action: Action? = null,
    val release: Release? = null,
    val forkee: Forkee? = null,
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
    val commits: List<Commit>? = null
)

enum class Action {
    Published,
    Started
}

data class Commit(
    val sha: String,
    val author: Author,
    val message: String,
    val distinct: Boolean,
    val url: String
)

data class Author(
    val email: String,
    val name: String
)

data class Forkee(
    val id: Long,
    val nodeID: String,
    val name: String,
    val fullName: String,
    val private: Boolean,
    val owner: Owner,
    val htmlURL: String,
    val description: String,
    val fork: Boolean,
    val url: String,
    val forksURL: String,
    val keysURL: String,
    val collaboratorsURL: String,
    val teamsURL: String,
    val hooksURL: String,
    val issueEventsURL: String,
    val eventsURL: String,
    val assigneesURL: String,
    val branchesURL: String,
    val tagsURL: String,
    val blobsURL: String,
    val gitTagsURL: String,
    val gitRefsURL: String,
    val treesURL: String,
    val statusesURL: String,
    val languagesURL: String,
    val stargazersURL: String,
    val contributorsURL: String,
    val subscribersURL: String,
    val subscriptionURL: String,
    val commitsURL: String,
    val gitCommitsURL: String,
    val commentsURL: String,
    val issueCommentURL: String,
    val contentsURL: String,
    val compareURL: String,
    val mergesURL: String,
    val archiveURL: String,
    val downloadsURL: String,
    val issuesURL: String,
    val pullsURL: String,
    val milestonesURL: String,
    val notificationsURL: String,
    val labelsURL: String,
    val releasesURL: String,
    val deploymentsURL: String,
    val createdAt: String,
    val updatedAt: String,
    val pushedAt: String,
    val gitURL: String,
    val sshURL: String,
    val cloneURL: String,
    val svnURL: String,
    val homepage: String? = null,
    val size: Long,
    val stargazersCount: Long,
    val watchersCount: Long,
    val language: Any? = null,
    val hasIssues: Boolean,
    val hasProjects: Boolean,
    val hasDownloads: Boolean,
    val hasWiki: Boolean,
    val hasPages: Boolean,
    val forksCount: Long,
    val mirrorURL: Any? = null,
    val archived: Boolean,
    val disabled: Boolean,
    val openIssuesCount: Long,
    val license: License? = null,
    val forks: Long,
    val openIssues: Long,
    val watchers: Long,
    val defaultBranch: String,
    val public: Boolean
)

data class License(
    val key: String,
    val name: String,
    val spdxID: String,
    val url: String,
    val nodeID: String
)

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
    val type: OwnerType,
    val siteAdmin: Boolean
)

enum class OwnerType {
    User
}

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

data class Asset(
    val url: String,
    val id: Long,
    val nodeID: String,
    val name: String,
    val label: String,
    val uploader: Owner,
    val contentType: String,
    val state: String,
    val size: Long,
    val downloadCount: Long,
    val createdAt: String,
    val updatedAt: String,
    val browserDownloadURL: String
)

data class Repo(
    val id: Long,
    val name: String,
    val url: String
)

enum class EventType {
    CreateEvent,
    ForkEvent,
    PushEvent,
    ReleaseEvent,
    WatchEvent
}
