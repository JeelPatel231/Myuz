package xyz.jeelpatel.myuz_compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.schabi.newpipe.extractor.InfoItem
import xyz.jeelpatel.myuz_compose.viewModels.SearchListViewModel
import xyz.jeelpatel.myuz_compose.viewModels.TabNames
import xyz.jeelpatel.myuz_compose.newpipeutils.LoadStates
import xyz.jeelpatel.myuz_compose.views.PlayerViewModel
import xyz.jeelpatel.myuz_compose.views.destinations.AlbumDetailsDestination
import xyz.jeelpatel.myuz_compose.views.destinations.ArtistDetailsDestination
import xyz.jeelpatel.myuz_compose.views.destinations.PlaylistDetailsDestination

fun LazyListState.isScrolledToEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

@Composable
fun SearchResultEntry(media: InfoItem,onClick:()->Unit){
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick))
    {
        AsyncImage(
            modifier = Modifier
                .width(64.dp)
                .height(64.dp)
                .clip(RoundedCornerShape(8.dp)),
            model = media.thumbnailUrl,
            contentDescription = null,
//            placeholder = painterResource(R.drawable.ic_launcher_background),
        )
        Text(
            modifier = Modifier.padding(8.dp,0.dp,0.dp,0.dp),
            text = media.name
        )
    }
}

fun searchItemOnClick(media:InfoItem, tabName:TabNames, navigator: DestinationsNavigator) {
    return when (tabName){
        TabNames.SONG -> { PlayerViewModel.play(media.url) }
        TabNames.ARTIST -> navigator.navigate(ArtistDetailsDestination(media))
        TabNames.ALBUM -> navigator.navigate(AlbumDetailsDestination(media))
        TabNames.PLAYLIST -> navigator.navigate(PlaylistDetailsDestination(media))
    }
}

@Composable
fun ResultViewList(model:SearchListViewModel, navigator: DestinationsNavigator){
    // REMEMBER SCROlL STATE FOR LISTENING TO END REACHED
    val state = rememberLazyListState()

    LazyColumn(state = state, modifier = Modifier.fillMaxHeight()){

        // ACTUAL COMPONENT
        items(model.data) {
            SearchResultEntry(it, onClick = { searchItemOnClick(it,model.tabname, navigator) })
        }

        // SHOW LOADING INDICATOR WHEN DATA IS BEING LOADED
        item {
            Box(modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()) {
                if (model.loading == LoadStates.LOADING)
                    LinearProgressIndicator(
                        Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    )
                if (model.endReached)
                    Divider(thickness = 2.dp, modifier = Modifier
                        .padding(0.dp, 2.dp)
                        .height(4.dp))
            }
        }
    }

//   IF END OF LIST IS REACHED, LOAD MORE DATA
    val endReached by remember {
        derivedStateOf {
            state.isScrolledToEnd() &&
            model.loading != LoadStates.UNINIT &&
            !model.endReached
        }
    }
    if (endReached) model.loadmore()
}
