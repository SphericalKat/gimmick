package at.sphericalk.gidget.data.db

//import at.sphericalk.gidget.model.Action
import androidx.room.TypeConverter
import at.sphericalk.gidget.model.Asset
import at.sphericalk.gidget.model.EventType
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type


object Converters {
    val moshi: Moshi = Moshi.Builder().build()
    private val listAsset: Type = Types.newParameterizedType(
        MutableList::class.java,
        Asset::class.java
    )
    val adapter = moshi.adapter<List<Asset>>(listAsset)

    @TypeConverter
    fun fromEventType(value: EventType) = when (value) {
        EventType.CommitCommentEvent -> 1
        EventType.CreateEvent -> 2
        EventType.DeleteEvent -> 3
        EventType.ForkEvent -> 4
        EventType.GollumEvent -> 5
        EventType.IssueCommentEvent -> 6
        EventType.IssuesEvent -> 7
        EventType.MemberEvent -> 8
        EventType.PublicEvent -> 9
        EventType.PullRequestEvent -> 10
        EventType.PullRequestReviewEvent -> 11
        EventType.PullRequestReviewCommentEvent -> 12
        EventType.PushEvent -> 13
        EventType.ReleaseEvent -> 14
        EventType.SponsorshipEvent -> 15
        EventType.WatchEvent -> 16
    }

    @TypeConverter
    fun toEventType(value: Int) = when (value) {
        1 -> EventType.CommitCommentEvent
        2 -> EventType.CreateEvent
        3 -> EventType.DeleteEvent
        4 -> EventType.ForkEvent
        5 -> EventType.GollumEvent
        6 -> EventType.IssueCommentEvent
        7 -> EventType.IssuesEvent
        8 -> EventType.MemberEvent
        9 -> EventType.PublicEvent
        10 -> EventType.PullRequestEvent
        11 -> EventType.PullRequestReviewEvent
        12 -> EventType.PullRequestReviewCommentEvent
        13 -> EventType.PushEvent
        14 -> EventType.ReleaseEvent
        15 -> EventType.SponsorshipEvent
        16 -> EventType.WatchEvent
        else -> throw IllegalStateException("unknown event type")
    }

//    @TypeConverter
//    fun fromActionType(value: Action?) = when (value) {
//        Action.Published -> 1
//        Action.Started -> 2
//        Action.Opened -> 3
//        Action.Closed -> 4
//        Action.Reopened -> 5
//        Action.Assigned -> 6
//        Action.Unassigned -> 7
//        Action.Labeled -> 8
//        Action.Unlabeled -> 9
//        Action.Added -> 10
//        else -> null
//    }
//
//    @TypeConverter
//    fun toActionType(value: Int?) = when (value) {
//        1 -> Action.Published
//        2 -> Action.Started
//        3 -> Action.Opened
//        4 -> Action.Closed
//        5 -> Action.Reopened
//        6 -> Action.Assigned
//        7 -> Action.Unassigned
//        8 -> Action.Labeled
//        9 -> Action.Unlabeled
//        10 -> Action.Added
//        null -> null
//        else -> throw IllegalStateException("unknown action type")
//    }

    @TypeConverter
    fun fromAssetList(value: List<Asset>): String = adapter.toJson(value)

    @TypeConverter
    fun toAssetList(value: String): List<Asset>? = adapter.fromJson(value)
}