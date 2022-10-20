package xyz.jeelpatel.myuz_compose.views

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.schabi.newpipe.extractor.ServiceList
import org.schabi.newpipe.extractor.stream.StreamInfoItem
import xyz.jeelpatel.myuz_compose.R

object PlayerViewModel : ViewModel(){
    lateinit var exoPlayer: ExoPlayer

    var isPlaying by mutableStateOf(false)
        private set
    var currentMediaItem: StreamInfoItem? by mutableStateOf(null)
        private set
    var playbackState by mutableStateOf(0)
        private set
    var currentPosition by mutableStateOf(0L)
        private set

    fun updatePlayer(){
        currentPosition = exoPlayer.currentPosition
        isPlaying = exoPlayer.isPlaying
        playbackState = exoPlayer.playbackState
    }

    fun initPlayer(ctx:Context){
        exoPlayer = ExoPlayer.Builder(ctx).build()

        exoPlayer.addListener(object: Player.Listener {
            override fun onIsPlayingChanged(_isPlaying: Boolean) {
                super.onIsPlayingChanged(_isPlaying)
                updatePlayer()
            }

            override fun onPlaybackStateChanged(_playbackState: Int) {
                super.onPlaybackStateChanged(_playbackState)
                if (_playbackState == Player.STATE_ENDED ) {
                    exoPlayer.playWhenReady = false
                    exoPlayer.seekTo(0)
                }
                updatePlayer()
            }

        })
    }

    fun togglePlayPause(){
        if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
    }

    fun play(media : StreamInfoItem) { viewModelScope.launch {
        exoPlayer.stop()
        currentMediaItem = media
        lateinit var _exoMedia: MediaItem
        withContext(Dispatchers.IO) {
            val se = ServiceList.YouTube.getStreamExtractor(media.url).apply { fetchPage() }
            val item = se.audioStreams
            item.sortBy { it.averageBitrate } // sort with avg bitrate
            _exoMedia = MediaItem.fromUri(item.last().content) // use the maximum bitrate available
        }
        exoPlayer.apply{
            setMediaItem(_exoMedia)
            prepare()
            play()
        }
    }}
}

@Composable
fun VideoPlayer() {
    LaunchedEffect(key1 = Unit){
        while(true){
            if (PlayerViewModel.exoPlayer.isPlaying) {
               PlayerViewModel.updatePlayer()
            }
            delay(1000)
        }
    }

    val play_arrow_btn = painterResource(R.drawable.round_play_arrow_24)
    val pause_btn = painterResource(R.drawable.round_pause_24)

    val visibleState = remember { MutableTransitionState(false) }
    visibleState.targetState = (PlayerViewModel.currentMediaItem != null)

    AnimatedVisibility(visibleState,
        enter = slideInVertically(
            initialOffsetY = { 50 }
        ) + fadeIn(initialAlpha = 0.3f),
        exit = fadeOut()
    ) {
        Column {
            Row(Modifier.padding(8.dp)) {
                Column(modifier=Modifier.weight(1F)){
                    Text(
                        text = PlayerViewModel.currentMediaItem?.name ?: "",
                        maxLines = 1,
                    )
                    Text(
                        text = PlayerViewModel.currentMediaItem?.uploaderName ?: "",
                        maxLines = 1,
                    )
                }
                IconButton(onClick = { PlayerViewModel.togglePlayPause() }) {
                    Icon(
                        painter = if(PlayerViewModel.isPlaying) pause_btn else play_arrow_btn,
                        null
                    )
                }
            }
            when (PlayerViewModel.playbackState){
                0 , 1 , 2 -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                3 , 4 -> LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = PlayerViewModel.currentPosition / PlayerViewModel.exoPlayer.contentDuration.toFloat()
                )
            }
        }
    }
}