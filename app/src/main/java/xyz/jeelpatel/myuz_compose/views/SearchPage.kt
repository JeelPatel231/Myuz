package xyz.jeelpatel.myuz_compose.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import xyz.jeelpatel.myuz_compose.components.ResultViewList
import xyz.jeelpatel.myuz_compose.components.SearchBar
import xyz.jeelpatel.myuz_compose.viewModels.*

@OptIn(ExperimentalPagerApi::class)
@RootNavGraph(start = true) // sets this as the start destination of the default nav graph
@Destination
@Composable
fun SearchPage(
    navigator: DestinationsNavigator,
) {
    val pagerState = rememberPagerState(pageCount = globalAppState.size);
    val scope = rememberCoroutineScope()
    Column {
        SearchBar(globalAppState)
        TabRow(
            selectedTabIndex = pagerState.currentPage,
        ) {
            globalAppState.forEachIndexed { index, screen ->
                Tab(
                    text = { Text(screen.tabname.name) },
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.scrollToPage(index) } },
                )
            }
        }

        HorizontalPager(state = pagerState) {
            ResultViewList(globalAppState[it], navigator)
        }
    }
}
