package xyz.jeelpatel.myuz_compose.views

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.schabi.newpipe.extractor.channel.ChannelInfoItem

@Destination
@Composable
fun ArtistDetails(
    navigator: DestinationsNavigator,
    media: ChannelInfoItem
){
    Text(text = "ARTIST : ${media.name}")
}