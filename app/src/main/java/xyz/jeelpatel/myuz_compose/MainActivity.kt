package xyz.jeelpatel.myuz_compose

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.ramcosta.composedestinations.DestinationsNavHost
import org.schabi.newpipe.extractor.ServiceList.YouTube
import org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeSearchQueryHandlerFactory
import xyz.jeelpatel.myuz_compose.newpipeutils.initNewPipeExtractor
import xyz.jeelpatel.myuz_compose.ui.theme.MyuzcomposeTheme
//import xyz.jeelpatel.myuz_compose.viewModels.BottomBarViewModel
import xyz.jeelpatel.myuz_compose.views.NavGraphs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNewPipeExtractor()
        setContent {
//            val bottomBarViewModel = viewModel<BottomBarViewModel>()
            MyuzcomposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        bottomBar = {
                            Text("WIP BOTTOM BAR")
                        },
                        content = {
                            DestinationsNavHost(navGraph = NavGraphs.root, modifier = Modifier.padding(it))
                        }
                    )
                }
            }
        }
    }
}
