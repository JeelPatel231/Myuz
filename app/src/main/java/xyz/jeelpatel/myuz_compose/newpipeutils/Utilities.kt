package xyz.jeelpatel.myuz_compose.newpipeutils

import androidx.compose.foundation.lazy.LazyListState
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.ServiceList.YouTube
import org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeSearchQueryHandlerFactory.MUSIC_SONGS
import org.schabi.newpipe.extractor.stream.AudioStream

fun initNewPipeExtractor(){
    NewPipe.init(CustomDownloader.getInstance())
}
