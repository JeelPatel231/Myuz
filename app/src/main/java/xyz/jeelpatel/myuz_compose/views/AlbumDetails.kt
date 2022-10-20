package xyz.jeelpatel.myuz_compose.views

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItem

@Destination
@Composable
fun AlbumDetails(
    navigator: DestinationsNavigator,
    media: PlaylistInfoItem
){
    Text(text = "ALBUM : ${media.name}")
}