package xyz.jeelpatel.myuz_compose.viewModels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.Page
import org.schabi.newpipe.extractor.ServiceList
import org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeSearchQueryHandlerFactory as YSQHF
import xyz.jeelpatel.myuz_compose.newpipeutils.LoadStates

abstract class SearchListViewModel : ViewModel() {
    abstract val filters : List<String>
    abstract val tabname : TabNames

    // Private variables
    private lateinit var _query:String

    // public getters, private setters
    var nextpage: Page? = null
        private set
    var data = mutableStateListOf<InfoItem>()
        private set
    var loading by mutableStateOf(LoadStates.UNINIT)
        private set
    var endReached = false
        private set


//    var exception by mutableStateOf("")
//      private set


    private fun loadFromNewPipe(): List<InfoItem> {
        val extractor = ServiceList.YouTube.getSearchExtractor(_query, filters , "")
        if (nextpage == null) {
            extractor.fetchPage()
            nextpage = extractor.initialPage.nextPage
            if (nextpage == null) endReached = true
            return extractor.initialPage.items
        }

        val newPage = extractor.getPage(nextpage)
        nextpage = newPage.nextPage
        if (nextpage == null) endReached = true
        return newPage.items
    }

    fun loadmore() {
        viewModelScope.launch(Dispatchers.IO) {
            loading = LoadStates.LOADING
            data += loadFromNewPipe().toMutableStateList()
            loading = LoadStates.SUCCESS
        }
    }

    fun init(query: String){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                data.clear()
                endReached = false
                _query = query
                nextpage = null
                loading = LoadStates.LOADING
                data += loadFromNewPipe().toMutableStateList()
                loading = LoadStates.SUCCESS
            } catch (e:Exception){
//                exception = e.stackTrace
                loading = LoadStates.ERROR
            }
        }
    }
}

enum class TabNames{
    SONG,
    ALBUM,
    PLAYLIST,
    ARTIST
}

object SongListViewModel : SearchListViewModel(){
    override val filters = listOf(YSQHF.MUSIC_SONGS)
    override val tabname = TabNames.SONG
}

object AlbumListViewModel : SearchListViewModel(){
    override val filters = listOf(YSQHF.MUSIC_ALBUMS)
    override val tabname = TabNames.ALBUM
}

object PlaylistListViewModel : SearchListViewModel(){
    override val filters = listOf(YSQHF.MUSIC_PLAYLISTS)
    override val tabname = TabNames.PLAYLIST
}

object ArtistListViewModel : SearchListViewModel(){
    override val filters = listOf(YSQHF.MUSIC_ARTISTS)
    override val tabname = TabNames.ARTIST
}

val globalAppState = arrayOf(
    SongListViewModel,
    AlbumListViewModel,
    ArtistListViewModel,
    PlaylistListViewModel,
)