package at.sphericalk.gidget.ui.routes

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import at.sphericalk.gidget.LocalActivity
import at.sphericalk.gidget.R
import at.sphericalk.gidget.dataStore
import at.sphericalk.gidget.model.Event
import at.sphericalk.gidget.model.EventType
import at.sphericalk.gidget.ui.composables.Loading
import at.sphericalk.gidget.utils.Constants
import at.sphericalk.gidget.utils.LanguageColors
import at.sphericalk.gidget.utils.timeAgo
import at.sphericalk.gidget.utils.toColor
import coil.transform.CircleCropTransformation
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Feed(navController: NavController, viewModel: FeedViewModel, languageColors: LanguageColors) {
    val datastore = LocalActivity.current.dataStore
    val username = runBlocking { datastore.data.map { it[Constants.USERNAME] }.first() }

    // check if current user is signed in
    if (username == null) {
        navController.navigate("welcome")
    }

    viewModel.fetchEvents()

    Scaffold(topBar = {
        TopAppBar(
            elevation = 0.dp,
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .statusBarsPadding(),
            backgroundColor = MaterialTheme.colors.background,
            title = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "@$username's feed")
                }
            },
        )
    }) {
        if (viewModel.events.isEmpty()) {
            Loading()
        } else {
            val isRefreshing by viewModel.isRefreshing.collectAsState()
            val context = LocalContext.current
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { viewModel.refresh() }) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    contentPadding = rememberInsetsPaddingValues(
                        insets = LocalWindowInsets.current.systemBars,
                        applyTop = false,
                    )
                ) {
                    items(viewModel.events) { event ->
                        Row(
                            modifier = Modifier
                                .padding(vertical = 24.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberCoilPainter(
                                    request = event.actor.avatar_url,
                                    requestBuilder = {
                                        transformations(CircleCropTransformation())
                                    },
                                    previewPlaceholder = R.drawable.github_icon,
                                    fadeIn = true
                                ),
                                contentDescription = event.actor.login,
                                modifier = Modifier.size(32.dp, 32.dp),
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(event.actor.login)
                                    }
                                    append(" ")
                                    append(event.type.toString())
                                    when (event.type) {
                                        EventType.DeleteEvent -> {
                                            append(" a ${event.payload?.ref_type}, ")
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append(event.payload?.ref.toString())
                                            }
                                            append(" in")
                                        }
                                        EventType.CreateEvent -> {
                                            if (event.payload?.ref_type != "repository") {
                                                append(" a ${event.payload?.ref_type}, ")
                                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                    append(event.payload?.ref.toString())
                                                }
                                                append(" in")
                                            }
                                        }
                                        EventType.IssuesEvent -> {
                                            append("${event.payload?.action.toString()} an issue in")
                                        }
                                        EventType.MemberEvent -> {
                                            append(" ")
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append(event.payload?.member?.login.toString())
                                            }
                                            append(" to")
                                        }
                                        EventType.IssueCommentEvent -> {
                                            append("${event.payload?.action.toString()} a comment on issue ")
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append("#${event.payload?.issue?.number.toString()} ")
                                            }
                                            append("in ")
                                        }
                                    }
                                    append(" ")
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(event.repo.name)
                                    }
                                },
                                modifier = Modifier.padding(start = 16.dp),
                            )
                        }
                        Card(onClick = { handleCLick(navController, viewModel, event, context) }) {
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Column(
                                    modifier = Modifier
                                        .padding(24.dp).fillParentMaxWidth(0.75f)
                                ) {
                                    val showTitle = event.type != EventType.IssueCommentEvent
                                    if (showTitle) {
                                        Text(buildAnnotatedString {
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                when (event.type) {
                                                    EventType.ForkEvent -> {
                                                        append(event.payload?.forkee?.full_name.toString())
                                                    }
                                                    else -> {
                                                        append(event.repo.name)
                                                    }
                                                }
                                            }
                                        })
                                    }
                                    val desc = when (event.type) {
                                        EventType.ForkEvent -> event.payload?.forkee?.description
                                        EventType.IssueCommentEvent -> event.payload?.comment?.body
                                        else -> event.repoExtra?.description
                                    }
                                    desc?.let {
                                        Text(
                                            text = it,
                                            fontSize = 14.sp,
                                            modifier = Modifier.padding(top = (if (showTitle) 8 else 0).dp)
                                        )
                                    }
                                }
                                Column(Modifier.fillMaxHeight().fillParentMaxWidth().padding(end = 24.dp), verticalArrangement = Arrangement.Center) {
                                    Icon(Icons.Rounded.OpenInNew, "")
                                }
                            }
                        }
                        Row(
                            Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = event.created_at.timeAgo(), fontSize = 12.sp)


                            event.repoExtra?.language?.let {
                                val color = languageColors[it]?.toColor() ?: Color.Transparent
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Canvas(modifier = Modifier.size(12.dp), onDraw = {
                                        drawCircle(color)
                                    })
                                    Text(
                                        text = it,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun handleCLick(
    navController: NavController,
    viewModel: FeedViewModel,
    event: Event,
    context: Context
) {
    when (event.type) {
        EventType.ReleaseEvent -> {
            viewModel.selectedEvent.value = event
            navController.navigate("release")
        }
        else -> {
            val url = when (event.type) {
                EventType.ForkEvent -> event.payload?.forkee?.html_url
                EventType.IssueCommentEvent -> event.payload?.comment?.html_url
                else -> event.repoExtra?.html_url
            }
            if (url != null) {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }

        }
    }
}
