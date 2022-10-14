package xyz.jeelpatel.myuz_compose.views

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.schabi.newpipe.extractor.InfoItem

@Destination
@Composable
fun PlaylistDetails(
    navigator: DestinationsNavigator,
    media: InfoItem
){
    Text(text = "PLAYLIST : ${media.name}")
}