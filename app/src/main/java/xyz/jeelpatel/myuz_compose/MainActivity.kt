package xyz.jeelpatel.myuz_compose

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ramcosta.composedestinations.DestinationsNavHost
import xyz.jeelpatel.myuz_compose.newpipeutils.initNewPipeExtractor
import xyz.jeelpatel.myuz_compose.ui.theme.MyuzcomposeTheme
import xyz.jeelpatel.myuz_compose.views.NavGraphs
import xyz.jeelpatel.myuz_compose.views.PlayerViewModel
import xyz.jeelpatel.myuz_compose.views.BottomPlayer
import xyz.jeelpatel.myuz_compose.views.MusicPlayer

class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNewPipeExtractor()
        setContent {
            MyuzcomposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val context = LocalContext.current
                    // LOCK Orientation to portrait
                    val activity = context as Activity
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                    PlayerViewModel.initPlayer(context)
                    DisposableEffect(key1 = Unit) { onDispose { PlayerViewModel.exoPlayer.release() } }

                    var expanded by remember { mutableStateOf(false) }

                    Scaffold(
                        bottomBar = {
                            BottomPlayer(visible=!expanded,onClick = {expanded = !expanded })
                        },
                        content = {
                            DestinationsNavHost(navGraph = NavGraphs.root, modifier = Modifier.padding(it))
                            MusicPlayer(toggle = expanded, onBackPressed = {expanded = !expanded})
                        }
                    )
                }
            }
        }
    }
}
